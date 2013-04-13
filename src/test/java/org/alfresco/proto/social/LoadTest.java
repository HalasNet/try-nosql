package org.alfresco.proto.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.alfresco.proto.social.api.Activity;
import org.alfresco.proto.social.api.ActivityService;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.api.Relationship;
import org.alfresco.proto.social.api.SocialService;
import org.alfresco.proto.social.dao.mongo.MongoDAOFactory;
import org.alfresco.proto.social.dao.mongo.MongoRelationshipCountJob;
import org.alfresco.proto.social.job.ActivityPostJob;
import org.alfresco.proto.social.service.ServiceFactory;
import org.alfresco.proto.util.LaunchThreads;
import org.alfresco.proto.util.counter.Counter;
import org.alfresco.proto.util.counter.Counters;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoException;


public abstract class LoadTest
{
    private static int SECONDS = 10;
    private static int AVG_PEOPLE_PER_SECOND = 10000;
    
    private ServiceFactory service;
    private SocialService socialService;
    private ActivityService activityService;

    private AtomicInteger pId;
    private ConcurrentHashMap<Integer, AtomicInteger> friends = new ConcurrentHashMap<Integer, AtomicInteger>();
    private ConcurrentHashMap<Integer, AtomicInteger> followers = new ConcurrentHashMap<Integer, AtomicInteger>();

    
    @Before public void setUp() throws Exception
    {
        Counters.instance.reset();
        
        service = createServiceFactory();
        socialService = service.createSocialService();
        activityService = service.createActivityService();
        
        pId = new AtomicInteger();
        initCounts(friends);
        initCounts(followers);
    }

    protected abstract ServiceFactory createServiceFactory() throws Exception;
    
    private void initCounts(ConcurrentHashMap<Integer, AtomicInteger> counts)
    {
        for (int i = 0; i < (SECONDS * AVG_PEOPLE_PER_SECOND) ; i++)
        {
            counts.put(i, new AtomicInteger());
        }
    }
    
    @Test public void loadPeople() throws Exception
    {
        Thread log = Counters.instance.startLogging();
        
        // NOTE: Mongo only
//        LaunchThreads.Threads relCountThreads = LaunchThreads.start(new RelationshipCountRunnable(), 1);
        
        // create people
        LaunchThreads.Threads createPeopleThreads = LaunchThreads.start(new CreatePeopleRunnable(), 1);
        // follow people, and post activities
        LaunchThreads.Threads followPeopleThreads = LaunchThreads.start(new FollowPeopleRunnable(), 4);
        LaunchThreads.Threads activityPostThreads = LaunchThreads.start(new ActivityPostRunnable(), 1);
        
        Thread.sleep(SECONDS * 1000);
        
        createPeopleThreads.stop();
        followPeopleThreads.stop();
//        relCountThreads.stop();
        activityPostThreads.stop();
        
        log.interrupt();
        log.join();
        Counters.instance.logCounters();
        
        for (int i = 1; i <= pId.get(); i++)
        {
            Person person = socialService.getPersonByEmail(i + "@example.org");
            assertNotNull("person " + i + "@example.org exists", person);
            assertEquals("person " + i + "@example.org friend count", friends.get(i).get(), person.getFriendCount());
            assertEquals("person " + i + "@example.org follower count", followers.get(i).get(), person.getFollowerCount());
            List<Relationship> friendRels = socialService.getFriends(person);
            assertEquals("person " + i + "@example.org friends", friends.get(i).get(), friendRels.size());
            List<Relationship> followerRels = socialService.getFollowers(person);
            assertEquals("person " + i + "@example.org followers", followers.get(i).get(), followerRels.size());
            List<Activity> activities = activityService.getActivities(person);
            if (friendRels.size() == 0)
                assertTrue(activities.size() == 0);
            if (activities.size() > 0)
                assertTrue(friendRels.size() > 0);
            
            //System.out.println("Person " + i + " friends=" + person.getFriendCount() + ", followers=" + person.getFollowerCount() + ", activities=" + activities.size());
        }
    }

    public class CreatePeopleRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Counter counter = Counters.instance.createThreadCounter("CreatePeople");
            counter.start();
            
            while(true)
            {
                for (int i = 0; i < 1000; i++)
                {
                    int id = pId.incrementAndGet();
                    String name = new Integer(id).toString();
                    Person person = LoadTest.this.socialService.newPerson(name, name + "@example.org");
                    LoadTest.this.socialService.savePerson(person);
                    counter.incrementCount();
                }
                if (Thread.interrupted())
                {
                    counter.stop();
                    return;
                }
            }
        }
    }

    public class FollowPeopleRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Counter counter = Counters.instance.createThreadCounter("FollowPeople");
            counter.start();
            
            while(true)
            {
                for (int i = 0; i < 1000; i++)
                {
                    int id = pId.get(); 
                    int from = new Random().nextInt(id) +1;
                    int to = new Random().nextInt(id) +1;
                    if (from != to)
                    {
//                        System.out.println("Creating friend between " + from + " and " + to);
                        Person fromPerson = LoadTest.this.socialService.getPersonByEmail(from + "@example.org");
                        Person toPerson = LoadTest.this.socialService.getPersonByEmail(to + "@example.org");
                        if (fromPerson != null && toPerson != null)
                        {
                            try
                            {
                                LoadTest.this.socialService.addFriend(fromPerson, toPerson);
                                counter.incrementCount();
                                
                                AtomicInteger friendCount = friends.get(from);
                                friendCount.incrementAndGet();
                                AtomicInteger followerCount = followers.get(to);
                                followerCount.incrementAndGet();
                                
//                                System.out.println("Created  friend between " + from + " and " + to);
                            }
                            catch(MongoException.DuplicateKey e)
                            {
                            }
                        }
                    }
                }
                if (Thread.interrupted())
                {
                    counter.stop();
                    return;
                }
            }
        }
    }

    public class ActivityPostRunnable implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                ActivityPostJob activityPostJob = service.createActivityPostJob();
                activityPostJob.consumePosts();
            }
            catch (Exception e)
            {
                return;
            }
        }
    }

    // NOTE: For Mongo Only
    public class RelationshipCountRunnable implements Runnable
    {
        private MongoRelationshipCountJob relCountJob;
        
        @Override
        public void run()
        {
            MongoDAOFactory dao;
            try
            {
                dao = new MongoDAOFactory();
            }
            catch (Exception e)
            {
                System.out.println("Failed to initialise Mongo Relationship Job Count " + e.getMessage());
                return;
            }
            relCountJob = new MongoRelationshipCountJob(dao.getJobDAO(), dao.getRelationshipDAO(), dao.getPersonDAO());
            
            while(true)
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    return;
                }
                relCountJob.updateFriendCounts();
            }
        }
    }

}

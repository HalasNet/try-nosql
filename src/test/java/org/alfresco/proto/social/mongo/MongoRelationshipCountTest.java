package org.alfresco.proto.social.mongo;

import static org.junit.Assert.assertEquals;

import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.api.SocialService;
import org.alfresco.proto.social.dao.mongo.MongoDAOFactory;
import org.alfresco.proto.social.dao.mongo.MongoRelationshipCountJob;
import org.alfresco.proto.social.service.ServiceFactory;
import org.junit.Before;
import org.junit.Test;


public class MongoRelationshipCountTest
{
    private MongoDAOFactory dao;
    private SocialService social;

    private Person a;
    private Person b;
    
    @Before public void setUp() throws Exception
    {
        dao = new MongoDAOFactory();
        social = new ServiceFactory(dao).createSocialService();
        
        a = social.newPerson("A", "a@example.org");
        social.savePerson(a);
        b = social.newPerson("B", "b@example.org");
        social.savePerson(b);
        social.addFriend(a, b);
    }

    
    @Test public void executeJob()
    {
        MongoRelationshipCountJob job = new MongoRelationshipCountJob(dao.getJobDAO(), dao.getRelationshipDAO(), dao.getPersonDAO());
        job.updateFriendCounts();
        
        Person aUpdated = social.getPersonById(a.getId());
        assertEquals(1, aUpdated.getFriendCount());
        assertEquals(0, aUpdated.getFollowerCount());
        Person bUpdated = social.getPersonById(b.getId());
        assertEquals(0, bUpdated.getFriendCount());
        assertEquals(1, bUpdated.getFollowerCount());
    }
}

package org.alfresco.proto.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.api.Relationship;
import org.alfresco.proto.social.api.SocialService;
import org.alfresco.proto.social.service.ServiceFactory;
import org.junit.Before;
import org.junit.Test;

public abstract class SocialServiceImplTest
{
    private ServiceFactory service;
    private SocialService socialService;
    
    @Before public void setUp() throws Exception
    {
        service = createServiceFactory();
        socialService = service.createSocialService();
    }
    
    protected abstract ServiceFactory createServiceFactory() throws Exception;
    
    @Test public void createPerson()
    {
        Person p = socialService.newPerson("Dave", "dave@example.org");
        p.getName().setFamilyName("Caruana");
        socialService.savePerson(p);
        
        assertNotNull(p.getId());
        assertEquals("Dave", p.getDisplayName());
        assertEquals("Caruana", p.getName().getFamilyName());
        assertEquals(1, p.getEmails().size());
        assertEquals("dave@example.org", p.getEmails().iterator().next().getValue());
    }
    
    @Test public void getPersonById()
    {
        Person p = socialService.newPerson("Dave", "dave@example.org");
        p.getName().setFamilyName("Caruana");
        socialService.savePerson(p);

        Person doesnotexist = socialService.getPersonById(service.getDAOFactory().createIdentifier());
        assertNull(doesnotexist);

        Person retrieved = socialService.getPersonById(p.getId());
        assertNotNull(retrieved);
        assertEquals(p.getId(), retrieved.getId());
        assertEquals(p.getDisplayName(), retrieved.getDisplayName());
    }

    @Test public void getPersonByEmail()
    {
        Person p = socialService.newPerson("Dave", "dave@example.org");
        socialService.savePerson(p);

        Person doesnotexist = socialService.getPersonByEmail("doesnotexist@example.org");
        assertNull(doesnotexist);

        Person retrieved = socialService.getPersonByEmail("dave@example.org");
        assertNotNull(retrieved);
        assertEquals(p.getId(), retrieved.getId());
        assertEquals(p.getDisplayName(), retrieved.getDisplayName());
    }

    @Test public void updatePerson()
    {
        Person p = socialService.newPerson("Dave", "dave@example.org");
        socialService.savePerson(p);

        Person retrieved = socialService.getPersonById(p.getId());
        assertNull(retrieved.getName().getFamilyName());
        
        p.getName().setFamilyName("Caruana");
        socialService.savePerson(p);
        
        retrieved = socialService.getPersonById(p.getId());
        assertEquals("Caruana", retrieved.getName().getFamilyName());
    }

    @Test public void createRelationship()
    {
        Person a = socialService.newPerson("A", "a@example.org");
        socialService.savePerson(a);
        Person b = socialService.newPerson("B", "b@example.org");
        socialService.savePerson(b);

        Relationship rel = socialService.addFriend(a, b);
        assertNotNull(rel);
        assertNotNull(rel.getId());
        assertEquals(rel.getFollower().getId(), a.getId());
        assertEquals(rel.getFollower().getDisplayName(), a.getDisplayName());
        assertEquals(rel.getFriend().getId(), b.getId());
        assertEquals(rel.getFriend().getDisplayName(), b.getDisplayName());
    }
    
    @Test public void getFriends()
    {
        Person a = socialService.newPerson("A", "a@example.org");
        socialService.savePerson(a);
        Person b = socialService.newPerson("B", "b@example.org");
        socialService.savePerson(b);
        Person c = socialService.newPerson("C", "c@example.org");
        socialService.savePerson(c);
        Person d = socialService.newPerson("D", "d@example.org");
        socialService.savePerson(d);
        
        socialService.addFriend(a, b);
        socialService.addFriend(a, c);
        
        List<Relationship> friends = socialService.getFriends(a);
        assertNotNull(friends);
        assertEquals(2, friends.size());
        List<Identifier> friendIds = new ArrayList<Identifier>();
        for (Relationship friend : friends)
        {
            friendIds.add(friend.getFriend().getId());
        }
        Set<Identifier> expected = new HashSet<Identifier>(Arrays.asList(b.getId(), c.getId()));
        assertIds(expected, friendIds);
    }
    
    @Test public void getFollowers()
    {
        Person a = socialService.newPerson("A", "a@example.org");
        socialService.savePerson(a);
        Person b = socialService.newPerson("B", "b@example.org");
        socialService.savePerson(b);
        Person c = socialService.newPerson("C", "c@example.org");
        socialService.savePerson(c);
        Person d = socialService.newPerson("D", "d@example.org");
        socialService.savePerson(d);
        
        socialService.addFriend(b, a);
        socialService.addFriend(c, a);
        
        List<Relationship> followers = socialService.getFollowers(a);
        assertNotNull(followers);
        assertEquals(2, followers.size());
        List<Identifier> followerIds = new ArrayList<Identifier>();
        for (Relationship follower : followers)
        {
            followerIds.add(follower.getFollower().getId());
        }
        Set<Identifier> expected = new HashSet<Identifier>(Arrays.asList(b.getId(), c.getId()));
        assertIds(expected, followerIds);
    }
    
    private void assertIds(Set<Identifier> expected, List<Identifier> actual)
    {
        Set<Identifier> idsRemaining = new HashSet<Identifier>(expected);
        for (Identifier id : actual)
        {
            assertTrue(expected.contains(id));   // id is in list
            assertTrue(idsRemaining.contains(id));  // id is not duplicate
            idsRemaining.remove(id);
        }
        assertEquals(0, idsRemaining.size());
    }
}

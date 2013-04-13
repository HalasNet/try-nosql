package org.alfresco.proto.social;

import java.util.Date;
import java.util.List;

import org.alfresco.proto.social.api.PersonName;
import org.alfresco.proto.social.api.PluralField;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.PersonData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.junit.Test;
import static org.junit.Assert.*;

import com.mongodb.MongoException;


public abstract class DAOTest
{
    protected DAOFactory dao;
    
    @Test public void registerPerson()
    {
        PersonData p = createPerson("Dave");
        dao.getPersonDAO().save(p);
        
        PersonData p1 = dao.getPersonDAO().get(p.getId());
        assertNotNull(p1);
        assertEquals(p.getId(), p1.getId());
    }

    @Test(expected=MongoException.DuplicateKey.class)
    public void registerDuplicateEmails()
    {
        PersonData p = createPerson("Dave");
        dao.getPersonDAO().save(p);
        PersonData p2 = createPerson("Dave");
        dao.getPersonDAO().save(p2);
    }

    @Test public void followPerson()
    {
        PersonData a = createPerson("A");
        dao.getPersonDAO().save(a);
        PersonData b = createPerson("B");
        dao.getPersonDAO().save(b);

        PersonData follower = dao.getPersonDAO().newPersonData((IdentifierData)a.getId());
        follower.setDisplayName(a.getDisplayName());
        PersonData friend = dao.getPersonDAO().newPersonData((IdentifierData)b.getId());
        friend.setDisplayName(b.getDisplayName());

        RelationshipData rel = dao.getRelationshipDAO().newRelationshipData();
        rel.setFollower(follower);
        rel.setFriend(friend);
        rel.setCreated(new Date());
        
        dao.getRelationshipDAO().save(rel);
        
        RelationshipData fRead = dao.getRelationshipDAO().get(rel.getId());
        
        assertEquals(rel.getFollower().getDisplayName(), fRead.getFollower().getDisplayName());
        assertEquals(rel.getFriend().getDisplayName(), fRead.getFriend().getDisplayName());
        
        List<RelationshipData> friendsA = dao.getRelationshipDAO().findFriends((IdentifierData)a.getId());
        assertNotNull(friendsA);
        assertEquals(1, friendsA.size());
        List<RelationshipData> friendsB = dao.getRelationshipDAO().findFriends((IdentifierData)b.getId());
        assertNotNull(friendsB);
        assertEquals(0, friendsB.size());
        List<RelationshipData> followersA = dao.getRelationshipDAO().findFollowers((IdentifierData)a.getId());
        assertNotNull(followersA);
        assertEquals(0, followersA.size());
        List<RelationshipData> followersB = dao.getRelationshipDAO().findFollowers((IdentifierData)b.getId());
        assertNotNull(followersB);
        assertEquals(1, followersB.size());
    }
    
    @Test(expected=MongoException.DuplicateKey.class)
    public void registerDuplicateFollows()
    {
        PersonData a = createPerson("A");
        dao.getPersonDAO().save(a);
        PersonData b = createPerson("B");
        dao.getPersonDAO().save(b);

        PersonData follower = dao.getPersonDAO().newPersonData((IdentifierData)a.getId());
        follower.setDisplayName(a.getDisplayName());
        PersonData friend = dao.getPersonDAO().newPersonData((IdentifierData)b.getId());
        friend.setDisplayName(b.getDisplayName());

        RelationshipData rel = dao.getRelationshipDAO().newRelationshipData();
        rel.setFollower(follower);
        rel.setFriend(friend);
        rel.setCreated(new Date());
        dao.getRelationshipDAO().save(rel);
        
        RelationshipData rel2 = dao.getRelationshipDAO().newRelationshipData();
        rel2.setFollower(follower);
        rel2.setFriend(friend);
        rel2.setCreated(new Date());
        dao.getRelationshipDAO().save(rel2);
    }
    
    @Test public void postActivity()
    {
        PersonData p = createPerson("Bob");
        dao.getPersonDAO().save(p);
        PersonData follower = createPerson("Alice");
        dao.getPersonDAO().save(follower);
        PersonData pFriend = createPerson("Fred");
        dao.getPersonDAO().save(pFriend);

        BoundActivityData a = dao.getActivityDAO().newBoundActivityData();
        a.setOriginatorPersonId(follower.getId());
        a.setPersonId(follower.getId());
        a.setTitle("${userId} followed ${to.displayName}");
        a.setBody("${userId} became friends with ${to.displayName}");
        a.setType("follow");
        a.addTemplateParam("relationship", "colleague");
        a.addTemplateParam("to", p);
        a.setForPersonId(pFriend.getId());
        
        dao.getActivityDAO().save(a);

        ActivityData aRead = dao.getActivityDAO().get(a.getId());
        assertEquals(a.getPersonId(), aRead.getPersonId());
        assertEquals(a.getTemplateParams(), aRead.getTemplateParams());
        
        List<BoundActivityData> activities = dao.getActivityDAO().findActivities((IdentifierData)p.getId());
        assertNotNull(activities);
        assertEquals(0, activities.size());
        activities = dao.getActivityDAO().findActivities((IdentifierData)pFriend.getId());
        assertNotNull(activities);
        assertEquals(1, activities.size());
    }
    
    
    private PersonData createPerson(String name)
    {
        PersonData p = dao.getPersonDAO().newPersonData();
        p.setDisplayName(name);
        PersonName personName = p.getName();
        personName.setFamilyName("Family " + name);
        personName.setFormatted("Formatted " + name);
        p.addEmail(name + "@example.org", PluralField.TypeEnum.work, true);
        p.setCreated(new Date());
        return p;
    }
    
}

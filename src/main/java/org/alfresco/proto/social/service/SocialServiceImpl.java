package org.alfresco.proto.social.service;

import java.util.Date;
import java.util.List;

import org.alfresco.proto.social.api.Activity;
import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.api.PluralField;
import org.alfresco.proto.social.api.Relationship;
import org.alfresco.proto.social.api.SocialService;
import org.alfresco.proto.social.dao.PersonDAO;
import org.alfresco.proto.social.dao.RelationshipDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.PersonData;
import org.alfresco.proto.social.dao.model.RelationshipData;


// TODO: revisit encapsulation of DAO transfer objects


public class SocialServiceImpl implements SocialService
{
    private PersonDAO personDAO;
    private RelationshipDAO relationshipDAO;
    private ActivityServiceImpl activityServiceImpl;


    public SocialServiceImpl(PersonDAO personDAO, RelationshipDAO relationshipDAO, ActivityServiceImpl activityServiceImpl)
    {
        this.personDAO = personDAO;
        this.relationshipDAO = relationshipDAO;
        this.activityServiceImpl = activityServiceImpl;
    }
    
    @Override
    public Person newPerson(String displayName, String primaryEmailAddress)
    {
        PersonData person = personDAO.newPersonData();
        person.setDisplayName(displayName);
        person.addEmail(primaryEmailAddress, PluralField.TypeEnum.other, true);
        return person;
    }

    @Override
    public void savePerson(Person person)
    {
        PersonData personData = (PersonData)person;
        if (personData.getId() == null)
        {
            personData.setCreated(new Date());
        }
        personDAO.save(personData);
    }

    @Override
    public Person getPersonById(Identifier person)
    {
        return personDAO.get(person);
    }

    @Override
    public Person getPersonByEmail(String email)
    {
        return personDAO.findByEmail(email);
    }
    
    @Override
    public Relationship addFriend(Person follower, Person friend)
    {
        RelationshipData relationship = createRelationship(follower, friend);
        relationshipDAO.save(relationship);

        // in-line update of relationship counts (instead of mapreduce)
        personDAO.incrementFollowerCount((PersonData)friend);
        personDAO.incrementFriendCount((PersonData)follower);

        Activity activity = activityServiceImpl.newActivity(follower);
        activity.setBody(follower.getDisplayName() + " followed " + friend.getDisplayName());
        activityServiceImpl.postActivity(activity);
        
        return relationship;
    }
    
    private RelationshipData createRelationship(Person follower, Person friend)
    {
        if (follower.getId().equals(friend.getId()))
        {
            throw new IllegalArgumentException("Cannot friend yourself");
        }
        
        PersonData followerPersonData = personDAO.newPersonData((IdentifierData)follower.getId());
        followerPersonData.setDisplayName(follower.getDisplayName());
        PersonData friendPersonData = personDAO.newPersonData((IdentifierData)friend.getId());
        friendPersonData.setDisplayName(friend.getDisplayName());
        
        RelationshipData relData = relationshipDAO.newRelationshipData();
        relData.setFollower(followerPersonData);
        relData.setFriend(friendPersonData);
        relData.setCreated(new Date());
        return relData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Relationship> getFriends(Person person)
    {
        return (List)relationshipDAO.findFriends((IdentifierData)person.getId());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Relationship> getFollowers(Person person)
    {
        return (List)relationshipDAO.findFollowers((IdentifierData)person.getId());
    }
    
}

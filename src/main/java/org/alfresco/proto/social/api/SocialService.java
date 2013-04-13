package org.alfresco.proto.social.api;

import java.util.List;

public interface SocialService
{
    public Person newPerson(String displayName, String primaryEmailAddress);
    
    public void savePerson(Person person);
    
    public Person getPersonById(Identifier person);
    
    public Person getPersonByEmail(String email);
    
    public Relationship addFriend(Person follower, Person friend);
    
    public List<Relationship> getFriends(Person person);
    
    public List<Relationship> getFollowers(Person person);
}

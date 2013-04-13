package org.alfresco.proto.social.dao;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.PersonData;

public interface PersonDAO
{
    public PersonData newPersonData();
    
    public PersonData newPersonData(IdentifierData person);
 
    public String toJSON(PersonData jobData);
    
    public PersonData toPersonData(String json);
    
    public void save(PersonData person);
    
    public PersonData get(Identifier person);
    
    public PersonData findByEmail(String email);
    
    // used for mapreduce approach to updating counts
    public void setRelationshipCounts(PersonData person, int friendCount, int followerCount);
    
    public void incrementFriendCount(PersonData person);

    public void incrementFollowerCount(PersonData person);
}

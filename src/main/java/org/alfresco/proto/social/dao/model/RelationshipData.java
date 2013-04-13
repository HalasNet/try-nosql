package org.alfresco.proto.social.dao.model;

import java.util.Date;

import org.alfresco.proto.social.api.Relationship;

public interface RelationshipData extends Relationship
{
    public void setFollower(PersonData follower);

    public void setFriend(PersonData friend);
    
    public void setCreated(Date created);
}

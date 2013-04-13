package org.alfresco.proto.social.dao.couch;

import java.util.Date;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.dao.model.PersonData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class CouchRelationshipData implements RelationshipData
{
    @JsonProperty("_id")
    private String objectId;

    @JsonProperty("_rev")
    private String rev;

    private CouchPersonData follower;
    
    private CouchPersonData friend;
    
    private Date created;
    
    @Override
    public Identifier getId()
    {
        return CouchIdentifierData.create(objectId);
    }

    public String getRev()
    {
        return rev;
    }

    @Override
    public CouchPersonData getFollower()
    {
        return follower;
    }

    public void setFollower(PersonData follower)
    {
        this.follower = (CouchPersonData)follower;
    }

    @Override
    public Person getFriend()
    {
        return friend;
    }
    
    public void setFriend(PersonData friend)
    {
        this.friend = (CouchPersonData)friend;
    }
    
    @Override
    public Date getCreated()
    {
        return created;
    }

    @Override
    public void setCreated(Date created)
    {
        this.created = created;
    }
}

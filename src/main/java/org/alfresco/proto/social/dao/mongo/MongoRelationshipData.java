package org.alfresco.proto.social.dao.mongo;

import java.util.Date;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.dao.model.PersonData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.Transient;

@Entity(value="relationships", noClassnameStored=true)
@Indexes({
        @Index(value="follower.objectId, friend.objectId", unique=true),
        @Index(value="friend.objectId")
})
public class MongoRelationshipData implements RelationshipData
{
    private @Id ObjectId objectId;
    @Transient private MongoIdentifierData id;

    @Embedded private MongoPersonData follower;
    
    @Embedded private MongoPersonData friend;
    
    private Date created;


    @SuppressWarnings("unused")
    @PostLoad private void postLoad()
    {
        this.id = MongoIdentifierData.create(objectId);
    }
    
    @SuppressWarnings("unused")
    @PostPersist private void postPersist()
    {
        this.id = MongoIdentifierData.create(objectId);
    }
    
    
    @Override
    public Identifier getId()
    {
        return id;
    }

//    public ObjectId getObjectId()
//    {
//        return objectId;
//    }
    
    @Override
    public Person getFollower()
    {
        return follower;
    }

    public void setFollower(PersonData follower)
    {
        this.follower = (MongoPersonData)follower;
    }

    @Override
    public Person getFriend()
    {
        return friend;
    }
    
    public void setFriend(PersonData friend)
    {
        this.friend = (MongoPersonData)friend;
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

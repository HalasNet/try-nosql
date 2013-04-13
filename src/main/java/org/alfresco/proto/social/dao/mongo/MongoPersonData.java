package org.alfresco.proto.social.dao.mongo;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.PersonName;
import org.alfresco.proto.social.api.PluralField;
import org.alfresco.proto.social.dao.model.PersonData;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.Transient;


@Entity(value="people", noClassnameStored=true)
@Indexes(@Index(value="primaryEmail.value", unique=true))
public class MongoPersonData implements PersonData
{
    private @Id ObjectId objectId;
    @Transient private MongoIdentifierData id;
    
    private String displayName;
    
    private MongoPersonNameData name;
    
    @Embedded private MongoPluralFieldData primaryEmail;
    @Embedded private Set<MongoPluralFieldData> emails;
    
    private Date created;
    
    private int friendCount;
    
    private int followerCount;

    
    public MongoPersonData()
    {
    }
    
    public MongoPersonData(ObjectId objectId)
    {
        this.objectId = objectId;
        this.id = MongoIdentifierData.create(objectId);
    }
    
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
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    @Override
    public PersonName getName()
    {
        if (name == null)
        {
            name = new MongoPersonNameData();
        }
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<PluralField> getEmails()
    {
        return emails == null ? null : (Set)Collections.unmodifiableSet(emails);
    }

    @Override
    public PluralField addEmail(String address, PluralField.TypeEnum type, boolean isPrimary)
    {
        // ensure only one primary email address
        if (primaryEmail != null && isPrimary)
        {
            throw new IllegalArgumentException("Primary email already registered");
        }
        
        // record email
        if (emails == null)
        {
            emails = new HashSet<MongoPluralFieldData>();
        }
        MongoPluralFieldData email = new MongoPluralFieldData();
        email.setValue(address);
        email.setType(type);
        email.setPrimary(isPrimary);
        emails.add(email);

        // record primary email
        if (isPrimary)
        {
            primaryEmail = email;
        }
        
        return email;
    }
    
    @Override
    public void removeEmail(PluralField email)
    {
        if (emails != null)
        {
            boolean exists = emails.remove(email);
            if (exists && email.isPrimary())
            {
                primaryEmail = null;
            }
        }
    }

    @Override
    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    @Override
    public int getFollowerCount()
    {
        return followerCount;
    }

    @Override
    public int getFriendCount()
    {
        return friendCount;
    }
    
}

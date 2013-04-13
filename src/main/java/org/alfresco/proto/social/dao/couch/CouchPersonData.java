package org.alfresco.proto.social.dao.couch;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.PersonName;
import org.alfresco.proto.social.api.PluralField;
import org.alfresco.proto.social.dao.model.PersonData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class CouchPersonData implements PersonData
{
    @JsonProperty("_id")
    private String objectId;
    
    @JsonProperty("_rev")
    private String rev;
    
    private String displayName;
    
    private CouchPersonNameData name;
    
    private CouchPluralFieldData primaryEmail;
    private Set<CouchPluralFieldData> emails;
    
    private Date created;
    
    private Integer friendCount;
    
    private Integer followerCount;

    
    public CouchPersonData()
    {
    }
    
    public CouchPersonData(String objectId)
    {
        this.objectId = objectId;
    }
    
    @Override
    public Identifier getId()
    {
        // TODO: is it required to create everytime
        return CouchIdentifierData.create(objectId);
    }
    
    public String getRev()
    {
        return rev;
    }
    
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
            name = new CouchPersonNameData();
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
            emails = new HashSet<CouchPluralFieldData>();
        }
        CouchPluralFieldData email = new CouchPluralFieldData();
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
        return followerCount == null ? 0 : followerCount;
    }

    @Override
    public int getFriendCount()
    {
        return friendCount == null ? 0 : friendCount;
    }
    
}

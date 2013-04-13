package org.alfresco.proto.social.dao.mongo;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value="activities", noClassnameStored=true)
public class MongoActivityData implements ActivityData
{
    protected @Id ObjectId objectId;
    //@Transient protected MongoIdentifierData id;
    
    protected String appId;

    protected String type;
    
    protected Date postedTime;

    protected ObjectId originatorPersonObjectId;

    protected ObjectId personObjectId;

    protected String title;
    
    protected String body;

    @Embedded
    public Map<String, String> templateParams;

    @Embedded
    public Map<String, ObjectId> templateRefParams;
    
    @Override
    public Identifier getId()
    {
        return MongoIdentifierData.create(this.objectId);
    }
    
    @Override
    public String getAppId()
    {
        return appId;
    }

    @Override
    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public Date getPostedTime()
    {
        return postedTime;
    }

    public void setPostedTime(Date postedTime)
    {
        this.postedTime = postedTime;
    }
    
    @Override
    public Identifier getOriginatorPersonId()
    {
        return MongoIdentifierData.create(this.originatorPersonObjectId);
    }
    
    @Override
    public void setOriginatorPersonId(Identifier originatorPersonId)
    {
        this.originatorPersonObjectId = MongoIdentifierData.toObjectId(originatorPersonId);
    }

    @Override
    public Identifier getPersonId()
    {
        return MongoIdentifierData.create(personObjectId);
    }
    
    @Override
    public void setPersonId(Identifier personId)
    {
        this.personObjectId = MongoIdentifierData.toObjectId(personId);
    }
    
    @Override
    public String getTitle()
    {
        return title;
    }
    
    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    @Override
    public String getBody()
    {
        return body;
    }

    @Override
    public void setBody(String body)
    {
        this.body = body;
    }

    @Override
    public Map<String, Object> getTemplateParams()
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (templateParams != null)
        {
            params.putAll(templateParams);
        }
        if (templateRefParams != null)
        {
            for (Entry<String, ObjectId> entry : templateRefParams.entrySet())
            {
                params.put(entry.getKey(), MongoIdentifierData.create(entry.getValue()));
            }
        }
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Map<String, Object> removeTemplateParam(String key)
    {
        // TODO: implement appropriately
        templateParams.remove(key);
        templateRefParams.remove(key);
        return getTemplateParams();
    }

    @Override
    public Map<String, Object> addTemplateParam(String key, Person person)
    {
        // TODO: check for uniqueness of key across both template maps
        if (templateRefParams == null)
        {
            templateRefParams = new HashMap<String, ObjectId>();
        }
        templateRefParams.put(key, MongoIdentifierData.toObjectId(person.getId()));
        return getTemplateParams();
    }

    @Override
    public Map<String, Object> addTemplateParam(String key, String value)
    {
        // TODO: check for uniqueness of key across both template maps
        if (templateParams == null)
        {
            templateParams = new HashMap<String, String>();
        }
        templateParams.put(key, value);
        return getTemplateParams();
    }
}

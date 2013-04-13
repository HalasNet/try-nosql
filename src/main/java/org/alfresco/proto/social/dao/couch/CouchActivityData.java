package org.alfresco.proto.social.dao.couch;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class CouchActivityData implements ActivityData
{
    @JsonProperty("_id")
    protected String objectId;
    
    @JsonProperty("_rev")
    protected String rev;
    
    protected String appId;

    protected String type;
    
    protected Date postedTime;

    protected String originatorPersonObjectId;

    protected String personObjectId;

    protected String title;
    
    protected String body;

    public Map<String, String> templateParams;

    public Map<String, String> templateRefParams;
    
    @Override
    public Identifier getId()
    {
        return CouchIdentifierData.create(objectId);
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
        return CouchIdentifierData.create(originatorPersonObjectId);
    }
    
    @Override
    public void setOriginatorPersonId(Identifier originatorPersonId)
    {
        this.originatorPersonObjectId = originatorPersonId.getId();
    }

    @Override
    public Identifier getPersonId()
    {
        return CouchIdentifierData.create(personObjectId);
    }
    
    @Override
    public void setPersonId(Identifier personId)
    {
        this.personObjectId = personId.getId();
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
            for (Entry<String, String> entry : templateRefParams.entrySet())
            {
                params.put(entry.getKey(), CouchIdentifierData.create(entry.getValue()));
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
            templateRefParams = new HashMap<String, String>();
        }
        templateRefParams.put(key, person.getId().getId());
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

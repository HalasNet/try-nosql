package org.alfresco.proto.social.api;

import java.util.Date;
import java.util.Map;

public interface Activity
{
    public Identifier getId();

    public String getAppId();

    public void setAppId(String appId);

    public String getType();

    public void setType(String type);

    public Date getPostedTime();

    public Identifier getOriginatorPersonId();

    public void setOriginatorPersonId(Identifier originatorPersonId);

    public Identifier getPersonId();

    public void setPersonId(Identifier personId);

    public String getTitle();

    public void setTitle(String title);

    public String getBody();

    public void setBody(String body);

    public Map<String, Object> getTemplateParams();
    
    public Map<String, Object> addTemplateParam(String key, String value);
    
    public Map<String, Object> addTemplateParam(String key, Person person);

    public Map<String, Object> removeTemplateParam(String key);
}

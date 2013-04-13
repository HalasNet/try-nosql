package org.alfresco.proto.social.api;

import java.util.List;

public interface ActivityService
{
    public Activity newActivity(Person fromPerson);
    
    public Activity postActivity(Activity activity);
    
    public List<Activity> getActivities(Person forPerson);
}

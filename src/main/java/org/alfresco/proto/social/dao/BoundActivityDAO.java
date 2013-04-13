package org.alfresco.proto.social.dao;

import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;

public interface BoundActivityDAO
{
    public ActivityData newActivityData();

    public BoundActivityData newBoundActivityData();

    public String toJSON(ActivityData activityData);
    
    public ActivityData toActivityData(String json);
    
    public void save(BoundActivityData activityData);
    
    public BoundActivityData get(Identifier activityData);
    
    // TODO: cursor based query results (or paging)
    public List<BoundActivityData> findActivities(IdentifierData forPerson);
}

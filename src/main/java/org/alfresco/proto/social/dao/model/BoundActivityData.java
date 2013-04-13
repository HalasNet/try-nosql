package org.alfresco.proto.social.dao.model;

import org.alfresco.proto.social.api.Identifier;

public interface BoundActivityData extends ActivityData
{
    public Identifier getForPersonId();
    
    public void setForPersonId(Identifier forPersonId);
}

package org.alfresco.proto.social.dao.model;

import java.util.Date;

import org.alfresco.proto.social.api.Activity;

public interface ActivityData extends Activity
{
    public void setPostedTime(Date postedTime);
}

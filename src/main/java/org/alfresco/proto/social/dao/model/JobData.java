package org.alfresco.proto.social.dao.model;

import java.util.Date;

public interface JobData
{
    public String getJobId();
    
    public Date getLastExecuted();
    
    public void setLastExecuted(Date lastExecuted);
}

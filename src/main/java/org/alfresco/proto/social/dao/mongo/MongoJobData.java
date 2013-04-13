package org.alfresco.proto.social.dao.mongo;

import java.util.Date;

import org.alfresco.proto.social.dao.model.JobData;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value="jobs", noClassnameStored=true)
public class MongoJobData implements JobData
{
    private @Id String jobId;

    private Date lastExecuted;

    public MongoJobData()
    {
    }
    
    public MongoJobData(String jobId)
    {
        this.jobId = jobId;
    }
    
    public String getJobId()
    {
        return jobId;
    }
    
    public Date getLastExecuted()
    {
        return lastExecuted;
    }
    
    public void setLastExecuted(Date lastExecuted)
    {
        this.lastExecuted = lastExecuted;
    }
}

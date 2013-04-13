package org.alfresco.proto.social.dao;

import org.alfresco.proto.social.dao.model.JobData;


public interface JobDAO
{
    public JobData newJobData(String jobId);

    public String toJSON(JobData jobData);
    
    public JobData toJobData(String json);
    
    public void save(JobData job);
    
    public JobData get(String job);
}

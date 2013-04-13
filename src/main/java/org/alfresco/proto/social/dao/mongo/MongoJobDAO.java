package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.dao.JobDAO;
import org.alfresco.proto.social.dao.model.JobData;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoJobDAO implements JobDAO
{
    private BasicDAO<MongoJobData, String> dao;
    private Morphia ma;
    
    private static class MongoBasicDAO extends BasicDAO<MongoJobData, String>
    {
        public MongoBasicDAO(Mongo mongo, Morphia morphia, String dbName)
        {
            super(mongo, morphia, dbName);
        }
    }
    
    public MongoJobDAO(Mongo mongo, Morphia morphia, String dbName)
    {
        dao = new MongoBasicDAO(mongo, morphia, dbName);
        ma = morphia;
    }
    
    public BasicDAO<MongoJobData, String> getMongoDAO()
    {
        return dao;
    }

    @Override
    public JobData newJobData(String jobId)
    {
        return new MongoJobData(jobId);
    }
    
    @Override
    public JobData get(String job)
    {
        return dao.get(job);
    }

    @Override
    public void save(JobData job)
    {
        dao.save((MongoJobData)job);
    }

    @Override
    public JobData toJobData(String json)
    {
        DBObject dbObject = (DBObject)JSON.parse(json);
        return ma.fromDBObject(MongoJobData.class, dbObject);
    }

    @Override
    public String toJSON(JobData jobData)
    {
        DBObject o = ma.toDBObject((MongoJobData)jobData);
        return JSON.serialize(o);
    }
    
}

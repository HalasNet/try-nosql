package org.alfresco.proto.social.dao.mongo;

import java.util.Date;

import org.alfresco.proto.social.dao.model.JobData;
import org.alfresco.proto.social.dao.model.PersonData;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.MapReduceCommand.OutputType;

public class MongoRelationshipCountJob
{
    final String RELCOUNT_MAP = "function () {emit(this.follower._id, {friends:1, followers:0}); emit(this.friend._id, {friends:0, followers:1});}";
    final String RELCOUNT_REDUCE = "function (k, vals) {var sum = 0, sum2 = 0; for (var i in vals) {sum += vals[i].friends; sum2 += vals[i].followers} return {friends : sum, followers: sum2} ;}";
    
    private static String ID = MongoRelationshipCountJob.class.getName();
    
    private MongoJobDAO jobDAO;
    private MongoRelationshipDAO relationshipDAO;
    private MongoPersonDAO personDAO;


    public MongoRelationshipCountJob(MongoJobDAO jobDAO, MongoRelationshipDAO relationshipDAO, MongoPersonDAO personDAO)
    {
        this.jobDAO = jobDAO;
        this.relationshipDAO = relationshipDAO;
        this.personDAO = personDAO;
    }

    public void updateFriendCounts()
    {
        JobData job = jobDAO.get(ID);
        if (job == null)
        {
            job = jobDAO.newJobData(ID);
        }
        
        MapReduceOutput output = calculateRelationshipCounts(job.getLastExecuted());
        job.setLastExecuted(new Date());
        jobDAO.save(job);
        
        System.out.println(Thread.currentThread().getName() + ": " + output);
        long start = System.currentTimeMillis();
        int i = 0;
        for (DBObject count : output.results())
        {
            PersonData person = personDAO.newPersonData(MongoIdentifierData.create((ObjectId)count.get("_id")));
            DBObject value = (DBObject)count.get("value");
            Double friends = (Double)value.get("friends");
            Double followers = (Double)value.get("followers");
            personDAO.setRelationshipCounts(person, friends.intValue(), followers.intValue());
            i++;
        }
        System.out.println(Thread.currentThread().getName() + " Relationship counts updated for " + i + " people in " + (System.currentTimeMillis() - start) + "ms");
    }
    
    private MapReduceOutput calculateRelationshipCounts(Date friendsSince)
    {
        DBObject query = null;
        if (friendsSince != null)
        {
            query = new BasicDBObject();
            query.put("created", new BasicDBObject("$gte", friendsSince));
        }
        
        DBCollection relationships = relationshipDAO.getMongoDAO().getCollection();
        return relationships.mapReduce(RELCOUNT_MAP, RELCOUNT_REDUCE, "relationshipcounts", OutputType.REPLACE, query);
    }
    
}

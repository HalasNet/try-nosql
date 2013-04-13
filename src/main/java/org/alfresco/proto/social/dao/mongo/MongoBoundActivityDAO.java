package org.alfresco.proto.social.dao.mongo;

import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.BoundActivityDAO;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoBoundActivityDAO implements BoundActivityDAO
{
    private BasicDAO<MongoBoundActivityData, ObjectId> dao;
    private Morphia ma;
    
    private static class MongoBasicDAO extends BasicDAO<MongoBoundActivityData, ObjectId>
    {
        public MongoBasicDAO(Mongo mongo, Morphia morphia, String dbName)
        {
            super(mongo, morphia, dbName);
        }
    }
    
    public MongoBoundActivityDAO(Mongo mongo, Morphia morphia, String dbName)
    {
        dao = new MongoBasicDAO(mongo, morphia, dbName);
        ma = morphia;
    }
    
    public BasicDAO<MongoBoundActivityData, ObjectId> getMongoDAO()
    {
        return dao;
    }
    
    @Override
    public ActivityData newActivityData()
    {
        return new MongoActivityData();
    }

    @Override
    public BoundActivityData newBoundActivityData()
    {
        return new MongoBoundActivityData();
    }

    @Override
    public ActivityData toActivityData(String json)
    {
        DBObject dbObject = (DBObject)JSON.parse(json);
        return ma.fromDBObject(MongoActivityData.class, dbObject);
    }

    @Override
    public String toJSON(ActivityData activityData)
    {
        DBObject o = ma.toDBObject((MongoActivityData)activityData);
        return JSON.serialize(o);
    }
    
    @Override
    public BoundActivityData get(Identifier activityData)
    {
        return dao.get(MongoIdentifierData.toObjectId(activityData));
    }

    @Override
    public void save(BoundActivityData activityData)
    {
        dao.save((MongoBoundActivityData)activityData);
    }
    
    // TODO: cursor based query results (or paging)
    @SuppressWarnings("unchecked")
    @Override
    public List<BoundActivityData> findActivities(IdentifierData forPerson)
    {
        return (List)dao.getDatastore().find(MongoBoundActivityData.class, "forPersonObjectId", MongoIdentifierData.toObjectId(forPerson)).asList();
    }

}

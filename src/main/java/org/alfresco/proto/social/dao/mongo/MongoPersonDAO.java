package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.PersonDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.PersonData;
import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoPersonDAO implements PersonDAO
{
    private BasicDAO<MongoPersonData, ObjectId> dao;
    private Morphia ma;
    
    private static class MongoBasicDAO extends BasicDAO<MongoPersonData, ObjectId>
    {
        public MongoBasicDAO(Mongo mongo, Morphia morphia, String dbName)
        {
            super(mongo, morphia, dbName);
        }
    }
    
    public MongoPersonDAO(Mongo mongo, Morphia morphia, String dbName)
    {
        dao = new MongoBasicDAO(mongo, morphia, dbName);
        ma = morphia;
    }
    
    public BasicDAO<MongoPersonData, ObjectId> getMongoDAO()
    {
        return dao;
    }
    
    @Override
    public PersonData newPersonData()
    {
        return new MongoPersonData();
    }

    @Override
    public PersonData toPersonData(String json)
    {
        DBObject dbObject = (DBObject)JSON.parse(json);
        return ma.fromDBObject(MongoPersonData.class, dbObject);
    }

    @Override
    public String toJSON(PersonData personData)
    {
        DBObject o = ma.toDBObject((MongoPersonData)personData);
        return JSON.serialize(o);
    }
    
    @Override
    public PersonData get(Identifier person)
    {
        return dao.get((MongoIdentifierData.toObjectId(person)));
    }

    @Override
    public void save(PersonData person)
    {
        dao.save((MongoPersonData)person);
    }
    
    @Override
    public PersonData newPersonData(IdentifierData person)
    {
        return new MongoPersonData(MongoIdentifierData.toObjectId(person));
    }
    
    @Override
    public PersonData findByEmail(String email)
    {
        return dao.getDatastore().find(MongoPersonData.class, "primaryEmail.value", email).get();
    }
    
    // used for mapreduce approach to updating counts
    @Override
    public void setRelationshipCounts(PersonData person, int friendCount, int followerCount)
    {
        UpdateOperations<MongoPersonData> updOps = dao.createUpdateOperations().set("friendCount", friendCount).set("followerCount", followerCount);
        dao.getDatastore().update((MongoPersonData)person, updOps);
    }
    
    @Override
    public void incrementFriendCount(PersonData person)
    {
        UpdateOperations<MongoPersonData> updOps = dao.createUpdateOperations().inc("friendCount");
        dao.getDatastore().update((MongoPersonData)person, updOps);
    }

    @Override
    public void incrementFollowerCount(PersonData person)
    {
        UpdateOperations<MongoPersonData> updOps = dao.createUpdateOperations().inc("followerCount");
        dao.getDatastore().update((MongoPersonData)person, updOps);
    }

}

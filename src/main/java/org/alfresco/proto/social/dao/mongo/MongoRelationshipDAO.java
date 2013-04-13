package org.alfresco.proto.social.dao.mongo;

import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.RelationshipDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoRelationshipDAO implements RelationshipDAO
{

    private BasicDAO<MongoRelationshipData, ObjectId> dao;
    private Morphia ma;
    
    private static class MongoBasicDAO extends BasicDAO<MongoRelationshipData, ObjectId>
    {
        public MongoBasicDAO(Mongo mongo, Morphia morphia, String dbName)
        {
            super(mongo, morphia, dbName);
        }
    }
    
    public MongoRelationshipDAO(Mongo mongo, Morphia morphia, String dbName)
    {
        dao = new MongoBasicDAO(mongo, morphia, dbName);
        ma = morphia;
    }

    public BasicDAO<MongoRelationshipData, ObjectId> getMongoDAO()
    {
        return dao;
    }
    
    @Override
    public RelationshipData newRelationshipData()
    {
        return new MongoRelationshipData();
    }

    @Override
    public RelationshipData toRelationshipData(String json)
    {
        DBObject dbObject = (DBObject)JSON.parse(json);
        return ma.fromDBObject(MongoRelationshipData.class, dbObject);
    }

    @Override
    public String toJSON(RelationshipData relationshipData)
    {
        DBObject o = ma.toDBObject((MongoRelationshipData)relationshipData);
        return JSON.serialize(o);
    }
    
    @Override
    public RelationshipData get(Identifier relationship)
    {
        return dao.get(MongoIdentifierData.toObjectId(relationship));
    }

    @Override
    public void save(RelationshipData relationship)
    {
        dao.save((MongoRelationshipData)relationship);
    }
    
    // TODO: cursor based query results (or paging)
    @SuppressWarnings("unchecked")
    @Override
    public List<RelationshipData> findFriends(IdentifierData person)
    {
        return (List)dao.getDatastore().find(MongoRelationshipData.class, "follower.objectId", MongoIdentifierData.toObjectId(person)).asList();
    }
    
    // TODO: cursor based query results (or paging)
    @SuppressWarnings("unchecked")
    @Override
    public List<RelationshipData> findFollowers(IdentifierData person)
    {
        return (List)dao.getDatastore().find(MongoRelationshipData.class, "friend.objectId", MongoIdentifierData.toObjectId(person)).asList();
    }
}

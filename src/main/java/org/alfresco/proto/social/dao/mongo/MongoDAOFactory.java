package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class MongoDAOFactory implements DAOFactory
{
    private static String DB = "social";
    
    private String dbName;
    private Mongo mongo;
    private Morphia morphia;
    private MongoPersonDAO personDAO;
    private MongoRelationshipDAO relationshipDAO;
    private MongoBoundActivityDAO activityDAO;
    private MongoJobDAO jobDAO;

    public MongoDAOFactory() throws Exception
    {
        this(DB);
    }
    
    public MongoDAOFactory(String dbName) throws Exception
    {
        this.dbName = dbName;
        init();
    }
    
    private void init() throws Exception
    {
        mongo = new Mongo();
        morphia = new Morphia();
        personDAO = new MongoPersonDAO(mongo, morphia, dbName);
        personDAO.getMongoDAO().getCollection().drop();
        personDAO.getMongoDAO().ensureIndexes();
        relationshipDAO = new MongoRelationshipDAO(mongo, morphia, dbName);
        relationshipDAO.getMongoDAO().getCollection().drop();
        relationshipDAO.getMongoDAO().ensureIndexes();
        activityDAO = new MongoBoundActivityDAO(mongo, morphia, dbName);
        activityDAO.getMongoDAO().getCollection().drop();
        activityDAO.getMongoDAO().ensureIndexes();
        jobDAO = new MongoJobDAO(mongo, morphia, dbName);
        jobDAO.getMongoDAO().getCollection().drop();
        jobDAO.getMongoDAO().ensureIndexes();
        
        morphia.map(MongoActivityData.class);
    }

    @Override
    public MongoPersonDAO getPersonDAO()
    {
        return personDAO;
    }
    
    @Override
    public MongoRelationshipDAO getRelationshipDAO()
    {
        return relationshipDAO;
    }
    
    @Override
    public MongoBoundActivityDAO getActivityDAO()
    {
        return activityDAO;
    }
    
    @Override
    public MongoJobDAO getJobDAO()
    {
        return jobDAO;
    }
    
    public Morphia getMorphia()
    {
        return morphia;
    }
    
    public Mongo getMongo()
    {
        return mongo;
    }

    @Override
    public IdentifierData createIdentifier()
    {
        return MongoIdentifierData.create(ObjectId.get());
    }
}

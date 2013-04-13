package org.alfresco.proto.social.dao.couch;

import java.util.List;

import org.alfresco.proto.social.dao.BoundActivityDAO;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.JobDAO;
import org.alfresco.proto.social.dao.RelationshipDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.bson.types.ObjectId;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDAOFactory implements DAOFactory
{
    private CouchDbInstance db;
    private CouchPersonDAO personDAO;
    private CouchRelationshipDAO relationshipDAO;
    private CouchBoundActivityDAO activityDAO;
    

    public CouchDAOFactory() throws Exception
    {
        init();
    }
    
    private void deleteDatabase(String name)
    {
        List<String> dbs = db.getAllDatabases();
        if (dbs.contains(name))
        {
            db.deleteDatabase(name);
        }
    }
    
    private void init() throws Exception
    {
        HttpClient httpClient = new StdHttpClient.Builder().build();
        db = new StdCouchDbInstance(httpClient);
        
        deleteDatabase(CouchPersonDAO.DATABASE);
        personDAO = new CouchPersonDAO(db);

        deleteDatabase(CouchRelationshipDAO.DATABASE);
        relationshipDAO = new CouchRelationshipDAO(db);

        deleteDatabase(CouchBoundActivityDAO.DATABASE);
        activityDAO = new CouchBoundActivityDAO(db);

//        jobDAO = new MongoJobDAO(mongo, morphia, dbName);
//        jobDAO.getMongoDAO().getCollection().drop();
//        jobDAO.getMongoDAO().ensureIndexes();
        
//        morphia.map(MongoActivityData.class);
    }

    @Override
    public CouchPersonDAO getPersonDAO()
    {
        return personDAO;
    }
    
    @Override
    public RelationshipDAO getRelationshipDAO()
    {
        return relationshipDAO;
    }
    
    @Override
    public BoundActivityDAO getActivityDAO()
    {
        // TODO:
        return activityDAO;
    }
    
    @Override
    public JobDAO getJobDAO()
    {
        // TODO:
        return null;
    }
    
    @Override
    public IdentifierData createIdentifier()
    {
        return CouchIdentifierData.create(ObjectId.get().toString());
    }
}

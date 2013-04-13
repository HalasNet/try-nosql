package org.alfresco.proto.social.dao.couch;

import java.io.IOException;
import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.BoundActivityDAO;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.util.Documents;

public class CouchBoundActivityDAO implements BoundActivityDAO
{
    public static String DATABASE = "activities";
    
    private CouchDbConnector conn;
    private DataRepository dao;
    private ObjectMapper m = new ObjectMapper();

    
    private static class DataRepository extends CouchDbRepositorySupport<CouchBoundActivityData>
    {
        public DataRepository(CouchDbConnector conn)
        {
            super(CouchBoundActivityData.class, conn);
            initStandardDesignDocument();
        }

        @View(name="my_activities", map="function(doc) {if(doc.forPersonObjectId) {emit(doc.forPersonObjectId, doc._id)}}")
        public List<CouchBoundActivityData> findActivities(IdentifierData person)
        {
            return queryView("my_activities", person.getId());
        }
    }
    
    public CouchBoundActivityDAO(CouchDbInstance db)
    {
        conn = db.createConnector(DATABASE, true);
        dao = new DataRepository(conn);
    }
    
    public CouchDbRepositorySupport<CouchBoundActivityData> getRepositorySupport()
    {
        return dao;
    }
    
    @Override
    public ActivityData newActivityData()
    {
        return new CouchActivityData();
    }

    @Override
    public BoundActivityData newBoundActivityData()
    {
        return new CouchBoundActivityData();
    }

    @Override
    public ActivityData toActivityData(String json)
    {
        try
        {
            return m.readValue(json, CouchActivityData.class);
        }
        catch (JsonParseException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (JsonMappingException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toJSON(ActivityData activityData)
    {
        try
        {
            return m.writeValueAsString((CouchActivityData)activityData);
        }
        catch (JsonParseException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (JsonMappingException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public BoundActivityData get(Identifier activityData)
    {
        String id = ((CouchIdentifierData)activityData).getId();
        String rev = ((CouchIdentifierData)activityData).getRev();
        try
        {
            if (rev == null)
            {
                return dao.get(id);
            }
            else
            {
                return dao.get(id, rev);
            }
        }
        catch(DocumentNotFoundException e)
        {
            return null;
        }
    }

    @Override
    public void save(BoundActivityData activityData)
    {
        CouchBoundActivityData data = (CouchBoundActivityData)activityData;
        if (Documents.isNew(data))
        {
            dao.add(data);
        }
        else
        {
            dao.update(data);
        }
    }
    
    // TODO: cursor based query results (or paging)
    @SuppressWarnings("unchecked")
    @Override
    public List<BoundActivityData> findActivities(IdentifierData forPerson)
    {
        return (List)dao.findActivities(forPerson);
    }

}

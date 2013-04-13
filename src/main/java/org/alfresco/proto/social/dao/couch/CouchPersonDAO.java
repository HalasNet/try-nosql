package org.alfresco.proto.social.dao.couch;

import java.io.IOException;
import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.PersonDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.PersonData;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.util.Documents;


public class CouchPersonDAO implements PersonDAO
{
    public static String DATABASE = "people";
    
    private CouchDbConnector conn;
    private DataRepository dao;
    private ObjectMapper m = new ObjectMapper();

    
    private static class DataRepository extends CouchDbRepositorySupport<CouchPersonData>
    {
        public DataRepository(CouchDbConnector conn)
        {
            super(CouchPersonData.class, conn);
            initStandardDesignDocument();
        }
        
        @View(name="by_primaryEmail", map="function(doc) {if(doc.primaryEmail.value) {emit(doc.primaryEmail.value, doc._id)}}")
        public CouchPersonData findByEmail(String email)
        {
            List<CouchPersonData> r = queryView("by_primaryEmail", email);
            return r.size() == 0 ? null : r.get(0);
        }
    }
    
    public CouchPersonDAO(CouchDbInstance db)
    {
        conn = db.createConnector(DATABASE, true);
        dao = new DataRepository(conn);
    }
    
    public CouchDbRepositorySupport<CouchPersonData> getRepositorySupport()
    {
        return dao;
    }
    
    @Override
    public PersonData newPersonData()
    {
        return new CouchPersonData();
    }

    @Override
    public PersonData toPersonData(String json)
    {
        try
        {
            return m.readValue(json, CouchPersonData.class);
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
    public String toJSON(PersonData personData)
    {
        try
        {
            return m.writeValueAsString((CouchPersonData)personData);
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
    public PersonData get(Identifier person)
    {
        String id = ((CouchIdentifierData)person).getId();
        String rev = ((CouchIdentifierData)person).getRev();
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
    public void save(PersonData person)
    {
        CouchPersonData data = (CouchPersonData)person;
        if (Documents.isNew(data))
        {
            dao.add(data);
        }
        else
        {
            dao.update(data);
        }
    }
    
    @Override
    public PersonData newPersonData(IdentifierData person)
    {
        return new CouchPersonData(person.getId());
    }
    
    @Override
    public PersonData findByEmail(String email)
    {
        try
        {
            return dao.findByEmail(email);
        }
        catch(DocumentNotFoundException e)
        {
            return null;
        }
    }
    
    // used for mapreduce approach to updating counts
    @Override
    public void setRelationshipCounts(PersonData person, int friendCount, int followerCount)
    {
        // TODO:
//        UpdateOperations<CouchPersonData> updOps = dao.createUpdateOperations().set("friendCount", friendCount).set("followerCount", followerCount);
//        dao.getDatastore().update((CouchPersonData)person, updOps);
    }
    
    @Override
    public void incrementFriendCount(PersonData person)
    {
        // TODO:
//        UpdateOperations<CouchPersonData> updOps = dao.createUpdateOperations().inc("friendCount");
//        dao.getDatastore().update((CouchPersonData)person, updOps);
    }

    @Override
    public void incrementFollowerCount(PersonData person)
    {
        // TODO:
//        UpdateOperations<CouchPersonData> updOps = dao.createUpdateOperations().inc("followerCount");
//        dao.getDatastore().update((CouchPersonData)person, updOps);
    }

}

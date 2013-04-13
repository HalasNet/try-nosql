package org.alfresco.proto.social.dao.couch;

import java.io.IOException;
import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.RelationshipDAO;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.util.Documents;

public class CouchRelationshipDAO implements RelationshipDAO
{
    public static String DATABASE = "relationships";

    private CouchDbConnector conn;
    private DataRepository dao;
    private ObjectMapper m = new ObjectMapper();

    
    private static class DataRepository extends CouchDbRepositorySupport<CouchRelationshipData>
    {
        public DataRepository(CouchDbConnector conn)
        {
            super(CouchRelationshipData.class, conn);
            initStandardDesignDocument();
        }
        
        @View(name="friends", map="function(doc) {if(doc.follower) {emit(doc.follower._id, doc._id)}}")
        public List<CouchRelationshipData> findFriends(IdentifierData person)
        {
            return queryView("friends", person.getId());
        }
        
        @View(name="followers", map="function(doc) {if(doc.friend) {emit(doc.friend._id, doc._id)}}")
        public List<CouchRelationshipData> findFollowers(IdentifierData person)
        {
            return queryView("followers", person.getId());
        }
    }
    
    public CouchRelationshipDAO(CouchDbInstance db)
    {
        conn = db.createConnector(DATABASE, true);
        dao = new DataRepository(conn);
    }
    
    public CouchDbRepositorySupport<CouchRelationshipData> getRepositorySupport()
    {
        return dao;
    }
    
    @Override
    public RelationshipData newRelationshipData()
    {
        return new CouchRelationshipData();
    }

    @Override
    public RelationshipData toRelationshipData(String json)
    {
        try
        {
            return m.readValue(json, CouchRelationshipData.class);
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
    public String toJSON(RelationshipData relationshipData)
    {
        try
        {
            return m.writeValueAsString((CouchRelationshipData)relationshipData);
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
    public RelationshipData get(Identifier relationship)
    {
        String id = ((CouchIdentifierData)relationship).getId();
        String rev = ((CouchIdentifierData)relationship).getRev();
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
    public void save(RelationshipData relationship)
    {
        CouchRelationshipData data = (CouchRelationshipData)relationship;
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
    public List<RelationshipData> findFriends(IdentifierData person)
    {
        return (List)dao.findFriends(person);
    }
    
    // TODO: cursor based query results (or paging)
    @SuppressWarnings("unchecked")
    @Override
    public List<RelationshipData> findFollowers(IdentifierData person)
    {
        return (List)dao.findFollowers(person);
    }
}

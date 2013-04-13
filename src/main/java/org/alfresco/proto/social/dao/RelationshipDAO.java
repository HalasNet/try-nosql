package org.alfresco.proto.social.dao;

import java.util.List;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.RelationshipData;

public interface RelationshipDAO
{
    public RelationshipData newRelationshipData();
    
    public String toJSON(RelationshipData relationshipData);
    
    public RelationshipData toRelationshipData(String json);
    
    public void save(RelationshipData relationship);
    
    public RelationshipData get(Identifier relationship);
    
    // TODO: cursor based query results (or paging)
    public List<RelationshipData> findFriends(IdentifierData person);
    
    // TODO: cursor based query results (or paging)
    public List<RelationshipData> findFollowers(IdentifierData person);
}

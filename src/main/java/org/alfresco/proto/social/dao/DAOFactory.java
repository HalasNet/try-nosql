package org.alfresco.proto.social.dao;

import org.alfresco.proto.social.dao.model.IdentifierData;


public interface DAOFactory
{
    public PersonDAO getPersonDAO();
    
    public RelationshipDAO getRelationshipDAO();
    
    public BoundActivityDAO getActivityDAO();
    
    public JobDAO getJobDAO();
    
    public IdentifierData createIdentifier();
}

package org.alfresco.proto.social.service;

import java.io.IOException;

import org.alfresco.proto.social.api.ActivityService;
import org.alfresco.proto.social.api.SocialService;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.job.ActivityPostJob;

public class ServiceFactory
{
    private DAOFactory dao;

    public ServiceFactory(DAOFactory daoFactory) throws Exception
    {
        dao = daoFactory;;
    }
    
    public DAOFactory getDAOFactory()
    {
        return dao;
    }
    
    public SocialService createSocialService() throws Exception
    {
        return new SocialServiceImpl(dao.getPersonDAO(), dao.getRelationshipDAO(), new ActivityServiceImpl(dao.getActivityDAO()));
    }
    
    public ActivityService createActivityService() throws Exception
    {
        return new ActivityServiceImpl(dao.getActivityDAO());
    }
    
    public ActivityPostJob createActivityPostJob() throws IOException
    {
        return new ActivityPostJob(dao.getRelationshipDAO(), dao.getActivityDAO());
    }
}

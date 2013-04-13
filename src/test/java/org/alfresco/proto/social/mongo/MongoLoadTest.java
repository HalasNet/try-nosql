package org.alfresco.proto.social.mongo;

import org.alfresco.proto.social.LoadTest;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.mongo.MongoDAOFactory;
import org.alfresco.proto.social.service.ServiceFactory;

public class MongoLoadTest extends LoadTest
{
    @Override
    protected ServiceFactory createServiceFactory() throws Exception
    {
        DAOFactory dao = new MongoDAOFactory();
        return new ServiceFactory(dao);
    }
}

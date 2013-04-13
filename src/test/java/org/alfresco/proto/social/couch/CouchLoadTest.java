package org.alfresco.proto.social.couch;

import org.alfresco.proto.social.LoadTest;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.couch.CouchDAOFactory;
import org.alfresco.proto.social.service.ServiceFactory;

public class CouchLoadTest extends LoadTest
{
    @Override
    protected ServiceFactory createServiceFactory() throws Exception
    {
        DAOFactory dao = new CouchDAOFactory();
        return new ServiceFactory(dao);
    }
}

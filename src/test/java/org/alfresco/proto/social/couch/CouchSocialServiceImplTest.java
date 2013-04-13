package org.alfresco.proto.social.couch;

import org.alfresco.proto.social.SocialServiceImplTest;
import org.alfresco.proto.social.dao.DAOFactory;
import org.alfresco.proto.social.dao.couch.CouchDAOFactory;
import org.alfresco.proto.social.service.ServiceFactory;

public class CouchSocialServiceImplTest extends SocialServiceImplTest
{
    @Override
    protected ServiceFactory createServiceFactory() throws Exception
    {
        DAOFactory dao = new CouchDAOFactory();
        return new ServiceFactory(dao);
    }
}

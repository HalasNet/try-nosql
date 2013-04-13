package org.alfresco.proto.social.couch;

import org.alfresco.proto.social.DAOTest;
import org.alfresco.proto.social.dao.couch.CouchDAOFactory;
import org.junit.Before;

public class CouchDAOTest extends DAOTest
{
    @Before public void setUp() throws Exception
    {
        dao = new CouchDAOFactory();
    }
}

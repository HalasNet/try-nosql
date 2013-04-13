package org.alfresco.proto.social.mongo;

import org.alfresco.proto.social.DAOTest;
import org.alfresco.proto.social.dao.mongo.MongoDAOFactory;
import org.junit.Before;

public class MongoDAOTest extends DAOTest
{
    @Before public void setUp() throws Exception
    {
        dao = new MongoDAOFactory();
    }
}

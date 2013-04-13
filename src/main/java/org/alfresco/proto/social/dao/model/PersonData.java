package org.alfresco.proto.social.dao.model;

import java.util.Date;

import org.alfresco.proto.social.api.Person;


public interface PersonData extends Person
{
    public void setCreated(Date created);
}

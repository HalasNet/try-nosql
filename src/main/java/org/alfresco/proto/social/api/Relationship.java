package org.alfresco.proto.social.api;

import java.util.Date;

public interface Relationship
{
    public Identifier getId();

    public Person getFollower();

    public Person getFriend();

    public Date getCreated();
}

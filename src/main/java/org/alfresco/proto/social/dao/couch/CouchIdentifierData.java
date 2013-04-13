package org.alfresco.proto.social.dao.couch;

import org.alfresco.proto.social.dao.model.IdentifierData;

public class CouchIdentifierData implements IdentifierData
{
    private String id;
    private String rev;
    
    public CouchIdentifierData(String id)
    {
        this(id, null);
    }

    public CouchIdentifierData(String id, String rev)
    {
        this.id = id;
        this.rev = rev;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getRev()
    {
        return rev;
    }
    
    @Override
    public String toString()
    {
        return "IdentifierData[_id=" + getId() + ",_rev=" + (rev == null ? "<null>" : rev) + "]";
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof CouchIdentifierData && id.equals(((CouchIdentifierData)obj).getId());
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    public static CouchIdentifierData create(String id)
    {
        return id == null ? null : new CouchIdentifierData(id);
    }
    
    public static CouchIdentifierData create(String id, String rev)
    {
        return id == null ? null : new CouchIdentifierData(id, rev);
    }
}

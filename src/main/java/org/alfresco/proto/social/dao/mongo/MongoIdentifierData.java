package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.bson.types.ObjectId;

public class MongoIdentifierData implements IdentifierData
{
    private ObjectId id;
    
    public MongoIdentifierData(ObjectId id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id.toString();
    }
    
    public ObjectId getObjectId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "IdentifierData[ObjectId=" + getId() + "]";
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof MongoIdentifierData && id.equals(((MongoIdentifierData)obj).getObjectId());
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    public static MongoIdentifierData create(ObjectId id)
    {
        return id == null ? null : new MongoIdentifierData(id);
    }
    
    public static ObjectId toObjectId(Identifier identifier)
    {
        return identifier == null ? null : new ObjectId(identifier.getId());
    }
}

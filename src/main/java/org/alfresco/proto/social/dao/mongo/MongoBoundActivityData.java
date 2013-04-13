package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

@Entity(value="activities", noClassnameStored=true)
@Indexes(@Index(value="forPersonObjectId"))
public class MongoBoundActivityData extends MongoActivityData implements BoundActivityData
{
    private ObjectId forPersonObjectId;

    @Override
    public Identifier getForPersonId()
    {
        return MongoIdentifierData.create(forPersonObjectId);
    }

    @Override
    public void setForPersonId(Identifier forPersonId)
    {
        forPersonObjectId = MongoIdentifierData.toObjectId(forPersonId);
    }
}

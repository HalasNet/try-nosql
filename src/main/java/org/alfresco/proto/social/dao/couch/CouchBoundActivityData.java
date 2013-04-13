package org.alfresco.proto.social.dao.couch;

import org.alfresco.proto.social.api.Identifier;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class CouchBoundActivityData extends CouchActivityData implements BoundActivityData
{
    private String forPersonObjectId;


    @Override
    public Identifier getForPersonId()
    {
        return CouchIdentifierData.create(forPersonObjectId);
    }

    @Override
    public void setForPersonId(Identifier forPersonId)
    {
        forPersonObjectId = forPersonId.getId();
    }
}

package org.alfresco.proto.social.dao.couch;

import org.alfresco.proto.social.dao.model.PluralFieldData;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class CouchPluralFieldData implements PluralFieldData
{
    private String value;
    
    private TypeEnum type;
    
    private boolean primary;

    
    @Override
    public TypeEnum getType()
    {
        return type;
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public boolean isPrimary()
    {
        return primary;
    }

    @Override
    public void setPrimary(boolean isPrimary)
    {
        this.primary = isPrimary;
    }

    @Override
    public void setType(TypeEnum type)
    {
        this.type = type;
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}

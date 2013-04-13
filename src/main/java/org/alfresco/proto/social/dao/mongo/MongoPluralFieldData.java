package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.dao.model.PluralFieldData;

import com.google.code.morphia.annotations.Entity;

@Entity(noClassnameStored=true)
public class MongoPluralFieldData implements PluralFieldData
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

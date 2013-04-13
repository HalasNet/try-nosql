package org.alfresco.proto.social.api;


public interface PluralField
{
    public static enum TypeEnum { work, home,other };
        
    public String getValue();

    public void setValue(String value);
    
    public TypeEnum getType();

    public void setType(TypeEnum type);

    public boolean isPrimary();

    public void setPrimary(boolean isPrimary);
}

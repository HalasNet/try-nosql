package org.alfresco.proto.social.dao.mongo;

import org.alfresco.proto.social.dao.model.PersonNameData;

import com.google.code.morphia.annotations.Entity;

@Entity(noClassnameStored=true)
public class MongoPersonNameData implements PersonNameData
{
    public String familyName;
    
    public String formatted;
    
    public String givenName;
    
    public String honorificPrefix;
    
    public String honorificSuffix;
    
    public String middleName;

    @Override
    public String getFamilyName()
    {
        return familyName;
    }

    @Override
    public String getFormatted()
    {
        return formatted;
    }

    @Override
    public String getGivenName()
    {
        return givenName;
    }

    @Override
    public String getHonorificPrefix()
    {
        return honorificPrefix;
    }

    @Override
    public String getHonorificSuffix()
    {
        return honorificSuffix;
    }

    @Override
    public String getMiddleName()
    {
        return middleName;
    }

    @Override
    public void setFamilyName(String familyName)
    {
        this.familyName = familyName;
    }

    @Override
    public void setFormatted(String formatted)
    {
        this.formatted = formatted;
    }

    @Override
    public void setGivenName(String givenName)
    {
        this.givenName = givenName;
    }

    @Override
    public void setHonorificPrefix(String honorificPrefix)
    {
        this.honorificPrefix = honorificPrefix;
    }

    @Override
    public void setHonorificSuffix(String honorificSuffix)
    {
        this.honorificSuffix = honorificSuffix;
    }

    @Override
    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }
    
}

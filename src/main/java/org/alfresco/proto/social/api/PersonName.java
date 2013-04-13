package org.alfresco.proto.social.api;


public interface PersonName
{
    public String getFamilyName();

    public void setFamilyName(String familyName);

    public String getFormatted();

    public void setFormatted(String formatted);

    public String getGivenName();

    public void setGivenName(String givenName);

    public String getHonorificPrefix();

    public void setHonorificPrefix(String honorificPrefix);

    public String getHonorificSuffix();

    public void setHonorificSuffix(String honorificSuffix);

    public String getMiddleName();

    public void setMiddleName(String middleName);
}

package org.alfresco.proto.social.api;

import java.util.Date;
import java.util.Set;

public interface Person
{
    public Identifier getId();
    
    public String getDisplayName();

    public void setDisplayName(String displayName);

    public PersonName getName();

    public Set<PluralField> getEmails();
    
    public PluralField addEmail(String address, PluralField.TypeEnum type, boolean isPrimary);
    
    public void removeEmail(PluralField email);

    public Date getCreated();
    
    public int getFriendCount();
    
    public int getFollowerCount();
}

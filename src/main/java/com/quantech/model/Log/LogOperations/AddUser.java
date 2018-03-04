package com.quantech.model.Log.LogOperations;

import com.quantech.config.SecurityRoles;
import com.quantech.model.Log.LoggableEvent;
import com.quantech.model.Log.OperationTypes;


import java.time.LocalDateTime;
import java.util.Set;

public class AddUser implements LoggableEvent{

    private Long id;
    private LocalDateTime date;

    private Long createdUserId;
    private Set<SecurityRoles> securityRoles;

    public AddUser(Long id, LocalDateTime date, Long createdId, Set<SecurityRoles> createdRoles)
    {
        this.id = id;

        this.date = date;
        this.createdUserId = createdId;
        this.securityRoles = createdRoles;
    }
    @Override
    public Long getOriginatingId()
    {
        return id;
    }

    @Override
    public OperationTypes getOperation() {
        return OperationTypes.AddUser;
    }

    @Override
    public LocalDateTime getTimeOfOperation() {
        return date;
    }

    public Long getCreatedUserId()
    {
        return createdUserId;
    }

    public Set<SecurityRoles> getSecurityRoles()
    {
        return securityRoles;
    }

    @Override
    public String toString() {
        String s = "User ";
        s += createdUserId.toString();
        s += " was created with roles: ";
        for (SecurityRoles r: securityRoles) { s+= r.toString(); s+=", "; }
        s=s.substring(0,s.length()-2);
        s+=".";
        return s;
    }
}

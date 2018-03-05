package com.quantech.model.Log.LogOperations;

import com.quantech.config.SecurityRoles;
import com.quantech.model.Log.LoggableEvent;
import com.quantech.model.Log.OperationTypes;
import com.quantech.model.user.Title;
import com.quantech.model.user.UserFormBackingObject;


import java.time.LocalDateTime;
import java.util.Set;

public class AddUser implements LoggableEvent{


    private Long id;
    private LocalDateTime date;

    private Long createdUserId;

    private Set<SecurityRoles> securityRoles;
    private String username;
    private String firstName;
    private String surname;
    private Title title;
    private String email;
    public AddUser(Long id, LocalDateTime date, Long createdId, Set<SecurityRoles> createdRoles, String username, String email, String firstname, String surname, Title title)
    {
        this.id = id;

        this.date = date;
        this.createdUserId = createdId;
        this.securityRoles = createdRoles;
        this.username = username;
        this.firstName = firstname;
        this.title = title;
        this.surname = surname;
        this.email = email;
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

    public boolean meetsFilter(UserFormBackingObject user)
    {
        return (securityRoles.containsAll(user.getAuthorityStrings()))
                && (username.contains(user.getUsername()))
                && (email.contains(user.getEmail()))
                && ((user.getTitle() == null) || (user.getTitle() == title))
                && (firstName.contains(user.getFirstName()))
                && (surname.contains(user.getLastName()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String authoritiesAsString()
    {
        String s = "";
        for (SecurityRoles r:securityRoles) {
            s += ", ";
            s += r.toString();
            }
        s= s.substring(2);
        s+=".";
        return s;
    }

    public String getFullName()
    {
        return title.toString() + " " + firstName + " " + surname;
    }//Code Duplication?
}

package com.quantech.model.user;

import com.quantech.config.SecurityRoles;

import java.util.Set;

public interface UserInfo {

    Title getTitle();
    void setTitle(Title t);

    String getFirstName();
    void setFirstName(String firstName);

    String getLastName();
    void setLastName(String lastName);

    String getEmail();
    void setEmail(String email);

    String getUsername();
    void setUsername(String username);

    String getPassword();
    void setPassword(String password);

    void addAuth(SecurityRoles auth);
    void removeAuth(SecurityRoles auth) ;
    boolean hasAuth(SecurityRoles auth) ;

    boolean getEnabled();
    void setEnabled(boolean enabled);

    Long getId();

    Set<SecurityRoles> getAuthorityStrings();
    void setAuthorityStrings(Set<SecurityRoles> authorityStrings);
}

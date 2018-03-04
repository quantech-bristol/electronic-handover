package com.quantech.model.user;

import com.quantech.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.util.Collection;

public class UserEntry implements UserDetails
{
    @NotNull
    private final long id;

    private UserService userService;

    public UserEntry(Long id, UserService service)
    {
        this.id = id;
        this.userService = service;
    }

    public UserCore getUserCore()
    {
        return userService.findUserById(id);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return userService.findUserById(id).getAuthorities();
    }

    @Override
    public String getPassword() {
       return userService.findUserById(id).getPassword();
    }

    @Override
    public String getUsername() {
      return  userService.findUserById(id).getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
       return userService.findUserById(id).isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
       return userService.findUserById(id).isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userService.findUserById(id).isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userService.findUserById(id).isEnabled();
    }
}

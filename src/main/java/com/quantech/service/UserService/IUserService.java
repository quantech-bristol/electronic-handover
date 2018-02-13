package com.quantech.service.UserService;

import com.quantech.config.SecurityRoles;
import com.quantech.model.user.Title;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.model.user.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public interface IUserService
{
    public void insertRootUser();
    public void delUser(String user) ;

    public void editPassword(String user, String newPass);
    public boolean saveUser(UserCore user);

    public List<UserCore> getAllUsers() ;

    public UserCore findUserById(long id) ;

    public UserCore findUserByUsername(String username);

    public boolean nameIsValid(String s, Long validForUserId);

    public boolean emailIsValid(String s, Long validForUserId);

    public void CheckValidity(BindingResult result, boolean creating, UserInfo ob);
}

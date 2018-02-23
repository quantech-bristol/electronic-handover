package com.quantech.service.UserService;

import com.quantech.config.SecurityRoles;
import com.quantech.model.Doctor;
import com.quantech.model.user.Title;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.model.user.UserInfo;
import com.quantech.repo.UserRepository;
import com.quantech.service.DoctorService.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.*;
import java.util.function.Predicate;

@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorService doctorService;

    @Autowired
    public UserServiceImpl() {

    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserCore createUser(UserFormBackingObject user)
    {
        UserCore newUser = user.ToUserCore();
        saveUser(newUser, true);
        if (newUser.hasAuth(SecurityRoles.Doctor)) {
            Doctor newDoc = new Doctor(newUser);
            doctorService.saveDoctor(newDoc);
        }
        return newUser;
    }
    @Override
    @PostConstruct
    public void insertRootUser()
    {

        if ((!activeProfile.matches("test")) && (userRepository.count() == 0))
        {

            userRepository.save(new UserCore("quantech",passwordEncoder.encode("quantech"), SecurityRoles.Admin, Title.Mx, "quan", "tech", "quantech@gmail.com"));
        }
    }
    @Override
    public boolean checkUserPassword (UserCore user, String password)
    {
        return passwordEncoder.matches(password,user.getPassword());
    }
    @Override
    public void deleteUser(String user) {
        userRepository.deleteUserCoreByUsername(user);
        insertRootUser();
    }

    @Override
    public void editPassword(String user, String newPass)
    {
        UserCore userToEdit = userRepository.findUserCoreByUsername(user);
        userToEdit.setPassword(passwordEncoder.encode((newPass)));
        userRepository.save(userToEdit);

    }

    //TODO add sanity checks
    @Override
    public boolean saveUser(UserCore user, boolean hashPassword) {
        if (user.getUsername() != "quantech") {
            if(hashPassword){user.setPassword(passwordEncoder.encode(user.getPassword()));}
            userRepository.save(user);
            return false;
        }
        return true;
    }


    @Override
    public List<UserCore> getAllUsers() {
        List<UserCore> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public List<UserCore> getAllDoctorUsers() {
        List<UserCore> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        List<UserCore> doctorUsers = new ArrayList<>();
        for (UserCore user : allUsers) {
            if (user.isDoctor()) doctorUsers.add(user);
        }
        return doctorUsers;
    }

    @Override
    public UserCore findUserById(long id) {
        UserCore newUser = userRepository.getUserCoreByIdEquals(id);
        return newUser;
    }

    @Override
    public UserCore findUserByUsername(String username) {
        UserCore newUser = userRepository.findUserCoreByUsername(username);
        return newUser;
    }

    //Enables userService to authenticate its users for the Security config.
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserCore newUser = userRepository.findUserCoreByUsername(s);
        if (newUser==null) {
            throw new UsernameNotFoundException(s);
        }
        return newUser;
    }

    private UserCore rootUser() {

        UserCore rootUser = new UserCore("quantech","quantech", SecurityRoles.Admin, Title.Mx, "quan", "tech", "quantech@gmail.com");
        return rootUser;
    }

    @Override
    public boolean nameIsValid(String s, Long id)
    {
        UserCore user = userRepository.findUserCoreByUsername(s);
        if ((user == null)||(user.getId() == id)){return true;}
        return false;
    }

    @Override
    public boolean emailIsValid(String s, Long id)
    {
        UserCore user = userRepository.findUserCoreByEmail(s);
        if ((user == null)||(user.getId() == id)){return true;}
        return false;
    }


    public void CheckValidity(BindingResult result, boolean creating, UserFormBackingObject ob)
    {
        if(!nameIsValid(ob.getUsername(),ob.getId()))//If Username is already in use (but not by us)
        {
            result.rejectValue("username","error.usercore","That username is already in use!");//Add an error
        }
        if(!emailIsValid(ob.getEmail(),ob.getId()))//If Username is already in use (but not by us)
        {
            result.rejectValue("email","email.usercore","That email is already in use!");//Add an error
        }
        if ((creating) && (4 > ob.getPassword().length()|| ob.getPassword().length()>20))
        {
            result.rejectValue("password","password.usercore","Passwords should be between 4 and 20 characters!");//Add an error
        }
        if (!ob.getPassword().matches(ob.getPasswordConfirmation()))
        {
            result.rejectValue("passwordConfirmation","passwordConfirmation.usercore","Passwords do not match!");//Add an error

        }
        if ((ob.getAuthorityStrings().size() == 0))
        {
            result.rejectValue("authorityStrings","authorityStrings.usercore","Surely they have some role in this hospital!");//Add an error
        }
        if (ob.getEmail().length() == 0)
        {
            result.rejectValue("email","email.usercore","You must specify an email.");
        }
        if (ob.getFirstName().length() == 0)
        {
            result.rejectValue("firstName","firstName.usercore", "Please input the new users first name!");
        }
        if (ob.getLastName().length() == 0)
        {
            result.rejectValue("lastName","lastName.usercore", "Please input the new users surname!");
        }
        if (ob.getTitle() == null)
        {
            result.rejectValue("title","title.usercore", "Please select a Title!");
        }
    }

    @Override
    public List<UserCore> findMatchesFromFilter(UserInfo ob)
    {
        List<UserCore> userInfo = userRepository.findUserCoresByFirstNameContainsAndLastNameContainsAndEmailContainsAndUsernameContains(ob.getFirstName(), ob.getLastName(), ob.getEmail(), ob.getUsername());
        return userInfo;
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        doctorService.deleteDoctor(userRepository.getUserCoreByIdEquals(id));
        userRepository.deleteById(id);
        return true;
    }

    public void editUser(UserFormBackingObject user)
    {
        UserCore userToEdit = findUserById(user.getId());
        boolean isDoctor = userToEdit.isDoctor();
        boolean updatePassword = userToEdit.updateValues(user);
        saveUser(userToEdit,updatePassword);
        if (isDoctor != userToEdit.isDoctor())
        {
            if (isDoctor) { doctorService.deleteDoctor(userToEdit);}
            else
            {
                doctorService.saveDoctor(new Doctor(userToEdit));
            }
        }
    }

    @Override
    public List<UserCore> sortUsersByFirstName(List<UserCore> list) {
        // TODO
        return null;
    }

    @Override
    public List<UserCore> sortUsersByLastName(List<UserCore> list) {
        // TODO
        return null;
    }

    @Override
    public List<UserCore> sortUsersByUsername(List<UserCore> list) {
        // TODO
        return null;
    }

    @Override
    public List<UserCore> filterUsersBy(List<UserCore> list, Predicate<UserCore> predicate) {
        // TODO
        return null;
    }

    @Override
    public List<UserCore> filterUsersBy(List<UserCore> list, Iterable<Predicate<UserCore>> predicates) {
        // TODO
        return null;
    }

    @Override
    public Predicate<UserCore> usersFirstNameStartsWith(String str) {
        // TODO
        return null;
    }

    @Override
    public Predicate<UserCore> usersLastNameStartsWith(String str) {
        return null;
    }

    @Override
    public Predicate<UserCore> usersUserameStartsWith(String str) {
        return null;
    }

    @Override
    public Predicate<UserCore> userIsDoctor() {
        return null;
    }

    @Override
    public Predicate<UserCore> userIsAdmin() {
        return null;
    }


}

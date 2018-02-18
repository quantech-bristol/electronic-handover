package com.quantech.service.UserService;

import com.quantech.config.SecurityRoles;
import com.quantech.model.Patient;
import com.quantech.model.user.Title;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.model.user.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface UserService
{
    public void insertRootUser();

    public void deleteUser(String user) ;

    public void editPassword(String user, String newPass);

    /**
     * Saves a given user to the repository.
     * @param user The user to be saved into the repository.
     * @throws NullPointerException If the title, email, username, enabled, authority strings, password, first name or last name have not been set.
     * @throws IllegalArgumentException If email address is not in valid form.
     * @throws IllegalArgumentException If the username does not have the correct size.
     * @throws org.springframework.dao.DataIntegrityViolationException IIf the email address or username is not unique.
     */
    public boolean saveUser(UserCore user);

    public List<UserCore> getAllUsers() ;

    public UserCore findUserById(long id) ;

    public UserCore findUserByUsername(String username);

    public boolean nameIsValid(String s, Long validForUserId);

    public boolean emailIsValid(String s, Long validForUserId);

    public void CheckValidity(BindingResult result, boolean creating, UserInfo ob);

    public List<UserCore> findMatchesFromFilter(UserInfo ob);

    public boolean deleteUserById(Long id);

    /**
     * Sort the given list of users by their first name, alphabetically.
     * @param list The list of users.
     * @return A sorted list of users, by first name.
     */
    public List<UserCore> sortUsersByFirstName(List<UserCore> list);

    /**
     * Sort the given list of users by their last name, alphabetically.
     * @param list The list of users.
     * @return A sorted list of users, by last name.
     */
    public List<UserCore> sortUsersByLastName(List<UserCore> list);

    /**
     * Sort the given list of users by their username, alphabetically.
     * @param list The list of users.
     * @return A sorted list of users, by last name.
     */
    public List<UserCore> sortUsersByUsername(List<UserCore> list);

    /**
     * Filter list of a users by a given predicate.
     * @param list A list of users.
     * @param predicate A predicate to test the users against.
     * @return A list of users filtered by the given predicate.
     */
    public List<UserCore> filterUsersBy(List<UserCore> list, Predicate<UserCore> predicate);

    /**
     * Filter list of a users by a given set of predicates.
     * @param list A list of users.
     * @param predicates A collection of predicates to test the users against.
     * @return A list of users filtered by the given predicate.
     */
    public List<UserCore> filterUsersBy(List<UserCore> list, Iterable<Predicate<UserCore>> predicates);

    /**
     * A predicate that checks if the user's first name begins with the given string.
     * @param str The string to compare with.
     * @return The corresponding predicate object.
     */
    public Predicate<UserCore> usersFirstNameStartsWith(String str);

    /**
     * A predicate that checks if the user's last name begins with the given string.
     * @param str The string to compare with.
     * @return The corresponding predicate object.
     */
    public Predicate<UserCore> usersLastNameStartsWith(String str);

    /**
     * A predicate that checks if the user's username begins with the given string.
     * @param str The string to compare with.
     * @return The corresponding predicate object.
     */
    public Predicate<UserCore> usersUserameStartsWith(String str);

    /**
     * A predicate that checks if the user has doctor status.
     * @return The corresponding predicate object.
     */
    public Predicate<UserCore> userIsDoctor();

    /**
     * A predicate that checks if the user has admin status.
     * @return The corresponding predicate object.
     */
    public Predicate<UserCore> userIsAdmin();
}

package com.quantech.repo;

import org.springframework.data.repository.CrudRepository;
import com.quantech.model.user.UserCore;

import java.util.List;

public interface UserRepository extends CrudRepository<UserCore, Long> {

    UserCore findUserCoreByUsername(String username);

    UserCore getUserCoreByIdEquals(long id);

    void deleteUserCoreByUsername(String username);

    int countByUsername(String username);

    UserCore findUserCoreByEmail(String email);

    void deleteById(Long id);

    List<UserCore> findUserCoresByFirstNameContainsAndLastNameContainsAndEmailContainsAndUsernameContains(String firstName, String lastName, String email, String username);
}
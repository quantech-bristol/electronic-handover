package com.quantech.repo;

import org.springframework.data.repository.CrudRepository;
import com.quantech.model.user.UserCore;

public interface UserRepository extends CrudRepository<UserCore, Long> {

    public UserCore findUserCoreByUsername(String username);

    public UserCore getUserCoreByIdEquals(long id);

    public void deleteUserCoreByUsername(String username);

    public int countByUsername(String username);

    public UserCore findUserCoreByEmail(String email);
}
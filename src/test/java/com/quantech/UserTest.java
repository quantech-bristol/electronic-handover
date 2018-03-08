package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.repo.UserRepository;
import com.quantech.service.UserService.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)  // junit test runner
@SpringBootTest               // read app context

// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         TransactionalTestExecutionListener.class,       // use transactional test execution
                         DbUnitTestExecutionListener.class})             // to read data sets from file
@ActiveProfiles("test")                                                  // use application-test.yml properties (in-memory DB)
@DatabaseSetup("/dataSet1.xml")
@Transactional                                                           // rollback DB in between tests
public class UserTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    public void deleteUserTest() {
        // TODO
    }

    @Test
    public void editPasswordSuccessTest() {
        // TODO
    }

    @Test
    public void editPasswordInvalidTest() {
        // TODO
    }

    @Test
    public void saveUserSuccessTest() {
        // TODO
    }

    @Test
    public void saveUserWithNullFieldsTest() {
        // TODO
    }

    @Test
    public void saveUserInvalidFieldsTest() {
        //Includes non-uniqueness constraints and correct format constraints (i.e. emails).
        // TODO
    }

    @Test
    public void getAllUsersTest() {
        // TODO
    }

    @Test
    public void findByUserIdTest() {
        // TODO
    }

    @Test
    public void filterUsersByFirstNameStartsWithTest() {
        // TODO
    }

    @Test
    public void filterUsersByLastNameStartsWithTest() {
        // TODO
    }

    @Test
    public void filterUsersByUsernameStartsWithTest() {
        // TODO
    }

    @Test
    public void filterUsersByPermissionsTest() {
        // TODO
    }

    @Test
    public void sortUsersByFirstNameTest(){
        // TODO
    }

    @Test
    public void sortUsersByLastNameTest() {
        Assert.assertFalse(true);
    }
}
package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.Doctor;
import com.quantech.repo.DoctorRepository;
import com.quantech.repo.UserRepository;
import com.quantech.service.DoctorService.DoctorService;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)  // junit test runner
@SpringBootTest               // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         TransactionalTestExecutionListener.class, // use transactional test execution
                         DbUnitTestExecutionListener.class})       // to read data sets from file
@ActiveProfiles("test")       // use application-test.yml properties (in-memory DB)
@Transactional                // rollback DB in between tests
public class DoctorTest {
    @Autowired
    DoctorService doctorService;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; most recently renewed first.:
    public void sortByMostRecentlyRenewedFirstTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; least recently renewed first.:
    public void sortByLeastRecentlyRenewedFirstTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure ordering of sort doesn't matter when applying:
    public void sortByLeastRecentlyRenewedThenMostRecentlyRenewedFirstTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; alphabetically by first name.
    public void sortByFirstNameTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; alphabetically by last name.
    public void sortByLastNameTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - start of first name.
    public void filterDoctorsByStartOfFirstNameTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - start of last name.
    public void filterDoctorsByStartOfLastNameTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - renewed after date.
    public void filterDoctorsByRenewedAfterTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - renewed before date.
    public void filterDoctorsByRenewedBeforeTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - doctor team.
    public void filterDoctorsByTeamTest() {
        // TODO
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure filtering works - doctor teams.
    public void filterDoctorsByTeamsTest() {
        // TODO
    }

    @Test
    // Check if invalid email addresses are detected.
    public void invalidEmailCheckTest() {
        // TODO
        String e1 = "nuhat@bristol.com";
        String e2 = "nuhat@bristol.ac.uk";
        String e3 = "nuha-t@bristol.co.uk";
        String e4 = "nuhat@bri.st.ol.com";
        String e5 = "nuhat@bristol.c";
        String e6 = "nuhat@bristol.";
        String e7 = "nuha t@bristol.com";
        String e8 = "@bristol.com";
        String e9 = "nuhat@bristol";
        String e10 = "@";
        String e11 = "";
        String e12 = "nuhat";
        String e13 = "nuha-t@bristol.co.uk.";
        String e14 = "nuha-t@bristol.co..uk";

        boolean thrown = false;

        // These strings should be identified as valid.
        for (String s : new String[]{e1,e2,e3,e4}) {
            try {
                EntityFieldHandler.emailValidityCheck(s);
            } catch (IllegalArgumentException e) {
                thrown = true;
            }
            Assert.assertFalse(thrown);
            thrown = false;
        }

        // And these strings should be identified as invalid.
        for (String s : new String[]{e5,e6,e7,e8,e9,e10,e11,e12,e13,e14}) {
            try {
                EntityFieldHandler.emailValidityCheck(s);
            } catch (Exception e) {
                thrown = true;
            }
            Assert.assertTrue(thrown);
            thrown = false;
        }
    }

    // Use this to create a list of patients with a certain sequence of IDs.
    private List<Doctor> getDoctorsFromRepository(long[] ids) {
        List<Doctor> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(doctorRepository.findById(id));
        }
        return l1;
    }
}
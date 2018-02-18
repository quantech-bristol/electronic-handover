package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.Patient;
import com.quantech.model.Ward;
import com.quantech.model.user.Title;
import com.quantech.repo.JobContextRepository;
import com.quantech.repo.JobRepository;
import com.quantech.repo.PatientRepository;
import com.quantech.repo.WardRepository;
import com.quantech.service.JobsService.JobsService;
import com.quantech.service.PatientService.PatientService;
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
import java.util.*;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)  // junit test runner
@SpringBootTest               // read app context

// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         TransactionalTestExecutionListener.class,       // use transactional test execution
                         DbUnitTestExecutionListener.class})             // to read data sets from file
@ActiveProfiles("test")                                                  // use application-test.yml properties (in-memory DB)
@DatabaseSetup("/dataSet1.xml")
@Transactional                                                           // rollback DB in between tests
public class JobsTest {
    @Autowired
    JobContextRepository jobContextRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobsService jobsService;

    @Test
    public void getJobTest(){
        // TODO
    }

    @Test
    public void getAllJobsOfDoctorTest() {
        // TODO
    }

    @Test
    public void getAllJobsOfDoctorNotInDatabaseTest() {
        // TODO
    }

    @Test
    public void getAllUncompletedJobsFromDoctorTest() {
        // TODO
    }

    @Test
    public void getAllUncompletedJobsFromDoctorNotInDatabaseTest() {
        // TODO
    }

    @Test
    public void getAllJobsForPatientTest() {
        // TODO
    }

    @Test
    public void getAllJobsForPatientNotInDatabaseTest() {
        // TODO
    }

    @Test
    public void getAllUncompletedJobsForPatientTest() {
        // TODO
    }

    @Test
    public void getAllUncompletedJobsForPatientNotInDatabaseTest() {
        // TODO
    }

    @Test
    public void getAllCompletedJobsForPatientTest() {
        // TODO
    }

    @Test
    public void getAllCompletedJobsForPatientNotInDatabaseTest() {
        // TODO
    }

    @Test
    public void saveJobTest() {
        // TODO
    }

    @Test
    public void saveJobWithNullsTest() {
        // TODO
    }

    @Test
    public void saveJobWithInvalidJobContextTest() {
        // TODO
    }

    @Test
    public void saveJobContextTest() {
        // TODO
    }

    @Test
    public void saveJobContextWithNullsTest() {
        // TODO
    }

    @Test
    public void saveJobContextWithInvalidPatientTest() {
        // TODO
    }

    @Test
    public void handoverJobTest() {
        // TODO
    }

    @Test
    public void completeJobTest() {
        // TODO
    }

    @Test
    public void filterJobsByCategoryTest() {
        // TODO
        // Test conjunctive predicates too.
    }

    @Test
    public void filterJobsByUnwellPatientTest(){
        // TODO
    }

    @Test
    public void filterJobsByWardTest() {
        // TODO
        // Filtering multiple wards should yield empty list (conjunctive)
    }

    @Test
    public void sortJobsByAgeTest() {
        // TODO
    }
}
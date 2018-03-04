package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.*;
import com.quantech.model.user.Title;
import com.quantech.repo.*;
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
import javax.validation.constraints.Null;
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
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WardRepository wardRepository;

    // Making sure the correct list of joba is returned
    @Test
    public void getJobTest(){
        List<Job> l1 = getJobsFromRepository(new long[]{2L,4L,5L,7L});
        List<Job> l2 = jobsService.getAllJobsForPatient(patientRepository.findById(2L));
        Assert.assertTrue(sameContents(l1,l2));

        l1 = getJobsFromRepository(new long[]{2L,4L,5L});
        l2 = jobsService.getAllJobsOfContext(jobContextRepository.findById(2L));
        Assert.assertEquals(l1,l2);

        l1 = getJobsFromRepository(new long[]{1L,2L,3L,6L,7L});
        l2 = jobsService.getAllJobsOfDoctor(doctorRepository.findById(1L));
        Assert.assertEquals(l1,l2);
    }

    @Test
    public void getAllJobsOfDoctorNotInDatabaseTest() {
        List<Job> j = jobsService.getAllJobsOfDoctor(doctorRepository.findById(3L));
        Assert.assertEquals(new ArrayList<Job>(),j);
    }

    @Test
    // Checking that a job is being saved properly.
    public void saveJobTest() {
        List<Job> l1 = getJobsFromRepository(new long[]{2L,4L,5L});
        List<Job> l2 = jobsService.getAllJobsOfContext(jobContextRepository.findById(2L));
        Assert.assertEquals(l1,l2);

        Job j = new Job("Medicine",
                categoryRepository.findOne(1L),
                new Date(),
                null,
                jobContextRepository.findById(2L),
                doctorRepository.findById(1L));
        jobsService.saveJob(j);
        l1.add(j);
        l2 = jobsService.getAllJobsOfContext(jobContextRepository.findById(2L));
        Assert.assertEquals(l1,l2);
    }

    @Test
    // Make sure the system catches out nulls when it needs to.
    public void saveJobWithNullsTest() {
        // null description, category, creation date or job context.
        Job j = new Job();
        boolean thrown = false;
        try {
            jobsService.saveJob(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        j.setCategory(categoryRepository.findOne(1L));
        thrown = false;
        try {
            jobsService.saveJob(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        j.setCreationDate(new Date());
        j.setJobContext(jobContextRepository.findById(1L));
        j.setDoctor(doctorRepository.findById(1L));
        j.setDescription("test");
        thrown = false;
        try {
            jobsService.saveJob(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    // Making sure only valid j cs can be used.
    public void saveJobWithInvalidJobContextTest() {
        Job j = new Job("Medicine",
                categoryRepository.findOne(1L),
                new Date(),
                null,
                new JobContext(),
                doctorRepository.findById(1L));
        boolean thrown = false;
        try {
            jobsService.saveJob(j);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void saveJobContextTest() {
        List<JobContext> j1 = getJobContextsFromRepository(new long[]{1L,2L,3L,4L});
        JobContext j = new JobContext("details",
                false,
                new Date(),
                patientRepository.getOne(1L),
                1,
                wardRepository.findById(1L),
                new ArrayList<>(),
                new ArrayList<>());
        j1.add(j);
        jobsService.saveJobContext(j);
        Assert.assertEquals(j1,jobContextRepository.findAll());
    }

    @Test
    public void saveJobContextWithNullsTest() {
        JobContext j = new JobContext();
        boolean thrown = false;
        try {
            jobsService.saveJobContext(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        j.setUnwell(false);
        j.setCreationDate(new Date());
        thrown = false;
        try {
            jobsService.saveJobContext(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        j.setWard(wardRepository.findById(1L));
        j.setPatient(patientRepository.findById(1L));
        thrown = false;
        try {
            jobsService.saveJobContext(j);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    public void saveJobContextWithInvalidPatientTest() {
        JobContext j = new JobContext("details",
                false,
                new Date(),
                new Patient(),
                1,
                wardRepository.findById(1L),
                new ArrayList<>(),
                new ArrayList<>());
        boolean thrown = false;
        try {
            jobsService.saveJobContext(j);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void saveJobContextWithInvalidWardTest() {
        JobContext j = new JobContext("details",
                false,
                new Date(),
                patientRepository.getOne(1L),
                1,
                new Ward(),
                new ArrayList<>(),
                new ArrayList<>());
        boolean thrown = false;
        try {
            jobsService.saveJobContext(j);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    // Making sure that the doctor of a job only changes if it's incomplete.
    public void handoverJobTest() {
        Job j = jobRepository.findById(1L);
        Doctor d = doctorRepository.findById(2L);
        jobsService.handoverJob(j,d);
        j = jobRepository.findById(1L);
        Assert.assertEquals(j.getDoctor(),d);

        j = jobRepository.findById(7L);
        jobsService.handoverJob(j,d);
        j = jobRepository.findById(7L);
        Assert.assertEquals(j.getDoctor(),doctorRepository.findById(1L));
    }

    @Test
    public void completeJobTest() {
        Job j = jobRepository.findById(1L);
        jobsService.completeJob(j);
        //j = jobRepository.findById(1L);
        //Assert.assertTrue(j.getCompletionDate().compareTo(new Date()) == 0);
        // TODO
        // SHouldn't change completed date of finished job!!
        // SHould doctor entry be null?
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

    // Use this to create a list of jobs with a certain sequence of IDs.
    private List<Job> getJobsFromRepository(long[] ids) {
        List<Job> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(jobRepository.findById(id));
        }
        return l1;
    }

    // Use this to create a list of jobs with a certain sequence of IDs.
    private List<JobContext> getJobContextsFromRepository(long[] ids) {
        List<JobContext> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(jobContextRepository.findById(id));
        }
        return l1;
    }

    private boolean sameContents(List<Job> p1, List<Job> p2) {
        return p1.containsAll(p2) && p2.containsAll(p1);
    }}
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
    @Autowired
    RiskRepository riskRepository;

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
        j = jobRepository.findById(1L);
        Assert.assertTrue(j.getCompletionDate()!= null);

        // Can't complete completed job.
        j = jobRepository.findById(7L);
        jobsService.completeJob(j);
        j = jobRepository.findById(7L);
        Assert.assertTrue(j.getCompletionDate().compareTo(new Date()) != 0);
    }

    @Test
    public void filterJobsByCategoryTest() {
        List<Job> all = new ArrayList<>();
        jobRepository.findAll().forEach(all::add);

        List<Job> j1 = getJobsFromRepository(new long[]{3L,4L,6L});
        Category c = categoryRepository.findOne(2L);
        List<Job> j2 = jobsService.filterJobsBy(all,jobsService.jobIsOfCategory(c));
        Assert.assertEquals(j1,j2);
    }

    @Test
    public void filterJobContextsByUnwellPatientTest(){
        List<JobContext> all = new ArrayList<>();
        jobContextRepository.findAll().forEach(all::add);

        List<JobContext> j1 = getJobContextsFromRepository(new long[]{1L,2L});
        List<JobContext> j2 = jobsService.filterJobContextsBy(all,jobsService.patientIsUnwell());
        Assert.assertEquals(j1,j2);
    }

    @Test
    public void filterJobsByCompleteTest() {
        List<Job> all = new ArrayList<>();
        jobRepository.findAll().forEach(all::add);

        List<Job> j1 = getJobsFromRepository(new long[]{6L,7L});
        List<Job> j2 = jobsService.filterJobsBy(all,jobsService.jobIsComplete());
        Assert.assertEquals(j1,j2);

        j1 = getJobsFromRepository(new long[]{1L,2L,3L,4L,5L});
        j2 = jobsService.filterJobsBy(j1,jobsService.jobIsUncomplete());
        Assert.assertEquals(j1,j2);
    }

    @Test
    public void filterJobContextsByRisk() {
        List<JobContext> all = new ArrayList<>();
        jobContextRepository.findAll().forEach(all::add);

        JobContext j1 = jobContextRepository.findById(1L);
        JobContext j2 = jobContextRepository.findById(2L);
        JobContext j3 = jobContextRepository.findById(3L);
        Risk r1 = riskRepository.findOne(1L);
        Risk r2 = riskRepository.findOne(2L);
        j1.addRisk(r1); j1.addRisk(r2);
        j2.addRisk(r2);
        j3.addRisk(r1);
        jobsService.saveJobContext(j1);
        jobsService.saveJobContext(j2);
        jobsService.saveJobContext(j3);

        List<JobContext> jc1 = getJobContextsFromRepository(new long[]{1L,3L});
        List<JobContext> jc2 = jobsService.filterJobContextsBy(all,jobsService.patientHasRisk(r1));
        Assert.assertEquals(jc2,jc2);
        jc1 = getJobContextsFromRepository(new long[]{1L,2L});
        jc2 = jobsService.filterJobContextsBy(all,jobsService.patientHasRisk(r2));
        Assert.assertEquals(jc2,jc2);
    }

    @Test
    public void filterJobContextsByWard() {
        List<JobContext> all = new ArrayList<>();
        jobContextRepository.findAll().forEach(all::add);
        Ward w = wardRepository.findById(2L);

        List<JobContext> jc1 = getJobContextsFromRepository(new long[]{4L});
        List<JobContext> jc2 = jobsService.filterJobContextsBy(all,jobsService.patientIsInWard(w));
        Assert.assertEquals(jc2,jc2);
    }

    @Test  // Checking combination of job and job context predicates.
    public void filterWardAndUncompletedJobs() {
        List<JobContext> all = new ArrayList<>();
        jobContextRepository.findAll().forEach(all::add);
        Ward r = wardRepository.findOne(1L);

        List<JobContext> jc1 = getJobContextsFromRepository(new long[]{1L,2L,3L});
        List<Job> j1 = getJobsFromRepository(new long[]{1L});
        List<Job> j2 = getJobsFromRepository(new long[]{2L,4L,5L});
        List<Job> j3 = getJobsFromRepository(new long[]{3L});
        Set<Predicate<JobContext>> pjc = new HashSet<>(); pjc.add(jobsService.patientIsInWard(r));
        Set<Predicate<Job>> pj = new HashSet<>(); pj.add(jobsService.jobIsUncomplete());

        List<JobContext> jc2 = jobsService.filterJobContextsBy(all,pjc,pj);
        Assert.assertEquals(jc1,jc2);
        Assert.assertEquals(jc1.get(0).getJobs(),j1);
        Assert.assertTrue(sameContents(jc1.get(1).getJobs(),j2));
        Assert.assertEquals(jc1.get(2).getJobs(),j3);
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
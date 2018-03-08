package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup("/dataSet1.xml")
@Transactional
public class _2_3_aRequirementsTest {
//    2.3.a.  Handover Existing Job
//    Description: 	The doctor user logged into the system has the ability to handover a single job that
//    they are responsible for to another doctor user, effectively transferring responsibility to them.
    @Test // TODO: Check that each stage of the action flow works properly.
    public void handoverExistingJobSuccessful() {
        //    Preconditions:	i) The user currently logged in has doctor status.
        //    ii) The user currently has responsibility over the job they wish to handover.
        //    iii) The patient for which the job holds exists on the system.
        //    iv) The job has not been completed.
        //    Action:
        // 1) The sender doctor user selects the “handover” button for the desired job when viewing the
        //    list of jobs associated to a patient.
        //2) The system compiles a list of doctor users from the database that aren’t the current user.
        //3) The system populates the “description” text box with the job’s existing description.
        //4) The sender doctor user selects a recipient doctor user to handover to from this list.
        //5) The sender doctor  user makes potential changes to the job description by typing them in.
        //6) The user presses the “send handover” button.
        //7) If the recipient doctor hasn’t been chosen, an error message is thrown and the user is prompted to enter it.
        //8) Else the handover is successful and the responsible doctor attribute in the corresponding Job object changes to the recipient doctor.
        // TODO
    }

    public void handoverExistingJobUnsuccessful() {
        //TODO
    }

    @Test
    public void handoverUnsuccessfulPostConditionTest() {
        //a) If the handover is unsuccessful, the preconditions still hold.
        //TODO
    }

    // Testing that 2.3.a. post conditions hold.
    @Test
    public void _2_3_a_aTest() {
        //a) If the handover is unsuccessful, the preconditions still hold.
        // TODO
    }

    @Test
    public void _2_3_a_bTest() {
        //b) The job’s corresponding patient is in the recipient doctor’s patient list.
        // TODO
    }

    @Test
    public void _2_3_a_cTest() {
        //c) The recipient doctor can see all jobs relating to the current job’s context.
        // TODO
    }

    @Test
    public void _2_3_a_dTest() {
        //d) The recipient doctor is able to mark the newly received job as “completed”.
        // TODO
    }

    @Test
    public void _2_3_a_eTest() {
        //e) The recipient doctor is able to carry out a handover using the newly received job.
        // TODO
    }

    //f) If the sender doctor is still responsible for other jobs relating to the sent job’s context:
    @Test
    public void _2_3_a_f_ITest() {
        //I) The patient remains in the sender doctor’s patient list.
        // TODO
    }

    //f) If the sender doctor is still responsible for other jobs relating to the sent job’s context:
    @Test
    public void _2_3_a_f_IITest() {
        //II) The sent job is marked as pending.
        // TODO
    }

    //f) If the sender doctor is still responsible for other jobs relating to the sent job’s context:
    @Test
    public void _2_3_a_f_IIITest() {
        //III) The sender doctor cannot mark it as completed
        // TODO
    }

    //f) If the sender doctor is still responsible for other jobs relating to the sent job’s context:
    @Test
    public void _2_3_a_f_IVTest() {
        //IV) The sender doctor cannot hand it over.
        // TODO
    }

    @Test
    public void _2_3_a_gTest() {
        //g) If otherwise, the patient is taken off of the sender doctor’s list.
        // TODO
    }
}

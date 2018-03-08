package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class _2_3_bRequirementsTest {
/*
2.3.c. Create New Patient
Description: 	Any user on the system has the ability to add patients to the system. This doesn’t include
sensitive information, only identifiers to “anchor” job contexts and jobs onto.
*/
    @Test // TODO: Make sure that each stage of the action flow holds.
    public void createNewPatientTest() {
        /*
        Precondition:	i) The user is logged into the system
        Action:		1) The user fills in the following fields:
                    a) First name (compulsory)
                    b) Last name (compulsory)
                    c) Date of birth (compulsory)
                    d) Title (compulsory)
                    e) NHS number (compulsory if no hospital number is given)
                    f) Hospital number (compulsory if no NHS number is given)
        2) The user presses the “add patient” button.
                3) The system runs the following validity checks:
                    a) Check that compulsory fields have been set.
                    b) Check that the date of birth isn’t in the future.
                    c) Check that the given name doesn’t consist of empty strings.
                    d) If an NHS number is given, check that:
        I) It is 10 digits long.
        II) It is valid, by computing its checksum using the Modulus 11 algorithm.
        III) It is unique, that is no other patient in the database has it.
        f) If a hospital number is given, check that no other patient is using the same number.
                4) If any of the checks fail, a corresponding error message is displayed next to the field and the
        user is prompted to enter it again.
        5) Otherwise the patient is saved into the database.
        */
    }

    // Testing that 2.3.b. post conditions hold.
    @Test
    public void _2_3_b_a() {
        // a) Only if the addition was successful is there one more patient in the database.
    }
}

package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.Patient;
import com.quantech.model.Ward;
import com.quantech.model.user.Title;
import com.quantech.repo.PatientRepository;
import com.quantech.repo.WardRepository;
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
import java.time.LocalDate;
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
public class PatientTest {
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    WardRepository wardRepository;

    @Test
    // Making sure the service obtains patients by ID properly.
    public void samePatientReturnedTest() {
        Patient[] p1 = new Patient[]{patientRepository.findById(1L),
                patientRepository.findById(1000L),
                patientRepository.findByNHSNumber(27L),
                patientRepository.findByHospitalNumber(6L)};

        Patient[] p2 = new Patient[]{patientService.getPatientById(1L),
                patientService.getPatientById(1000L),
                patientService.getPatientByNHSNumber(27L),
                patientService.getPatientByHospitalNumber(6L)};

        for (int i = 0; i < p1.length; i++)
            Assert.assertEquals(p1[i],p2[i]);
    }

    @Test
    // Make sure that the service returns all patients actually in the database.
    public void allPatientsReturnedTest() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L});
        List<Patient> p2 = patientService.getAllPatients();
        Assert.assertTrue(sameContents(p1,p2));
    }

    @Test
    // Check that the service does actually properly sort the patients alphabetically by their first names, even when
    // some of the first names have the same value.
    public void sortPatientsByFirstNameCorrectOrder() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,5L,7L,4L,3L,6L,9L,8L});
        List<Patient> p2 = patientService.sortPatientsByFirstName(patientService.getAllPatients());
        Assert.assertEquals(p1,p2);

    }

    @Test
    // Check that the service does actually properly sort the patients alphabetically by their last names.
    public void sortPatientsByLastNameCorrectOrder() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,5L,2L,7L,3L,6L,4L,9L,8L});
        List<Patient> p2 = patientService.sortPatientsByLastName(patientService.getAllPatients());
        Assert.assertEquals(p1,p2);
    }

    @Test
    // Check that the patient service detects when all fields of the patient are null, and the patient isn't in the DB.
    public void checkAllNullFieldsDetectedInPatient() {
        Patient p = new Patient();
        p.setId(10L);
        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (Exception e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        Assert.assertFalse(patientRepository.exists(10L));
    }

    @Test
    // Make sure that no null fields are detected when there aren't any.
    public void checkNoNullFieldsDetectedInPatient() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setNHSNumber(2790585245L);
        p.setHospitalNumber(80L);
        p.setTitle(Title.Miss);

        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (Exception e) {
            thrown = true;
        }

        Assert.assertFalse(thrown);
    }

    @Test
    // Should be allowed to have one of hospital number and nhs number null, as they are identifiers.
    public void allowedNullNHSNumberTest() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setHospitalNumber(8000L);
        p.setTitle(Title.Miss);

        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (Exception e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    // Patient shouldn't be allowed to have both hospital and nhs number as null.
    public void bothNumbersNullTest() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setTitle(Title.Miss);

        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (Exception e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    // Should be allowed to have one of hospital number and nhs number null, as they are identifiers.
    public void allowedNullHospitalNumberTest() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setNHSNumber(2790585245L);
        p.setTitle(Title.Miss);

        Boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    // Should be able to detect when only some of the fields of the patient are null. (In this case it's the birth and admission dates)
    public void checkTwoNullFieldsDetectedInPatient() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setTitle(Title.Miss);

        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    // Making sure patients are deleted properly.
    public void deletePatientTest() {
        patientService.deletePatient(3L);
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,4L,5L,6L,7L,8L,9L});
        List<Patient> p2 = patientService.getAllPatients();
        Assert.assertTrue(sameContents(p1,p2));
    }

    @Test
    // Making sure that the method of filtering the patients by the start of their first name works.
    public void filterPatientsByStartOfFirstNameIsATest() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,4L,5L,7L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsFirstNameStartsWith("A"));
        Assert.assertEquals(p1,p2);
    }

    @Test
    // Making sure that the method of filtering the patients by the start of their first name works.
    public void filterPatientsByStartOfFirstNameIsChaTest() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{6L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsFirstNameStartsWith("Cha"));
        Assert.assertEquals(p1,p2);
    }

    @Test
    // Making sure that filtering for "Ari" and "A" only gives strings that match "Ari", and ordering of predicates
    // doesn't matter.
    public void filterPatientsByStartOfFirstNameIsAriAndATest() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{4L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsFirstNameStartsWith("Ari"));
        p2 = patientService.filterPatientsBy(p2, patientService.patientsFirstNameStartsWith("A"));
        Assert.assertEquals(p1,p2);

        p2 = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsFirstNameStartsWith("A"));
        p2 = patientService.filterPatientsBy(p2, patientService.patientsFirstNameStartsWith("Ari"));
        Assert.assertEquals(p1,p2);
    }

    @Test
    // When predicates conflict one another, the filter should yield an empty list.
    public void filterPatientsByStartOfDifferentFirstNameTest() {
        Set<Predicate<Patient>> pred = new HashSet<>();
        pred.add(patientService.patientsFirstNameStartsWith("A"));
        pred.add(patientService.patientsFirstNameStartsWith("Z"));

        List<Patient> p = patientService.filterPatientsBy(patientService.getAllPatients(),pred);
        Assert.assertEquals(new ArrayList<Patient>(),p);
    }

    @Test
    // Making sure last name filter is working properly.
    public void filterPatientsByStartOfLastNameHaTest() {
        List<Patient> p1 = getPatientsFromRepository(new long[]{4L,6L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsLastNameStartsWith("Ha"));
        Assert.assertEquals(p1,p2);
    }

    @Test
    // When patient fulfilling predicate isn't found, return empty list.
    public void filterPatientsByStartOfLastNameNoneFoundTest() {
        List<Patient> p = patientService.filterPatientsBy(patientService.getAllPatients(),
                patientService.patientsLastNameStartsWith("abcdefg"));
        Assert.assertEquals(new ArrayList<Patient>(),p);
    }

    @Test
    // Different filters working in conjunction should be fine.
    public void filterPatientsByStartOfFirstAndLastNameTest() {
        Set<Predicate<Patient>> pred = new HashSet<>();
        pred.add(patientService.patientsFirstNameStartsWith("A"));
        pred.add(patientService.patientsLastNameStartsWith("B"));

        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,5L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),pred);
        Assert.assertEquals(p1,p2);
    }

    @Test
    // When a patient that doesn't fulfil multiple predicates isn't found, return empty list.
    public void filterPatientsByStartOfFirstAndLastNameNoneFoundTest() {
        Set<Predicate<Patient>> pred = new HashSet<>();
        pred.add(patientService.patientsFirstNameStartsWith("abcd"));
        pred.add(patientService.patientsLastNameStartsWith("wxyz"));

        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),pred);
        Assert.assertEquals(new ArrayList<>(),p2);
    }

    @Test
    // Filtering by ward.
    public void filterPatientsByWardTest() {
        Ward w = wardRepository.findById(1L);
        Assert.assertTrue(patientService.patientInWard(w).test(patientService.getPatientById(1L)));
        Assert.assertTrue(patientService.getAllPatients().size() > 0);
        List<Patient> p1 = getPatientsFromRepository(new long[]{1L,2L,3L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientInWard(w));
        Assert.assertEquals(p1,p2);
    }

    @Test
    // Filtering by ward and bed.
    public void filterPatientsByWardAndBedTest() {
        Ward w = wardRepository.findById(1L);
        Set<Predicate<Patient>> pred = new HashSet<>();
        pred.add(patientService.patientInWard(w));
        pred.add(patientService.patientsBedIs(1));

        List<Patient> p1 = getPatientsFromRepository(new long[]{1L});
        List<Patient> p2 = patientService.filterPatientsBy(patientService.getAllPatients(),pred);
        Assert.assertEquals(p1,p2);
    }

    @Test
    // Check that NHS numbers that are invalid are flagged as such.
    public void nhsNumbersInvalidTest() {
        Patient p = new Patient();
        p.setBirthDate(LocalDate.now());
        p.setFirstName("Nuha");
        p.setLastName("Tumia");
        p.setTitle(Title.Miss);

        long test1 = 19999999991919L;
        long test2 = 7665993754L;
        long test3 = 35L;
        long test4 = 1014392802L;

        boolean thrown = false;

        try {
            p.setNHSNumber(test1);
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        thrown = false;

        try {
            p.setNHSNumber(test2);
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        thrown = false;

        try {
            p.setNHSNumber(test3);
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
        thrown = false;

        try {
            p.setNHSNumber(test4);
            patientService.savePatient(p);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    // Checking that first names and last names are being formatted in the correct way.
    public void patientNameFormattedCorrectlyTest() {
        Assert.assertEquals("Nuha", EntityFieldHandler.putNameIntoCorrectForm("nUHa"));
        Assert.assertEquals("Tumia", EntityFieldHandler.putNameIntoCorrectForm("TumIA"));

        Assert.assertEquals("Sally-Anne", EntityFieldHandler.putNameIntoCorrectForm("      sally-anne       "));
        Assert.assertEquals("Tyson", EntityFieldHandler.putNameIntoCorrectForm("tyson"));

        Assert.assertEquals("Sally-Anne", EntityFieldHandler.putNameIntoCorrectForm("sally-anne"));
        Assert.assertEquals("Tyson", EntityFieldHandler.putNameIntoCorrectForm("tyson"));

        Assert.assertEquals("Sally-Anne", EntityFieldHandler.putNameIntoCorrectForm("sally   -  anne"));
        Assert.assertEquals("Tyson", EntityFieldHandler.putNameIntoCorrectForm("tyson"));

        Assert.assertEquals("Sally-Anne", EntityFieldHandler.putNameIntoCorrectForm("sally   -  anne"));
        Assert.assertEquals("Tyson", EntityFieldHandler.putNameIntoCorrectForm("tyson"));

        Assert.assertEquals("Cameron", EntityFieldHandler.putNameIntoCorrectForm("CAMERON"));
        Assert.assertEquals("O'Hara", EntityFieldHandler.putNameIntoCorrectForm("O'HARA"));

        Assert.assertEquals("Nabil Suliman", EntityFieldHandler.putNameIntoCorrectForm("            Nabil    Suliman            "));
        Assert.assertEquals("Tumia", EntityFieldHandler.putNameIntoCorrectForm("Tumia        "));
    }

    @Test
    // Errors thrown when an empty name field is attempted.
    public void emptyNameThrowsErrorTest() {
        boolean thrown = false;
        Patient p = new Patient();
        try {
            EntityFieldHandler.nameValidityCheck("");
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
        thrown = false;

        try {
            EntityFieldHandler.nameValidityCheck("          ");
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    // Use this to create a list of patients with a certain sequence of IDs.
    private List<Patient> getPatientsFromRepository(long[] ids) {
        List<Patient> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(patientRepository.findById(id));
        }
        return l1;
    }

    private boolean sameContents(List<Patient> p1, List<Patient> p2) {
        return p1.containsAll(p2) && p2.containsAll(p1);
    }
}
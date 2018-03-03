package com.quantech.service.PatientService;

import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserInfo;
import com.quantech.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("patientService")
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> ps = new ArrayList<>();
        patientRepository.findAll().forEach(p -> ps.add(p));
        return ps;
    }

    @Override
    public List<Patient> sortPatientsByFirstName(List<Patient> list) {
        return sortPatients(list, firstNameAlphabeticalComparator());
    }

    @Override
    public List<Patient> sortPatientsByLastName(List<Patient> list) {
        return sortPatients(list, lastNameAlphabeticalComparator());
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findOne(id);
    }

    @Override
    public Patient getPatientByHospitalNumber(Long hospNum) {
        return patientRepository.findByHospitalNumber(hospNum);
    }

    @Override
    public Patient getPatientByNHSNumber(Long nhsNum) {
        return patientRepository.findByNHSNumber(nhsNum);
    }

    @Override
    public void savePatient(Patient patient) throws NullPointerException, IllegalArgumentException {
        if (patient == null)
            throw new NullPointerException("Error: patient object cannot be null.");

        // Check if date of birth is null.
        EntityFieldHandler.nullCheck(patient.getBirthDate(),"date of birth");

        // Check if the first name is null;
        String name = patient.getFirstName();
        EntityFieldHandler.nameValidityCheck(name);
        patient.setFirstName(EntityFieldHandler.putNameIntoCorrectForm(name));

        // Check if the last name is null;
        name = patient.getLastName();
        EntityFieldHandler.nameValidityCheck(name);
        patient.setLastName(EntityFieldHandler.putNameIntoCorrectForm(name));

        // Check that the NHS number of the patient is valid, if it exists.
        if (patient.getNHSNumber() != null) {
            NHSNumberValidityCheck(patient.getNHSNumber());
            //if (patientRepository.findByNHSNumber(patient.getNHSNumber()) != null)
            //    throw new DataIntegrityViolationException("Error: Patient with given NHS number already exists.");
        }
        else {
            // Identification numbers cannot both be null.
            if (patient.getHospitalNumber() == null)
                throw new IllegalArgumentException("Error: patient requires either a hospital number or an NHS number");
            //else
            //    if (patientRepository.findByHospitalNumber(patient.getHospitalNumber()) != null)
            //        throw new DataIntegrityViolationException("Error: Patient with given hospital number already exists");
        }

        // Check that the patient's date of birth isn't in the future.
        if (patient.getBirthDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Error: patient's date of birth cannot be in the future.");

        // Now that all check have passed, save the patient.
        patientRepository.save(patient);
    }

    // Used to check that an NHS number is valid before setting an attribute to be equal to it.
    private void NHSNumberValidityCheck(Long NHSNumber) {
        int digits = NHSNumber.toString().length();

        if (digits > 10)
            throw new IllegalArgumentException("Error: NHS number has too many digits.");

        if (NHSNumber < 0)
            throw new IllegalArgumentException("Error: NHS number cannot be negative.");

        // Check that the checksum is correct.
        if ( !checksumCorrect(NHSNumber) )
            throw new IllegalArgumentException("Error: NHS number is not valid (checksum does not match)");
    }

    // Checks that the checksum of the NHS number is correct.
    private int checkDigit(Long n) {
        String digits = n.toString();

        if (digits.length() < 10) {
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < 10 - digits.length(); i++) {
                zeros.append('0');
            }
            digits = zeros.toString() + digits;
        }

        // Applying Modulus 11 algorithm:
        // Source: http://www.datadictionary.nhs.uk/data_dictionary/attributes/n/nhs/nhs_number_de.asp?shownav=1
        // 1- Apply factors:
        int sum = 0; int factor = 10;
        for (int i = 0; i < 9; i++) {
            int dig = Character.getNumericValue(digits.charAt(i));
            sum += dig*factor;
            factor--;
        }
        // 2- Find the remainder of dividing by 11.
        int r = sum % 11;
        // 3- Take the remainder away from 11 to get the check digit.
        int checkDigit = 11 - r;
        // 4- If the value is 10 then the check digit used is 0. If it is 0, then the number is invalid.
        if (checkDigit == 10)
            return -1;
        if (checkDigit == 11)
            checkDigit = 0;
        // 5- Check the remainder matches the check digit.
        return checkDigit;
    }

    // Checks if the check digit generated from an NHS number matches the checksum provided in the number.
    public boolean checksumCorrect(Long n) {
        String digits = n.toString();
        int checkSum = Character.getNumericValue(digits.charAt(digits.length()-1));
        int checkDigit = checkDigit(n);
        return (checkSum == checkDigit && checkDigit != -1);
    }

    @Override
    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.delete(id);
    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Iterable<Predicate<Patient>> predicates) {
        Stream<Patient> stream = list.stream();
        for (Predicate<Patient> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public Predicate<Patient> patientsFirstNameStartsWith(String str) {
        return p -> p.getFirstName().startsWith(str);
    }

    @Override
    public Predicate<Patient> patientsLastNameStartsWith(String str) {
        return p -> p.getLastName().startsWith(str);
    }

    @Override
    public Predicate<Patient> patientInWard(Ward ward) {
        return p -> {
            List<Ward> w = p.getJobContexts().stream().map(JobContext::getWard).collect(Collectors.toList());
            return w.contains(ward);
        };
    }

    @Override
    public Predicate<Patient> patientsBedIs(Integer bed) {
        return p -> {
            List w = p.getJobContexts().stream().map(JobContext::getBed).collect(Collectors.toList());
            return w.contains(bed);
        };
    }

    @Override
    public Predicate<Patient> patientIsUnwell() {
        return p -> {
            List<JobContext> contexts = sortContextsByDateCreatedMostRecentFirst(p.getJobContexts());
            return contexts.get(0).getUnwell();
        };
    }

    /* Helper Methods */

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Patient> firstNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(!(o1.getFirstName() == null || o2.getFirstName() == null)){
                if (o1.getFirstName().compareTo(o2.getFirstName()) == 0)
                    return lastNameAlphabeticalComparator().compare(o1,o2);
                else
                    return o1.getFirstName().compareTo(o2.getFirstName());
            }
            return 0;
        };
    }

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Patient> lastNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(o1.getLastName() != null && o2.getLastName() != null){
                return o1.getLastName().compareTo(o2.getLastName());
            }
            return 0;
        };
    }

    // Returns a list sorted using the input comparator.
    private List<Patient> sortPatients(List<Patient> l, Comparator<Patient> c) {
        l.sort(c);
        return l;
    }
    public List<JobContext> sortContextsByDateCreatedMostRecentFirst(List<JobContext> list) {
        list.sort(Comparator.comparing(JobContext::getCreationDate));
        return list;
    }

    public List<Patient> findMatchesFromFilter(PatientFormBackingObject p) {
        List<Patient> patients = patientRepository.findPatientsByFirstNameContainsAndLastNameContains(p.getFirstName(), p.getLastName());
        return patients;
    }

}

package com.quantech.service.PatientService;

import com.quantech.model.Doctor;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import com.quantech.model.Ward;
import com.quantech.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
        patientRepository.save(patient);
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
    public Predicate<Patient> latestWardIs(Ward ward) {
        return p -> {
            List<JobContext> contexts = sortContextsByDateCreatedMostRecentFirst(p.getJobContexts());
            return (contexts.get(0).getId() == ward.getId());
        };
    }

    @Override
    public Predicate<Patient> patientsBedIs(Integer bed) {
        return p -> {
            List<JobContext> contexts = sortContextsByDateCreatedMostRecentFirst(p.getJobContexts());
            if (contexts.get(0) == null) return false;
            else return (contexts.get(0).getBed() == bed);
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
}

package com.quantech.service.DoctorService;

import com.quantech.model.Doctor;
import com.quantech.model.Patient;
import com.quantech.model.user.UserCore;
import com.quantech.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctorRepository.findAll().forEach(doctors::add);
        return doctors;
    }

    @Override
    public List<Doctor> sortDoctorsByFirstName(List<Doctor> list) {
        return sortList(list,firstNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Doctor> firstNameAlphabeticalComparator() {
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

    @Override
    public List<Doctor> sortDoctorsByLastName(List<Doctor> list) {
        return sortList(list,lastNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Doctor> lastNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(o1.getLastName() != null && o2.getLastName() != null){
                return o1.getLastName().compareTo(o2.getLastName());
            }
            return 0;
        };
    }

    // Returns a list sorted using the input comparator.
    private List<Doctor> sortList(List<Doctor> l, Comparator<Doctor> c) {
        l.sort(c);
        return l;
    }

    @Override
    public Doctor getDoctor(UserCore user) {
        return doctorRepository.findById(user.getId());
    }

    @Override
    public void saveDoctor(Doctor doctor) throws NullPointerException, IllegalArgumentException {
        // TODO
        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(UserCore user) {
        doctorRepository.deleteByUser_id(user.getId());
    }

    @Override
    public List<Patient> getPatientsUnderCareOf(UserCore user) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Predicate<Doctor> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Iterable<Predicate<Doctor>> predicates) {
        Stream<Doctor> stream = list.stream();
        for (Predicate<Doctor> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public Predicate<Doctor> doctorsFirstNameStartsWith(String str) {
        return doctor -> doctor.getFirstName().startsWith(str);
    }

    @Override
    public Predicate<Doctor> doctorsLastNameStartsWith(String str) {
        return doctor -> doctor.getLastName().startsWith(str);
    }
}

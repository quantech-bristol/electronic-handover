package com.quantech.service;

import com.quantech.model.Doctor;
import com.quantech.model.Patient;
import com.quantech.model.user.UserCore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService {
    @Override
    public List<Doctor> getAllDoctors() {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByDateRenewedMostRecentFirst(List<Doctor> list) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByDateRenewedMostRecentLast(List<Doctor> list) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByFirstName(List<Doctor> list) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByLastName(List<Doctor> list) {
        // TODO
        return null;
    }

    @Override
    public Doctor getDoctor(UserCore user) {
        // TODO
        return null;
    }

    @Override
    public void saveDoctor(Doctor doctor) throws NullPointerException, IllegalArgumentException {
        // TODO
    }

    @Override
    public void deleteDoctor(Long id) {
       // TODO
    }

    @Override
    public List<Patient> getPatientsUnderCareOf(UserCore user) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Predicate<Doctor> predicate) {
        // TODO
        return null;
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Iterable<Predicate<Doctor>> predicates) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Doctor> doctorsFirstNameStartsWith(String str) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Doctor> doctorsLastNameStartsWith(String str) {
        // TODO
        return null;
    }
}

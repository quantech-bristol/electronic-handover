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
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByDateRenewedMostRecentFirst(List<Doctor> list) {
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByDateRenewedMostRecentLast(List<Doctor> list) {
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByFirstName(List<Doctor> list) {
        return null;
    }

    @Override
    public List<Doctor> sortDoctorsByLastName(List<Doctor> list) {
        return null;
    }

    @Override
    public Doctor getDoctor(UserCore user) {
        return null;
    }

    @Override
    public void saveDoctor(Doctor doctor) throws NullPointerException, IllegalArgumentException {

    }

    @Override
    public void deleteDoctor(Long id) {

    }

    @Override
    public List<Patient> getPatientsUnderCareOf(UserCore user) {
        return null;
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Predicate<Doctor> predicate) {
        return null;
    }

    @Override
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Iterable<Predicate<Doctor>> predicates) {
        return null;
    }

    @Override
    public Predicate<Doctor> doctorsFirstNameStartsWith(String str) {
        return null;
    }

    @Override
    public Predicate<Doctor> doctorsLastNameStartsWith(String str) {
        return null;
    }
}

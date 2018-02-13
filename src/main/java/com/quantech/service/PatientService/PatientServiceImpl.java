package com.quantech.service;

import com.quantech.model.Doctor;
import com.quantech.model.Patient;
import com.quantech.model.Ward;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service("patientService")
public class PatientServiceImpl implements PatientService {
    @Override
    public List<Patient> getAllPatients() {
        return null;
    }

    @Override
    public List<Patient> getAllDoctorsPatients(Doctor doctor) {
        return null;
    }

    @Override
    public List<Patient> sortPatientsByFirstName(List<Patient> list) {
        return null;
    }

    @Override
    public List<Patient> sortPatientsByLastName(List<Patient> list) {
        return null;
    }

    @Override
    public Patient getPatientById(Long id) {
        return null;
    }

    @Override
    public Patient getPatientByHospitalNumber(Long id) {
        return null;
    }

    @Override
    public Patient getPatientByNHSNumber(Long nhsNum) {
        return null;
    }

    @Override
    public void savePatient(Patient patient) throws NullPointerException, IllegalArgumentException {

    }

    @Override
    public void deletePatient(Patient patient) {

    }

    @Override
    public void deletePatient(Long id) {

    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate) {
        return null;
    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Iterable<Predicate<Patient>> predicates) {
        return null;
    }

    @Override
    public Predicate<Patient> patientsFirstNameStartsWith(String str) {
        return null;
    }

    @Override
    public Predicate<Patient> patientsLastNameStartsWith(String str) {
        return null;
    }

    @Override
    public Predicate<Patient> patientsWardIs(Ward ward) {
        return null;
    }

    @Override
    public Predicate<Patient> patientsBedIs(String str) {
        return null;
    }

    @Override
    public Predicate<Patient> patientIsUnwell() {
        return null;
    }
}

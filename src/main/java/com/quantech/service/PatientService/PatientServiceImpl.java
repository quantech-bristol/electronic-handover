package com.quantech.service.PatientService;

import com.quantech.model.Doctor;
import com.quantech.model.Patient;
import com.quantech.model.Ward;
import com.quantech.service.PatientService.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service("patientService")
public class PatientServiceImpl implements PatientService {
    @Override
    public List<Patient> getAllPatients() {
        // TODO
        return null;
    }

    @Override
    public List<Patient> getAllDoctorsPatients(Doctor doctor) {
        // TODO
        return null;
    }

    @Override
    public List<Patient> sortPatientsByFirstName(List<Patient> list) {
        // TODO
        return null;
    }

    @Override
    public List<Patient> sortPatientsByLastName(List<Patient> list) {
        // TODO
        return null;
    }

    @Override
    public Patient getPatientById(Long id) {
        // TODO
        return null;
    }

    @Override
    public Patient getPatientByHospitalNumber(Long id) {
        // TODO
        return null;
    }

    @Override
    public Patient getPatientByNHSNumber(Long nhsNum) {
        // TODO
        return null;
    }

    @Override
    public void savePatient(Patient patient) throws NullPointerException, IllegalArgumentException {
        // TODO
    }

    @Override
    public void deletePatient(Patient patient) {
        // TODO
    }

    @Override
    public void deletePatient(Long id) {
       // TODO
    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate) {
        // TODO
        return null;
    }

    @Override
    public List<Patient> filterPatientsBy(List<Patient> list, Iterable<Predicate<Patient>> predicates) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Patient> patientsFirstNameStartsWith(String str) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Patient> patientsLastNameStartsWith(String str) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Patient> patientsWardIs(Ward ward) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Patient> patientsBedIs(String str) {
        // TODO
        return null;
    }

    @Override
    public Predicate<Patient> patientIsUnwell() {
        // TODO
        return null;
    }
}

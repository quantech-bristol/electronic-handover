package com.quantech.service.PatientService;

import com.quantech.model.Patient;
import com.quantech.model.Doctor;
import com.quantech.model.PatientFormBackingObject;
import com.quantech.model.Ward;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.function.Predicate;

public interface PatientService {

    /**
     * Returns all patients currently stored in the repository.
     * @return A list of all patients currently stored in the repository.
     */
    List<Patient> getAllPatients();

    /**
     * Sort the given list of patients by their first name, alphabetically.
     * @param list The list of patients.
     * @return A sorted list of patients, by first name.
     */
    List<Patient> sortPatientsByFirstName(List<Patient> list);

    /**
     * Sort the given list of patients by their last name, alphabetically.
     * @param list The list of patients.
     * @return A sorted list of patients, by last name.
     */
    List<Patient> sortPatientsByLastName(List<Patient> list);

    /**
     * Finds a patient stored in the repository corresponding to a unique id.
     * @param id The id for which the patient corresponds to.
     * @return  A patient corresponding to the id if it exists, null otherwise.
     */
    Patient getPatientById(Long id);

    /**
     * Returns a patient corresponding to their hospital number.
     * @param hospNum The hospital number corresponding to the patient.
     * @return The patient corresponding to the hospital number if they exist, null otherwise.
     */
    Patient getPatientByHospitalNumber(Long hospNum);

    /**
     * Returns a patient corresponding to their hospital number.
     * @param nhsNum The NHS Number corresponding to the patient.
     * @return The patient corresponding to the hospital number if they exist, null otherwise.
     */
    Patient getPatientByNHSNumber(Long nhsNum);

    /**
     * Saves a given patient to the repository.
     * @param patient The patient to be saved into the repository.
     * @throws NullPointerException If the title, first name or last name have not been set.
     * @throws IllegalArgumentException If either the hospital number or the NHS number haven't been set.
     * @throws IllegalArgumentException If the patient's given NHS number is not valid, that is, its' checksum doesn't match.
     * @throws IllegalArgumentException If the patient's date of birth is in the future.
     * @throws org.springframework.dao.DataIntegrityViolationException If a patient with a given non-null NHS number already exists.
     * @throws org.springframework.dao.DataIntegrityViolationException If a patient with a given non-null hospital number already exists.
     */
    void savePatient(Patient patient) throws NullPointerException, IllegalArgumentException;

    /**
     * Checks if a given NHS number is valid, by checking to see if it has the correct checksum.
     * Only works on numbers with the correct length (10 digits).
     * @param nhsNumber The NHS number.
     * @return True if it is correct, false otherwise.
     */
    boolean checksumCorrect(Long nhsNumber);

    /**
     * Deletes a given patient from the repository.
     * @param patient The patient to be removed from the repository.
     */
    void deletePatient(Patient patient);

    /**
     * Deletes a given patient from the repository.
     * @param id The patient to be removed from the repository.
     */
    void deletePatient(Long id);

    /**
     * Filter list of a patients by a given predicate.
     * @param list A list of patients.
     * @param predicate A predicate to test the patients against.
     * @return A list of patients filtered by the given predicate.
     */
    List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate);

    /**
     * Filter list of a patients by a given predicate.
     * @param list A list of patients.
     * @param predicates A collection of predicates to test the patients against.
     * @return A list of patients filtered by the given predicate.
     */
    List<Patient> filterPatientsBy(List<Patient> list, Iterable<Predicate<Patient>> predicates);

    /**
     * A predicate that checks if the patient's first name begins with the given string.
     * @param str The string to compare with.
     * @return The corresponding predicate object.
     */
    Predicate<Patient> patientsFirstNameStartsWith(String str);

    /**
     * A predicate that checks if the patient's last name begins with the given string.
     * @param str The string to compare with.
     * @return The corresponding predicate object.
     */
    Predicate<Patient> patientsLastNameStartsWith(String str);

    /**
     * A predicate that checks if the patient is in a given ward.
     * @param ward The ward to compare with.
     * @return The corresponding predicate object.
     */
    Predicate<Patient> patientInWard(Ward ward);

    /**
     * A predicate that checks if the patient is on a given bed.
     * @param bed The bed to compare with.
     * @return The corresponding predicate object.
     */
    Predicate<Patient> patientsBedIs(Integer bed);

    /**
     * A predicate that checks if the patient is unwell.
     * @return The corresponding predicate object.
     */
    Predicate<Patient> patientIsUnwell();

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param patient The patient object created through the form.
     */
     public void CheckValidity(BindingResult result, PatientFormBackingObject patient);
}

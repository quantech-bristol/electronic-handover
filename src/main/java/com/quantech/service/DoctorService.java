package com.quantech.service;

import com.quantech.model.Doctor;
import com.quantech.model.Patient;
import com.quantech.model.user.UserCore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public interface DoctorService {

    /**
     * @return A list of all doctors stored in the doctor repository.
     */
    public List<Doctor> getAllDoctors();

    /**
     * Sort the given list of doctors by their last date of renewal, most recent first.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by first name.
     */
    public List<Doctor> sortDoctorsByDateRenewedMostRecentFirst(List<Doctor> list);

    /**
     * Sort the given list of doctors by their last date of renewal, most recent last.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by first name.
     */
    public List<Doctor> sortDoctorsByDateRenewedMostRecentLast(List<Doctor> list);

    /**
     * Sort the given list of doctors by their first name, alphabetically.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by first name.
     */
    public List<Doctor> sortDoctorsByFirstName(List<Doctor> list);

    /**
     * Sort the given list of doctors by their last name, alphabetically.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by last name.
     */
    public List<Doctor> sortDoctorsByLastName(List<Doctor> list);

    /**
     * Finds the doctor object corresponding to a given unique id.
     * @param user The user that has been assigned doctor privileges.
     * @return The doctor object that corresponds to the id if it exists, null if a doctor with the given id doesn't exist.
     */
    public Doctor getDoctor(UserCore user);

    /**
     * Saves the given doctor object into the repository.
     * @param doctor The doctor object to save.
     * @throws NullPointerException If the user or title has not been set, that is, is null.
     * @throws IllegalArgumentException If the user object is already associated with a user.
     * @throws IllegalArgumentException If the user object assigned hasn't got doctor permissions.
     */
    public void saveDoctor(Doctor doctor) throws NullPointerException, IllegalArgumentException;

    /**
     * Deletes the doctor with the corresponding id from the repository.
     * @param id The id corresponding to the doctor to be deleted.
     */
    public void deleteDoctor(Long id);

    /**
     * Returns a list of patients under the doctor's care.
     * @param user The user object of the given doctor.
     * @return A list of patients under the doctor's care.
     */
    public List<Patient> getPatientsUnderCareOf(UserCore user);

    /**
     * Filter list of a doctors by a given predicate.
     * @param list A list of doctors.
     * @param predicate A predicate to test the doctors against.
     * @return A list of doctors filtered by the given predicate.
     */
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Predicate<Doctor> predicate);

    /**
     * Filter list of a doctors by a given predicate.
     * @param list A list of doctors.
     * @param predicates A collection of predicates to test the doctors against.
     * @return A list of doctors filtered by the given predicate.
     */
    public List<Doctor> filterDoctorsBy(List<Doctor> list, Iterable<Predicate<Doctor>> predicates);

    /**
     * Returns a predicate that checks if a doctor's first name starts with the given string.
     * @param str The string to check.
     * @return A predicate.
     */
    public Predicate<Doctor> doctorsFirstNameStartsWith(String str);

    /**
     * Returns a predicate that checks if a doctor's last name starts with the given string.
     * @param str The string to check.
     * @return A predicate.
     */
    public Predicate<Doctor> doctorsLastNameStartsWith(String str);

}

package com.quantech.repo;

import com.quantech.model.Patient;
import com.quantech.model.user.UserCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long>, JpaRepository<Patient, Long> {

    public Patient findByHospitalNumber(Long hospNum);

    public Patient findByNHSNumber(Long nhsNum);

    public Patient findById(Long id);

    public List<Patient> findPatientsByFirstNameContainsAndLastNameContains(String firstName, String lastName);

}

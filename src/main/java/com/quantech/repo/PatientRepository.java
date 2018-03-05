package com.quantech.repo;

import com.quantech.model.Patient;
import com.quantech.model.user.UserCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long>, JpaRepository<Patient, Long> {

    Patient findByHospitalNumber(Long hospNum);

    Patient findByNHSNumber(Long nhsNum);

    Patient findById(Long id);

    List<Patient> findPatientsByFirstNameContainsAndLastNameContains(String firstName, String lastName);

}

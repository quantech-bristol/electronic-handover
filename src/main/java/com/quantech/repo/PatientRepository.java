package com.quantech.repo;

import com.quantech.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface PatientRepository extends CrudRepository<Patient, Long>, JpaRepository<Patient, Long> {

    public Patient findByHospitalNumber(Long hospNum);

    public Patient findByNHSNumber(Long nhsNum);

    public Patient findById(Long id);

}

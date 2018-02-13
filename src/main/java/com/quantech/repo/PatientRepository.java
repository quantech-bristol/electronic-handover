package com.quantech.repo;

import com.quantech.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    public Patient findByHospitalNumber(Long hospNum);

    public Patient findByNHSNumber(Long nhsNum);

    public Patient findById(Long id);
}

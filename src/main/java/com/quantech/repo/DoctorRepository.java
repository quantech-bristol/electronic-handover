package com.quantech.repo;

import com.quantech.model.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    public Doctor findById(Long id);

    public void deleteByUser_id(Long id);
}

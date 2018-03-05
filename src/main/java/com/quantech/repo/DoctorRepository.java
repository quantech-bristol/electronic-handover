package com.quantech.repo;

import com.quantech.model.Doctor;
import com.quantech.model.user.UserCore;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Doctor findById(Long id);

    Doctor findByUser_id(Long id);

    void deleteByUser_id(Long id);
}

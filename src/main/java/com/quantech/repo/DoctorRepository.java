package com.quantech.repo;

import com.quantech.model.Doctor;
import com.quantech.model.user.UserCore;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    public Doctor findById(Long id);

    public Doctor findByUser_id(Long id);

    public void deleteByUser_id(Long id);
}

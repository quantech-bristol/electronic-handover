package com.quantech.repo;

import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobRepository extends CrudRepository<Job, Long> {
    public List<Job> findByDoctor(Doctor doctor);
}

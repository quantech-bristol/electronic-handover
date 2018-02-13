package com.quantech.repo;

import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobContextRepository extends CrudRepository<JobContext, Long> {
    public List<JobContext> findByPatient(Patient patient);
}

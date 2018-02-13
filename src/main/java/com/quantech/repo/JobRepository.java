package com.quantech.repo;

import com.quantech.model.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Long> {
}

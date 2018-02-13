package com.quantech.service.JobsService;

import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import com.quantech.service.JobsService.JobsService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service("jobsService")
public class JobsServiceImpl implements JobsService {
    @Override
    public Job getJob(Long id) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllJobsOfDoctor(Doctor doctor) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllPendingFrom() {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllPendingJobsFrom(Doctor doctor) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllJobsForPatient(Patient patient) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllUncompletedJobsForPatient(Patient patient) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllCompletedJobsForPatient(Patient patient) {
        // TODO
        return null;
    }

    @Override
    public void saveJob(Job job) {
        // TODO
    }

    @Override
    public void saveJobContext(JobContext context) {
        // TODO
    }

    @Override
    public void handoverJob(Job job, Doctor doctor) {
        // TODO
    }

    @Override
    public void handoverJob(Iterable<Job> job, Doctor doctor) {
        // TODO
    }

    @Override
    public void completeJob(Job job) {
        // TODO
    }

    @Override
    public void CheckValidity(BindingResult result, Job job) {
        // TODO
    }
}

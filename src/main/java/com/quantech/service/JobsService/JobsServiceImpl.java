package com.quantech.service;

import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service("jobsService")
public class JobsServiceImpl implements JobsService {
    @Override
    public Job getJob(Long id) {
        return null;
    }

    @Override
    public List<Job> getAllJobsOfDoctor(Doctor doctor) {
        return null;
    }

    @Override
    public List<Job> getAllPendingFrom() {
        return null;
    }

    @Override
    public List<Job> getAllPendingJobsFrom(Doctor doctor) {
        return null;
    }

    @Override
    public List<Job> getAllJobsForPatient(Patient patient) {
        return null;
    }

    @Override
    public List<Job> getAllUncompletedJobsForPatient(Patient patient) {
        return null;
    }

    @Override
    public List<Job> getAllCompletedJobsForPatient(Patient patient) {
        return null;
    }

    @Override
    public void saveJob(JobContext context, Job job) {

    }

    @Override
    public void handoverJob(Job job, Doctor doctor) {

    }

    @Override
    public void handoverJob(Iterable<Job> job, Doctor doctor) {

    }

    @Override
    public void completeJob(Job job) {

    }

    @Override
    public void CheckValidity(BindingResult result, Job job) {

    }
}

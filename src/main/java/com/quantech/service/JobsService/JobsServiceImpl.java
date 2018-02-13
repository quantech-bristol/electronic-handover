package com.quantech.service.JobsService;

import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import com.quantech.repo.JobContextRepository;
import com.quantech.repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("jobsService")
public class JobsServiceImpl implements JobsService {
    @Autowired
    private JobContextRepository jobContextRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Job getJob(Long id) {
        return jobRepository.findOne(id);
    }

    @Override
    public List<Job> getAllJobsOfDoctor(Doctor doctor) {
        return jobRepository.findByDoctor(doctor);
    }

    @Override
    public List<Job> getAllPendingJobsFrom(Doctor doctor) {
        // TODO
        return null;
    }

    @Override
    public List<Job> getAllUncompletedJobsFrom(Doctor doctor) {
        return jobRepository.findByDoctor(doctor).stream()
                .filter(job -> job.getCompletionDate() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getAllJobsForPatient(Patient patient) {
        List<Job> jobs = new ArrayList<>();
        jobContextRepository.findByPatient(patient).forEach(context -> jobs.addAll(context.getJobs()));
        return jobs;
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
    public List<Job> getAllJobsOfContext(JobContext context) {
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

package com.quantech.service.JobsService;

import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import com.quantech.repo.JobContextRepository;
import com.quantech.repo.JobRepository;
import com.quantech.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("jobsService")
public class JobsServiceImpl implements JobsService {
    @Autowired
    private JobContextRepository jobContextRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PatientRepository patientRepository;

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
        List<Job> jobs = new ArrayList<>();
        jobContextRepository.findByPatient(patient).forEach(context -> jobs.addAll(context.getJobs()));
        return jobs.stream()
                .filter(job -> job.getCompletionDate() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getAllCompletedJobsForPatient(Patient patient) {
        List<Job> jobs = new ArrayList<>();
        jobContextRepository.findByPatient(patient).forEach(context -> jobs.addAll(context.getJobs()));
        return jobs.stream()
                .filter(job -> job.getCompletionDate() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getAllJobsOfContext(JobContext context) {
        return jobRepository.findByJobContext(context);
    }

    @Override
    public void saveJob(Job job) throws NullPointerException, IllegalArgumentException {
        if (job == null)
            throw new NullPointerException("Error: job to be saved has null value.");

        Object[] fields = new Object[]{job.getDescription(),
                job.getCategory(),
                job.getCreationDate(),
                job.getJobContext()};
        String[] fieldNames = new String[] {"description",
                "category",
                "creation date",
                "job context"};

        // Carry out field null checks.
        for (int i = 0; i < fields.length; i++) {
            EntityFieldHandler.nullCheck(fields[i],fieldNames[i]);
        }

        // Check if the job context is in the repository.
        if (jobContextRepository.findById(job.getJobContext().getId()) != null) {
            throw new IllegalArgumentException("Error: job context doesn't already exist in the database.");
        }
    }

    @Override
    public void saveJobContext(JobContext context) throws NullPointerException, IllegalArgumentException {
        if (context == null)
            throw new NullPointerException("Error: context to be saved has null value.");

        Object[] fields = new Object[]{context.getUnwell(),
                context.getCreationDate(),
                context.getPatient(),
                context.getWard(),
                context.getJobs()};
        String[] fieldNames = new String[] {"unwell",
                "creation date",
                "patient",
                "ward",
                "jobs"};

        // Carry out field null checks.
        for (int i = 0; i < fields.length; i++) {
            EntityFieldHandler.nullCheck(fields[i],fieldNames[i]);
        }

        // Check if the patient is in the repository.
        if (patientRepository.findById(context.getPatient().getId()) != null) {
            throw new IllegalArgumentException("Error: patient doesn't already exist in the database.");
        }
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

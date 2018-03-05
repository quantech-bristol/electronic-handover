package com.quantech.service.JobsService;

import com.quantech.model.Category;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.*;
import com.quantech.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("jobsService")
public class JobsServiceImpl implements JobsService {
    @Autowired
    private JobContextRepository jobContextRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private WardRepository wardRepository;

    @Override
    public Job getJob(Long id) {
        return jobRepository.findOne(id);
    }

    @Override
    public JobContext getJobContext(Long id) {
        return jobContextRepository.findById(id);
    }

    @Override
    public List<Job> getAllJobsOfDoctor(Doctor doctor) {
        return jobRepository.findByDoctor(doctor);
    }

    /*
    @Override
    public List<Job> getAllPendingJobsFrom(Doctor doctor) {
        // TODO
        return null;
    }
    */

    @Override
    public List<Job> getAllJobsForPatient(Patient patient) {
        List<Job> jobs = new ArrayList<>();
        jobContextRepository.findByPatient(patient).forEach(context -> jobs.addAll(context.getJobs()));
        return jobs;
    }

    @Override
    public List<JobContext> getAllJobContexts() {
        List<JobContext> jobContexts = new ArrayList<>();
        jobContextRepository.findAll().forEach(jobContexts::add);
        return jobContexts;
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
        if (jobContextRepository.findById(job.getJobContext().getId()) == null) {
            throw new IllegalArgumentException("Error: job context doesn't already exist in the database.");
        }

        // Same for doctor.
        if (job.getDoctor() != null && doctorRepository.findById(job.getDoctor().getId()) == null) {
            throw new IllegalArgumentException("Error: doctor doesn't already exist in the database.");
        }


        jobRepository.save(job);

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
        if (patientRepository.findById(context.getPatient().getId()) == null) {
            throw new IllegalArgumentException("Error: patient doesn't already exist in the database.");
        }

        // Check if the ward is in the repository.
        if (wardRepository.findById(context.getWard().getId()) == null) {
            throw new IllegalArgumentException("Error: ward doesn't already exist in the database.");
        }

        jobContextRepository.save(context);
    }

    @Override
    public void handoverJob(Job job, Doctor doctor) {
        if (job == null)
            throw new NullPointerException("Error: job cannot be null.");
        if (doctor == null)
            throw new NullPointerException("Error: doctor cannot be null.");
        if (job.getCompletionDate() == null) {
            job.setDoctor(doctor);
            this.saveJob(job);
        }
    }

    @Override
    public void handoverJobs(Iterable<Job> jobs, Doctor doctor) {
        jobs.forEach(job -> this.handoverJob(job,doctor));
    }

    @Override
    public void completeJob(Job job) {
        if (job == null)
            throw new NullPointerException("Error: job cannot have null value.");
        if (job.getCompletionDate() == null) {
            job.setCompletionDate(new Date());
            this.saveJob(job);
        }
    }

    @Override
    public List<Job> filterJobsBy(List<Job> list, Predicate<Job> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<Job> filterJobsBy(List<Job> list, Iterable<Predicate<Job>> predicates) {
        Stream<Job> stream = list.stream();
        for (Predicate<Job> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public Predicate<Job> jobIsOfCategory(Category category) {
        return job -> job.getCategory().equals(category);
    }

    @Override
    public Predicate<Job> jobWherePatientIsUnwell() {
        return job -> job.getJobContext().getUnwell();
    }

    @Override
    public Predicate<Job> jobWherePatientIsWell() {
        return job -> !job.getJobContext().getUnwell();
    }

    @Override
    public Predicate<Job> jobIsOfWard(Ward ward) {
        return job -> job.getJobContext().getWard().equals(ward);
    }

    @Override
    public Predicate<Job> jobIsComplete() {
        return job -> job.getCompletionDate() != null;
    }

    @Override
    public Predicate<Job> jobIsUncomplete() {
        return job -> job.getCompletionDate() == null;
    }

    @Override
    public List<Job> sortJobsOldestFirst(List<Job> jobs) {
        jobs.sort(Comparator.comparing(Job::getCreationDate));
        return jobs;
    }

    @Override
    public List<Job> sortJobsNewestFirst(List<Job> jobs) {
        jobs.sort((j1,j2) -> -j2.getCreationDate().compareTo(j1.getCreationDate()));
        return jobs;
    }

    @Override
    public List<JobContext> sortJobContextsByFirstName(List<JobContext> jobContexts) {
        jobContexts.sort((j1,j2) -> -j2.getPatient().getFirstName().compareTo(j1.getPatient().getFirstName()));
        return jobContexts;
    }

    @Override
    public List<JobContext> sortJobContextsByLastName(List<JobContext> jobContexts) {
        jobContexts.sort((j1,j2) -> -j2.getPatient().getLastName().compareTo(j1.getPatient().getLastName()));
        return jobContexts;
    }

    @Override
    public List<JobContext> filterJobContextsBy(List<JobContext> list, Predicate<JobContext> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<JobContext> filterJobContextsBy(List<JobContext> list, Iterable<Predicate<JobContext>> predicates) {
        Stream<JobContext> stream = list.stream();
        for (Predicate<JobContext> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<JobContext> filterJobContextsBy(List<JobContext> list, Iterable<Predicate<JobContext>> jobContextPredicates, Iterable<Predicate<Job>> jobPredicates) {
        List<JobContext> jcs = filterJobContextsBy(list,jobContextPredicates);
        for (JobContext jc : jcs) {
            jc.setJobs(filterJobsBy(jc.getJobs(),jobPredicates));
        }
        return jcs;
    }

    @Override
    public Predicate<JobContext> patientIsUnwell() {
        return (JobContext::getUnwell);
    }

    @Override
    public Predicate<JobContext> patientHasRisk(Risk risk) {
        return (j->j.getRisks().contains(risk));
    }

    @Override
    public Predicate<JobContext> patientIsInWard(Ward ward) {
        return j->j.getWard().equals(ward);
    }

    @Override
    public List<JobContext> getJobContextsUnderCareOf(Doctor doctor) {
        // TODO: Could be sped up by using a hash table.
        List<Job> js = this.getAllJobsOfDoctor(doctor);
        List<JobContext> jcs = new ArrayList<>();
        for (Job j : js) {
            JobContext jc = j.getJobContext();
            if (!jcs.contains(jc))
                jcs.add(jc);
        }
        jcs = this.sortJobContextsByFirstName(jcs);
        return jcs;
    }

    @Override
    public void CheckValidity(BindingResult result, Job job) {
        // TODO
    }

}

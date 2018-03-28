package com.quantech.service.JobsService;

import com.quantech.model.Category;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.model.*;
import com.quantech.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
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
        Set<Predicate<Job>> category = new HashSet<>();
        Set<Predicate<Job>> others = new HashSet<>();
        Stream<Job> stream = list.stream();
        for (Predicate<Job> predicate : predicates) {
            if (predicate.getClass().isInstance(new EntityPredicate<Job>()) && ((EntityPredicate<Job>) predicate).field.equals("category"))
                category.add(predicate);
            else
                others.add(predicate);
        }
        boolean categoryFilter = false;
        if (!category.isEmpty())
            categoryFilter = true;

        // Or-ing all category predicates together.
        Predicate<Job> cp = (categoryFilter) ? p->false : p->true;
        for (Predicate<Job> p : category)
            cp = cp.or(p);
        stream = stream.filter(cp);
        for (Predicate<Job> p : others)
            stream = stream.filter(p);

        return stream.collect(Collectors.toList());
    }

    @Override
    public EntityPredicate<Job> jobIsOfCategory(Category category) {
        return new EntityPredicate<>(job -> job.getCategory().equals(category),"category");
    }

    @Override
    public EntityPredicate<Job> jobWherePatientIsUnwell() {
        return new EntityPredicate<>(job -> job.getJobContext().getUnwell(),"unwell");
    }

    @Override
    public EntityPredicate<Job> jobWherePatientIsWell() {
        return new EntityPredicate<>(job -> !job.getJobContext().getUnwell(),"unwell");
    }

    @Override
    public EntityPredicate<Job> jobIsOfWard(Ward ward) {
        return new EntityPredicate<>(job -> job.getJobContext().getWard().equals(ward),"ward");
    }

    @Override
    public EntityPredicate<Job> jobIsComplete() {
        return new EntityPredicate<>(job -> job.getCompletionDate() != null,"complete");
    }

    @Override
    public EntityPredicate<Job> jobIsUncomplete() {
        return new EntityPredicate<>(job -> job.getCompletionDate() == null,"complete");
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
    public List<JobContext> sortJobContextsByCreationDate(List<JobContext> jobContexts) {
        jobContexts.sort((j1,j2) -> -j1.getCreationDate().compareTo(j2.getCreationDate()));
        return jobContexts;
    }

    @Override
    public List<JobContext> filterJobContextsBy(List<JobContext> list, Predicate<JobContext> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<JobContext> filterJobContextsBy(List<JobContext> list, Iterable<Predicate<JobContext>> predicates) {
        Set<Predicate<JobContext>> risk = new HashSet<>();
        Set<Predicate<JobContext>> ward = new HashSet<>();
        Set<Predicate<JobContext>> others = new HashSet<>();

        Stream<JobContext> stream = list.stream();
        for (Predicate<JobContext> predicate : predicates) {
            if (predicate.getClass().isInstance(new EntityPredicate<JobContext>()) && ((EntityPredicate<JobContext>) predicate).field.equals("risk"))
                risk.add(predicate);
            else if (predicate.getClass().isInstance(new EntityPredicate<JobContext>()) && ((EntityPredicate<JobContext>) predicate).field.equals("ward"))
                ward.add(predicate);
            else
                others.add(predicate);
        }
        boolean wardFilter = false;
        if (!ward.isEmpty())
            wardFilter = true;
        boolean riskFilter = false;
        if (!risk.isEmpty())
            riskFilter = true;

        // Or-ing all category predicates together.
        Predicate<JobContext> wp = (wardFilter) ? p->false : p->true;
        Predicate<JobContext> rp = (riskFilter) ? p->false : p->true;
        for (Predicate<JobContext> p : ward)
            wp = wp.or(p);
        for (Predicate<JobContext> p : risk)
            rp = rp.or(p);
        for (Predicate<JobContext> p : others)
            stream = stream.filter(p);
        stream = stream.filter(wp);
        stream = stream.filter(rp);

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
    public EntityPredicate<JobContext> patientIsUnwell() {
        return new EntityPredicate<> (JobContext::getUnwell,"unwell");
    }

    @Override
    public EntityPredicate<JobContext> patientHasRisk(Risk risk) {
        return new EntityPredicate<>(j->j.getRisks().contains(risk),"risk");
    }

    @Override
    public EntityPredicate<JobContext> patientIsInWard(Ward ward) {
        return new EntityPredicate<>(j->j.getWard().equals(ward),"ward");
    }

    @Override
    public List<JobContext> getJobContextsUnderCareOf(Doctor doctor) {
        // TODO: Could be sped up by using a hash table.
        List<Job> js = this.getAllJobsOfDoctor(doctor);
        List<JobContext> jcs = new ArrayList<>();
        for (Job j : js) {
            JobContext jc = j.getJobContext();
            if (!jcs.contains(jc) && j.getCompletionDate() == null)
                jcs.add(jc);
        }
        jcs = this.sortJobContextsByFirstName(jcs);
        return jcs;
    }

    @Override
    public void CheckJobValidity(BindingResult result, JobFormBackingObject job) {
        if (job.getCategoryId() == null)
            result.rejectValue("categoryId","category.job","Please select a category to use.");
        if (job.getDoctorId() == null)
            result.rejectValue("doctorId","category.doctor","Please select a doctor to send to.");
    }

    @Override
    public void CheckJobContextFormValidity(BindingResult result, JobContextFormBackingObject jobContext) {
        if (jobContext.getWard() == null)
            result.rejectValue("ward","ward.jobContext","Please select a ward to use.");
    }

    class EntityPredicate<T> implements Predicate<T> {
        Predicate<T> p;
        String field;

        EntityPredicate(){}

        EntityPredicate(Predicate<T> p, String field) {
            this.p = p;
            this.field = field;
        }

        @Override
        public boolean test(T t) {
            return p.test(t);
        }
    }
}

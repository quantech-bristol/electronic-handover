package com.quantech.service.JobsService;

import com.quantech.misc.Category;
import com.quantech.model.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.function.Predicate;

@Service
public interface JobsService {

    /**
     * Returns the handover stored in the repository corresponding to the unique id.
     * @param id The id to be used to identify the job in the repository.
     * @return The job corresponding to the id if it exists, or null otherwise.
     */
    public Job getJob(Long id);

    /**
     * Finds a list of all jobs that are currently the responsibility of a certain doctor.
     * @param doctor The doctor to which the job was sent.
     * @return A list of jobs that the doctor is responsible for.
     */
    public List<Job> getAllJobsOfDoctor(Doctor doctor);

    /**
     * Get all jobs that a certain doctor has sent that has not yet been accepted.
     * @param doctor Doctor that has sent a handover.
     * @return A list of jobs that have yet to be picked up.
     */
    // public List<Job> getAllPendingJobsFrom(Doctor doctor);

    /**
     * Get all jobs that a certain doctor has sent that has not yet been completed.
     * @param doctor Doctor that is responsible for a job.
     * @return A list of jobs that have yet to be completed.
     */
    public List<Job> getAllUncompletedJobsFrom(Doctor doctor);

    /**
     * Finds a list of jobs that concern a specific patient.
     * @param patient The patient for which the jobs concern.
     * @return A list of jobs that involve the input patient.
     */
    public List<Job> getAllJobsForPatient(Patient patient);

    /**
     * Finds a list of jobs that concern a specific patient that are incomplete.
     * @param patient The patient for which the jobs concern.
     * @return A list of incomplete jobs that involve the input patient.
     */
    public List<Job> getAllUncompletedJobsForPatient(Patient patient);

    /**
     * Finds a list of jobs that concern a specific patient that are complete.
     * @param patient The patient for which the jobs concern.
     * @return A list of complete jobs that involve the input patient.
     */
    public List<Job> getAllCompletedJobsForPatient(Patient patient);

    /**
     * Finds a list of all jobs corresponding to a certain context.
     * @param context The context to use in the search.
     * @return A list of jobs that use the context.
     */
    public List<Job> getAllJobsOfContext(JobContext context);

    /**
     * Saves the given job into the repository.
     * @param job The handover to be saved.
     * @throws NullPointerException If the job has a null description, category, creation date or job context.
     * @throws IllegalArgumentException If the corresponding job context isn't in the repository, or is invalid.
     */
    public void saveJob(Job job) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given job context into the repository.
     * @param context The new job context.
     * @throws NullPointerException If the "unwell", creation date, patient, ward or jobs field is null.
     * @throws IllegalArgumentException If the patient isn't already in the database.
     */
    public void saveJobContext(JobContext context);

    /**
     * Send a handover using a given job, making another doctor responsible for that job.
     * @param job The job to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJob(Job job, Doctor doctor);

    /**
     * Send a handover using a given iterable of jobs, making that doctor responsible for those jobs.
     * @param job The jobs to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJobs(Iterable<Job> job, Doctor doctor);

    /**
     * Marks a given job as complete.
     * @param job The job to complete.
     */
    public void completeJob(Job job);

    /**
     * Filter list of a jobs by a given predicate.
     * @param list A list of jobs.
     * @param predicate A predicate to test the jobs against.
     * @return A list of jobs filtered by the given predicate.
     */
    public List<Job> filterJobsBy(List<Job> list, Predicate<Job> predicate);

    /**
     * Filter list of a jobs by a given predicate.
     * @param list A list of jobs.
     * @param predicates A collection of predicates to test the jobs against, conjunctive.
     * @return A list of jobs filtered by the given predicate.
     */
    public List<Job> filterJobsBy(List<Job> list, Iterable<Predicate<Job>> predicates);

    /**
     * A predicate that checks if a job has a specific category.
     * @param category The category to check.
     * @return The corresponding predicate object.
     */
    public Predicate<Job> jobIsOfCategory(Category category);

    /**
     * A predicate that checks if a job's patient is unwell.
     * @return The corresponding predicate object.
     */
    public Predicate<Job> jobWherePatientIsUnwell();

    /**
     * A predicate that checks if a job's patient is well.
     * @return The corresponding predicate object.
     */
    public Predicate<Job> jobWherePatientIsWell();

    /**
     * A predicate that checks if a job is to be carried out in a certain ward.
     * @param ward A ward.
     * @return The corresponding predicate object.
     */
    public Predicate<Job> jobIsOfWard(Ward ward);

    /**
     * Sorts the given list of jobs into order of creation date, oldest first.
     * @param jobs List of jobs to be sorted.
     * @return Sorted list of jobs.
     */
    public List<Job> sortJobsOldestFirst(List<Job> jobs);

    /**
     * Sorts the given list of jobs into order of creation date, newest first.
     * @param jobs List of jobs to be sorted.
     * @return Sorted list of jobs.
     */
    public List<Job> sortJobsNewestFirst(List<Job> jobs);

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param job The job object created through the form.
     */
    public void CheckValidity(BindingResult result, Job job);
}

package com.quantech.service.JobsService;

import com.quantech.model.Category;
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
    Job getJob(Long id);

    /**
     * Returns the handover stored in the repository corresponding to the unique id.
     * @param id The id to be used to identify the job context in the repository.
     * @return The job context corresponding to the id if it exists, or null otherwise.
     */
    JobContext getJobContext(Long id);

    /**
     * Finds a list of all jobs that are currently the responsibility of a certain doctor.
     * @param doctor The doctor to which the job was sent.
     * @return A list of jobs that the doctor is responsible for.
     */
    List<Job> getAllJobsOfDoctor(Doctor doctor);

    /**
     * Get all jobs that a certain doctor has sent that has not yet been accepted.
     * @param doctor Doctor that has sent a handover.
     * @return A list of jobs that have yet to be picked up.
     */
    // public List<Job> getAllPendingJobsFrom(Doctor doctor);

    /**
     * Finds a list of jobs that concern a specific patient.
     * @param patient The patient for which the jobs concern.
     * @return A list of jobs that involve the input patient.
     */
    List<Job> getAllJobsForPatient(Patient patient);

    /**
     * Finds a list of all jobs contexts.
     * @return A list of all job contexts.
     */
    List<JobContext> getAllJobContexts();

    /**
     * Finds a list of all jobs corresponding to a certain context.
     * @param context The context to use in the search.
     * @return A list of jobs that use the context.
     */
    List<Job> getAllJobsOfContext(JobContext context);

    /**
     * Saves the given job into the repository.
     * @param job The handover to be saved.
     * @throws NullPointerException If the job has a null description, category, creation date or job context.
     * @throws IllegalArgumentException If the corresponding job context or doctor isn't in the repository, or is invalid.
     */
    void saveJob(Job job) throws NullPointerException, IllegalArgumentException;

    /**
     * Saves the given job context into the repository.
     * @param context The new job context.
     * @throws NullPointerException If the "unwell", creation date, patient, ward or jobs field is null.
     * @throws IllegalArgumentException If the patient isn't already in the database.
     */
    void saveJobContext(JobContext context);

    /**
     * Send a handover using a given job, making another doctor responsible for that job.
     * A completed job cannot be handed over.
     * @param job The job to send.
     * @param doctor The doctor to handover to.
     */
    void handoverJob(Job job, Doctor doctor);

    /**
     * Send a handover using a given iterable of jobs, making that doctor responsible for those jobs.
     * @param job The jobs to send.
     * @param doctor The doctor to handover to.
     */
    void handoverJobs(Iterable<Job> job, Doctor doctor);

    /**
     * Marks a given job as complete.
     * @param job The job to complete.
     */
    void completeJob(Job job);

    /**
     * Sorts a list of job contexts by the patient's first name.
     * @param jobContexts List of job contexts.
     * @return A list of job contexts.
     */
    List<JobContext> sortJobContextsByFirstName(List<JobContext> jobContexts);

    /**
     * Sorts a list of job contexts by the patient's last name.
     * @param jobContexts List of job contexts.
     * @return A list of job contexts.
     */
    List<JobContext> sortJobContextsByLastName(List<JobContext> jobContexts);

    /**
     * Sorts a list of job contexts by the job context creation date.
     * @param jobContexts List of job contexts.
     * @return A list of job contexts.
     */
    List<JobContext> sortJobContextsByCreationDate(List<JobContext> jobContexts);

    /**
     * Filter list of job contexts by a given predicate.
     * @param list A list of jobs contexts.
     * @param predicate A predicate to test the job contexts against.
     * @return A list of job contexts filtered by the given predicate.
     */
    List<JobContext> filterJobContextsBy(List<JobContext> list, Predicate<JobContext> predicate);

    /**
     * Filter list of a job contexts by a given predicate.
     * @param list A list of job contexts.
     * @param predicates A collection of predicates to test the job contexts against, conjunctive.
     * @return A list of job contexts filtered by the given predicate.
     */
    List<JobContext> filterJobContextsBy(List<JobContext> list, Iterable<Predicate<JobContext>> predicates);

    /**
     * Filter list of a job contexts and their corresponding jobs by a given predicate.
     * @param list A list of job contexts.
     * @param jobContextPredicates A collection of predicates to test the job contexts against, conjunctive.
     * @param jobPredicates A collection of predicates to test the jobs against, conjunctive.
     * @return A list of job contexts filtered by the given predicate.
     */
    List<JobContext> filterJobContextsBy(List<JobContext> list, Iterable<Predicate<JobContext>> jobContextPredicates, Iterable<Predicate<Job>> jobPredicates);


    /**
     * Predicate that checks if the patient is unwell.
     * @return A predicate.
     */
    Predicate<JobContext> patientIsUnwell();

    /**
     * Predicate that checks if a patient has a given risk.
     * @param risk risk.
     * @return A predicate.
     */
    Predicate<JobContext> patientHasRisk(Risk risk);

    /**
     * Predicate that checks if a patient is in a given ward.
     * @param ward ward.
     * @return predicate.
     */
    Predicate<JobContext> patientIsInWard(Ward ward);

    /**
     * Filter list of a jobs by a given predicate.
     * @param list A list of jobs.
     * @param predicate A predicate to test the jobs against.
     * @return A list of jobs filtered by the given predicate.
     */
    List<Job> filterJobsBy(List<Job> list, Predicate<Job> predicate);

    /**
     * Filter list of a jobs by a given predicate.
     * @param list A list of jobs.
     * @param predicates A collection of predicates to test the jobs against, conjunctive.
     * @return A list of jobs filtered by the given predicate.
     */
    List<Job> filterJobsBy(List<Job> list, Iterable<Predicate<Job>> predicates);

    /**
     * A predicate that checks if a job has a specific category.
     * @param category The category to check.
     * @return The corresponding predicate object.
     */
    Predicate<Job> jobIsOfCategory(Category category);

    /**
     * A predicate that checks if a job's patient is unwell.
     * @return The corresponding predicate object.
     */
    Predicate<Job> jobWherePatientIsUnwell();

    /**
     * A predicate that checks if a job's patient is well.
     * @return The corresponding predicate object.
     */
    Predicate<Job> jobWherePatientIsWell();

    /**
     * A predicate that checks if a job is to be carried out in a certain ward.
     * @param ward A ward.
     * @return The corresponding predicate object.
     */
    Predicate<Job> jobIsOfWard(Ward ward);

    /**
     * A predicate that checks if a given job is complete.
     * @return A predicate.
     */
    Predicate<Job> jobIsComplete();

    /**
     * A predicate that checks if a given job is incomplete.
     * @return A predicate.
     */
    Predicate<Job> jobIsUncomplete();

    /**
     * Sorts the given list of jobs into order of creation date, oldest first.
     * @param jobs List of jobs to be sorted.
     * @return Sorted list of jobs.
     */
    List<Job> sortJobsOldestFirst(List<Job> jobs);

    /**
     * Sorts the given list of jobs into order of creation date, newest first.
     * @param jobs List of jobs to be sorted.
     * @return Sorted list of jobs.
     */
    List<Job> sortJobsNewestFirst(List<Job> jobs);

    /**
     * Returns a list of job contexts for patients under the care of a given doctor.
     * @param doctor The doctor to get job contexts for.
     * @return A list of job contexts.
     */
    List<JobContext> getJobContextsUnderCareOf(Doctor doctor);


    /**
     * Checks the validity of a job's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param job The job object created through the form.
     */
    void CheckJobValidity(BindingResult result, JobFormBackingObject job);

    /**
     * Checks the validity of a job context's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param jobContext context The job object created through the form.
     */
    void CheckJobContextFormValidity(BindingResult result, JobContextFormBackingObject jobContext);
}

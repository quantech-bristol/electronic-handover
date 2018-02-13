package com.quantech.service.JobsService;

import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import jdk.nashorn.internal.scripts.JO;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

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
     * Returns a list of jobs that still have not been accepted by their recipient doctors.
     * @return A list of pending jobs.
     */
    public List<Job> getAllPendingFrom();

    /**
     * Get all jobs that a certain doctor has sent that has not yet been accepted.
     * @param doctor Doctor that has sent a handover.
     * @return A list of jobs that have yet to be picked up.
     */
    public List<Job> getAllPendingJobsFrom(Doctor doctor);

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
     * @throws NullPointerException If the "unwell", creation date, patient, ward or jobs field.
     */
    public void saveJobContext(JobContext context);

    /**
     * Send a handover using a given job.
     * @param job The job to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJob(Job job, Doctor doctor);

    /**
     * Send a handover using a given iterable of jobs.
     * @param job The jobs to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJob(Iterable<Job> job, Doctor doctor);

    /**
     * Marks a given job as complete.
     * @param job The job to complete.
     */
    public void completeJob(Job job);

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param job The job object created through the form.
     */
    public void CheckValidity(BindingResult result, Job job);
}

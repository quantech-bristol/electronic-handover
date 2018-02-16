package com.quantech.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Job {

    @Id
    @GeneratedValue
    private Long id;

    // A description of the task at hand
    @NotNull
    @Type(type="text")
    private String description;

    @NotNull
    @ManyToOne
    private Category category;

    // YYYY-MM-DD HH:MM:SS
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date creationDate;

    // YYYY-MM-DD HH:MM:SS
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;

    @NotNull
    @ManyToOne
    private JobContext jobContext;

    @NotNull
    @ManyToOne
    private Doctor doctor;

    public Job() {
    }

    public Job(String description, Category category, Date creationDate, Date completionDate, JobContext jobContext, Doctor doctor) {
        this.description = description;
        this.category = category;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.jobContext = jobContext;
        this.doctor = doctor;
    }

    /**
     * ID getter.
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter.
     * @param id Job id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Description getter.
     * @return Job description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     * @param description Job description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Creation date getter.
     * @return Job creation date.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Creation date setter.
     * @param creationDate Job creation date.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Completion date getter.
     * @return Job completion date (null if still ongoing)
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    /**
     * Completion date setter.
     * @param completionDate Job's completion date
     */
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    /**
     * Job context getter.
     * @return The job's context.
     */
    public JobContext getJobContext() {
        return jobContext;
    }

    /**
     * Job context setter.
     * @param jobContext Job context
     */
    public void setJobContext(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    /**
     * Doctor getter
     * @return The doctor responsible for the job.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Doctor setter.
     * @param doctor The new doctor responsible for the job.
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Category setter
     * @param category Category to set the job with.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Category getter.
     * @return The category that the job is classed under.
     */
    public Category getCategory() {
        return category;
    }
}

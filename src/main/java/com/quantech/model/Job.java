package com.quantech.model;

import com.quantech.misc.Category;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Category> categories;

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
        this.categories = new ArrayList<>();
    }

    public Job(String description, Date creationDate, Date completionDate, JobContext jobContext, Doctor doctor) {
        this.description = description;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.jobContext = jobContext;
        this.doctor = doctor;
        this.categories = new ArrayList<>();
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
     * Categories setter
     * @param categories A list of categories to set the job with.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Categories getter.
     * @return The categories that the job is classed under.
     */
    public List<Category> getCategories() {
        return this.categories;
    }
}

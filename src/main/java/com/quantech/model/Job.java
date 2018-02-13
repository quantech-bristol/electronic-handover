package com.quantech.model;

import com.quantech.misc.Category;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Job {

    @Id
    @GeneratedValue
    private Long id;

    // description of the task at hand
    @NotNull
    @Type(type="text")
    private String description;

    @NotNull
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

    public Job() { }

    public Job(String description, Category category, Date creationDate, Date completionDate, JobContext jobContext, Doctor doctor) {
        this.description = description;
        this.category = category;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.jobContext = jobContext;
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public JobContext getJobContext() {
        return jobContext;
    }

    public void setJobContext(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

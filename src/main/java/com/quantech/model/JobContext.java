package com.quantech.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class JobContext {

    @Id
    @GeneratedValue
    private Long id;

    // diagnosis, clinical issues, medical history relevant to jobs
    @Type(type="text")
    private String clinicalDetails;

    @NotNull
    private Boolean unwell;

    // YYYY-MM-DD HH:MM:SS
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date creationDate;

    @NotNull
    @ManyToOne
    private Patient patient;

    @NotNull
    @OneToMany(mappedBy = "jobContext")
    private List<Job> jobs;

    public JobContext() { this.jobs = new ArrayList<>(); }

    public JobContext(String clinicalDetails, Boolean unwell, Date creationDate, Patient patient, List<Job> jobs) {
        this.clinicalDetails = clinicalDetails;
        this.unwell = unwell;
        this.creationDate = creationDate;
        this.patient = patient;
        this.jobs = jobs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClinicalDetails() {
        return clinicalDetails;
    }

    public void setClinicalDetails(String clinicalDetails) {
        this.clinicalDetails = clinicalDetails;
    }

    public Boolean getUnwell() {
        return unwell;
    }

    public void setUnwell(Boolean unwell) {
        this.unwell = unwell;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}

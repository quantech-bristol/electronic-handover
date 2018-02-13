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
    @Column(name="ID")
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

    private Integer bed;

    @NotNull
    @ManyToOne
    private Ward ward;

    @NotNull
    @OneToMany(mappedBy = "jobContext")
    private List<Job> jobs;

    @ManyToMany
    @JoinTable(name="risk_joiner",joinColumns=@JoinColumn(name="RISK_ID", referencedColumnName="ID"),inverseJoinColumns=@JoinColumn(name="JOBC_ID", referencedColumnName="ID"))
    private List<Risk> risks;

    public JobContext() {
        this.jobs = new ArrayList<>();
        this.risks = new ArrayList<>();
    }

    public JobContext(String clinicalDetails, Boolean unwell, Date creationDate, Patient patient, Integer bed, Ward ward, List<Job> jobs, List<Risk> risks) {
        this.clinicalDetails = clinicalDetails;
        this.unwell = unwell;
        this.creationDate = creationDate;
        this.patient = patient;
        this.bed = bed;
        this.ward = ward;
        this.jobs = jobs;
        this.risks = risks;
    }

    /**
     * ID getter.
     * @return Context ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter.
     * @param id ID of context.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Clinical details getter.
     * @return The clinical details of the context.
     */
    public String getClinicalDetails() {
        return clinicalDetails;
    }

    /**
     * Clinical details setter.
     * @param clinicalDetails Job context details.
     */
    public void setClinicalDetails(String clinicalDetails) {
        this.clinicalDetails = clinicalDetails;
    }

    /**
     * Unwell getter.
     * @return True if the patient is unwell, false otherwise.
     */
    public Boolean getUnwell() {
        return unwell;
    }

    /**
     * Unwell setter.
     * @param unwell Whether the corresponding patient is well.
     */
    public void setUnwell(Boolean unwell) {
        this.unwell = unwell;
    }

    /**
     * Creation date getter.
     * @return The job context creation date.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Creation date setter.
     * @param creationDate The job context creation date.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Patient getter.
     * @return The patient that belongs to the context.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Patient setter.
     * @param patient The patient for which the context holds.
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Jobs getter.
     * @return The jobs with the given context.
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * Jobs setter.
     * @param jobs The jobs with the given context.
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Integer getBed() {
        return bed;
    }

    public void setBed(Integer bed) {
        this.bed = bed;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }
}

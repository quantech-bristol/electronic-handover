package com.quantech.model;

import com.quantech.model.user.Title;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private Long hospitalNumber;

    // optional but unique ??
    @Column(unique = true)
    private Long nHSNumber;

    private Integer bed;

    @ManyToOne
    private Ward ward;

    @OneToMany(mappedBy = "patient")
    private List<JobContext> jobContexts;

    public Patient() { this.jobContexts = new ArrayList<>(); }

    public Patient(Title title, String firstName, String lastName, Long hospitalNumber, Long nHSNumber, Integer bed, Ward ward, List<JobContext> jobContexts) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hospitalNumber = hospitalNumber;
        this.nHSNumber = nHSNumber;
        this.bed = bed;
        this.ward = ward;
        this.jobContexts = jobContexts;
    }

    /**
     * ID getter.
     * @return Patient ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter
     * @param id Patient ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Title getter.
     * @return Patient title.
     */
    public Title getTitle() {
        return title;
    }

    /**
     * Title setter.
     * @param title Patient title
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * First name getter.
     * @return Patient first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * First name setter.
     * @param firstName Patient first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Last name getter.
     * @return Patient last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Last name setter.
     * @param lastName Patient last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Hospital number getter.
     *
     * @return The patient's hospital number if it exists, null otherwise.
     */
    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    /**
     * Hospital number setter.
     *
     * @param hospitalNumber The hospital number to set the patient up with.
     */
    public void setHospitalNumber(Long hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    /**
     * NHS number getter.
     *
     * @return The patient's NHS number if it is known, null otherwise.
     */
    public Long getNHSNumber() {
        return nHSNumber;
    }

    /**
     * Sets the NHS number of the patient.
     *
     * @param NHSNumber The NHS number to provide the patient.
     */
    public void setNHSNumber(Long NHSNumber) {
        this.nHSNumber = nHSNumber;
    }

    /**
     * Bed getter
     * @return The bed that the patient is currently staying in; null otherwise.
     */
    public Integer getBed() {
        return bed;
    }

    /**
     * Bed setter.
     * @param bed The bed to assign the patient to.
     */
    public void setBed(Integer bed) {
        this.bed = bed;
    }

    /**
     * Ward getter.
     * @return The ward that the patient is currently staying in; null if they are not currently in a ward.
     */
    public Ward getWard() {
        return ward;
    }

    /**
     * Ward setter.
     * @param ward The ward to currently assign the patient to.
     */
    public void setWard(Ward ward) {
        this.ward = ward;
    }

    /**
     * Job contexts getter.
     * @return Patient job contexts.
     */
    public List<JobContext> getJobContexts() {
        return jobContexts;
    }

    /**
     * Job contexts setter.
     * @param jobContexts Patient job contexts.
     */
    public void setJobContexts(List<JobContext> jobContexts) {
        this.jobContexts = jobContexts;
    }
}


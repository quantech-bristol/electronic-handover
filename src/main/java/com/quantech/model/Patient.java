package com.quantech.model;

import com.quantech.model.user.Title;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


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

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthDate;

    @Column(unique = true,nullable = true)
    private Long hospitalNumber;

    // optional but unique ??
    @Column(unique = true,nullable = true)
    private Long nHSNumber;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<JobContext> jobContexts;

    public Patient() { this.jobContexts = new ArrayList<>(); }

    public Patient(Title title, String firstName, String lastName, Date birthDate, Long hospitalNumber, Long nHSNumber, List<JobContext> jobContexts) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.hospitalNumber = hospitalNumber;
        this.nHSNumber = nHSNumber;
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
     * Birth date getter.
     * @return Patient birth date.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Birth date setter.
     * @param birthDate Patient birth date.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
     * @param nHSNumber The NHS number to provide the patient.
     */
    public void setNHSNumber(Long nHSNumber) {
        this.nHSNumber = nHSNumber;
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

    /**
     * toString full name only
     * @return string combining title, first name and surname.
     */
    public String getFullName() {
        return this.title + ". " + this.firstName + " " + this.lastName;
    }
}


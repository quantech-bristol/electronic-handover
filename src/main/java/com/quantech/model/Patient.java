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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(Long hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public Long getnHSNumber() {
        return nHSNumber;
    }

    public void setnHSNumber(Long nHSNumber) {
        this.nHSNumber = nHSNumber;
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

    public List<JobContext> getJobContexts() {
        return jobContexts;
    }

    public void setJobContexts(List<JobContext> jobContexts) {
        this.jobContexts = jobContexts;
    }
}


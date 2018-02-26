package com.quantech.model;

import com.quantech.model.user.Title;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PatientFormBackingObject {

    private Long id;

    private Title title;

    private String firstName;

    private String lastName;

    private Integer day;

    private Integer month;

    private Integer year;

    private Long hospitalNumber;

    private Long nHSNumber;

    private List<JobContext> jobContexts;

    public PatientFormBackingObject() {
        this.jobContexts = new ArrayList<>();
    }

    public PatientFormBackingObject(Long id, Title title, String firstName, String lastName, Integer day, Integer month, Integer year, Long hospitalNumber, Long nHSNumber, List<JobContext> jobContexts) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hospitalNumber = hospitalNumber;
        this.nHSNumber = nHSNumber;
        this.jobContexts = jobContexts;
    }

    public PatientFormBackingObject(Title title, String firstName, String lastName, Integer day, Integer month, Integer year, Long hospitalNumber, Long nHSNumber) {

        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hospitalNumber = hospitalNumber;
        this.nHSNumber = nHSNumber;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public List<JobContext> getJobContexts() {
        return jobContexts;
    }

    public void setJobContexts(List<JobContext> jobContexts) {
        this.jobContexts = jobContexts;
    }

    public Patient toPatient() {
        LocalDate birthDate = LocalDate.of(this.year, this.month, this.day);
        Patient patient = new Patient(this.title, this.firstName, this.lastName, birthDate, this.hospitalNumber, this.nHSNumber, this.jobContexts);
        return patient;
    }
}

package com.quantech.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Risk {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String acronym;

    @ManyToMany(mappedBy = "risks")
    private List<JobContext> jobContexts;

    public Risk() {
        this.jobContexts = new ArrayList<>();
    }

    public Risk(Long id, String name, String acronym, List<JobContext> jobContexts) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.jobContexts = jobContexts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public List<JobContext> getJobContexts() {
        return jobContexts;
    }

    public void setJobContexts(List<JobContext> jobContexts) {
        this.jobContexts = jobContexts;
    }
}

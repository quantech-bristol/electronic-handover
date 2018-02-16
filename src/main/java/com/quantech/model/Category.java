package com.quantech.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    private Long id;

    @NotNull
    private String summary;

    @OneToMany(mappedBy = "category")
    private List<Job> jobs;

    public Category() { this.jobs = new ArrayList<>();}

    public Category(Long id, String summary, List<Job> jobs) {
        this.id = id;
        this.summary = summary;
        this.jobs = jobs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}

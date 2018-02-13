package com.quantech.model;

import com.quantech.model.user.UserCore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "doctor")
    private List<Job> jobs;

    @OneToOne(fetch = FetchType.LAZY) // Matches ID with UserCore
    private UserCore user;

    public Doctor() { this.jobs = new ArrayList<>(); }

    public Doctor(List<Job> jobs, UserCore user) {
        this.jobs = jobs;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public UserCore getUser() {
        return user;
    }

    public void setUser(UserCore user) {
        this.user = user;
    }
}

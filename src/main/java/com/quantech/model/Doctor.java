package com.quantech.model;

import com.quantech.model.user.UserCore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // Matches ID with UserCore
    @MapsId
    private UserCore user;

    @OneToMany(mappedBy = "doctor")
    private List<Job> jobs;

    public Doctor() { this.jobs = new ArrayList<>(); }

    public Doctor(UserCore user) {
        this.user = user;
        this.jobs = new ArrayList<>();
    }

    public Doctor(List<Job> jobs, UserCore user) {
        this.jobs = jobs;
        this.user = user;
    }

    /**
     * ID getter.
     * @return The doctor's, i.e. corresponding UserCore's, ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter
     * @param id The ID to provide the doctor with.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Jobs getter.
     * @return A list of jobs that the doctor is responsible for.
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * Set the list of jobs that a doctor is assigned to.
     * @param jobs The new list of jobs to set the doctor up with.
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    /**
     * UserCore getter.
     * @return The doctor's corresponding UserCore object.
     */
    public UserCore getUser() {
        return user;
    }

    /**
     * Set the user that corresponds to the doctor object.
     * @param user The user to set
     */
    public void setUser(UserCore user) {
        this.user = user;
    }

    /**
     * First name getter.
     * @return Doctor's first name.
     */
    public String getFirstName() {
        return this.user.getFirstName();
    }

    /**
     * Last name getter.
     * @return Doctor's last name.
     */
    public String getLastName() {
        return this.user.getLastName();
    }
}

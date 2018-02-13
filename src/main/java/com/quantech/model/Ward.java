package com.quantech.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ward {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "ward")
    private List<JobContext> jobContexts;

    public Ward() { this.jobContexts = new ArrayList<>(); }

    public Ward(String name) {
        this.name = name;
    }

    /**
     * ID getter.
     * @return ID of ward.
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter.
     * @param id ID of ward.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Name getter.
     * @return Name of ward.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param name Name of ward.
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<JobContext> getJobContexts() {
        return jobContexts;
    }

    public void setJobContexts(List<JobContext> jobContexts) {
        this.jobContexts = jobContexts;
    }
}

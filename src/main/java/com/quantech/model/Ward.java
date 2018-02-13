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
    private List<Patient> patients;

    public Ward() { this.patients = new ArrayList<>(); }

    public Ward(String name, List<Patient> patients) {
        this.name = name;
        this.patients = patients;
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

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}

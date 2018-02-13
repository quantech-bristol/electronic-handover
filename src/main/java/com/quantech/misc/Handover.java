package com.quantech.misc;

import com.quantech.model.Doctor;
import com.quantech.model.Job;

// object used for collecting relevant objects in the handover page
public class Handover {

    private Job job;

    private Doctor sender;

    private Doctor recipient;

    private String notes;

    public Handover() {}

    public Handover(Job job, Doctor sender, Doctor recipient, String notes) {
        this.job = job;
        this.sender = sender;
        this.recipient = recipient;
        this.notes = notes;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Doctor getSender() {
        return sender;
    }

    public void setSender(Doctor sender) {
        this.sender = sender;
    }

    public Doctor getRecipient() {
        return recipient;
    }

    public void setRecipient(Doctor recipient) {
        this.recipient = recipient;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

package com.quantech.model;

import com.quantech.model.user.Title;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HandoverFormBackingObject {

    @NotNull
    private Boolean newPatient;

    private Patient existingPatient;

    private Title updatedTitle;

    private String updatedFirstName;

    private String updatedLastName;

    @NotNull
    private Boolean newJobContext;

    private String updatedClinicalDetails;

    private Boolean updatedUnwell;

    private Integer updatedBed;

    private Ward updatedWard;

    private List<Risk> updatedRisks;

    public Boolean getNewPatient() { return newPatient; }

    public void setNewPatient(Boolean newPatient) { this.newPatient = newPatient; }

    public Patient getExistingPatient() { return existingPatient; }

    public void setExistingPatient(Patient existingId) { this.existingPatient = existingId; }

    public Title getUpdatedTitle() { return updatedTitle; }

    public void setUpdatedTitle(Title title) {this.updatedTitle = updatedTitle; }

    public String getUpdatedFirstName() { return updatedFirstName; }

    public void setUpdatedFirstName(String updatedFirstName) { this.updatedFirstName = updatedFirstName; }

    public String getUpdatedLastName() { return updatedLastName; }

    public void setUpdatedLastName(String updatedLastName) { this.updatedLastName = updatedLastName; }

    public Boolean getNewJobContext() { return newJobContext; }

    public void setNewJobContext(Boolean newJobContext) { this.newJobContext = newJobContext; }

    public String getUpdatedClinicalDetails() { return updatedClinicalDetails; }

    public void setUpdatedClinicalDetails(String updatedClinicalDetails) { this.updatedClinicalDetails = updatedClinicalDetails; }

    public Boolean getUpdatedUnwell() { return updatedUnwell; }

    public void setUpdatedUnwell(Boolean updatedUnwell) { this.updatedUnwell = updatedUnwell; }

    public Integer getUpdatedBed() { return updatedBed; }

    public void setUpdatedBed(Integer updatedBed) { this.updatedBed = updatedBed; }

    public Ward getUpdatedWard() { return updatedWard; }

    public void setUpdatedWard(Ward updatedWard) { this.updatedWard = updatedWard; }

    public List<Risk> getUpdatedRisks() { return updatedRisks; }

    public void setUpdatedRisks(List<Risk> updatedRisks) { this.updatedRisks = updatedRisks; }

}

package com.quantech.model;

import com.quantech.model.user.Title;
import com.quantech.model.user.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HandoverFormBackingObject {

    //Are we entering a new patient:
    @NotNull
    private Boolean newPatient;

    //New patient info:

    private Title newTitle;

    private String newFirstName;

    private String newLastName;

    private Integer newDay;

    private Integer newMonth;

    private Integer newYear;

    private Long newHospitalNumber;

    private Long newNHSNumber;

    //Existing patient info:

    private Long existingId;

    private Title updatedTitle;

    private String updatedFirstName;

    private String updatedLastName;

    @NotNull
    private Boolean newJobContext;

    //New job context info:

    private String newClinicalDetails;

    private Boolean newUnwell;

    private Integer newBed;

    private Long newWardId;

    //Existing job context info:

    private String updatedClinicalDetails;

    private Boolean updatedUnwell;

    private Integer updatedBed;

    private Ward updatedWard;

    private List<Risk> updatedRisks;

    public Boolean getNewPatient() { return newPatient; }

    public void setNewPatient(Boolean newPatient) { this.newPatient = newPatient; }

    public Title getNewTitle() { return newTitle; }

    public void setNewTitle(Title newTitle) { this.newTitle = newTitle; }

    public String getNewFirstName() { return newFirstName; }

    public void setNewFirstName(String newFirstName) { this.newFirstName = newFirstName; }

    public String getNewLastName() { return newLastName; }

    public void setNewLastName(String newLastName) { this.newLastName = newLastName; }

    public Integer getNewDay() { return newDay; }

    public void setNewDay(Integer newDay) { this.newDay = newDay; }

    public Integer getNewMonth() { return newMonth; }

    public void setNewMonth(Integer newMonth) { this.newMonth = newMonth; }

    public Integer getNewYear() { return newYear; }

    public void setNewYear(Integer newYear) { this.newYear = newYear; }

    public Long getNewHospitalNumber() { return newHospitalNumber; }

    public void setNewHospitalNumber(Long newHospitalNumber) { this.newHospitalNumber = newHospitalNumber; }

    public Long getNewNHSNumber() { return newNHSNumber; }

    public void setNewNHSNumber(Long newNHSNumber) { this.newNHSNumber = newNHSNumber; }

    public Long getExistingId() { return existingId; }

    public void setExistingId(Long existingId) { this.existingId = existingId; }

    public Title getUpdatedTitle() { return updatedTitle; }

    public void setUpdatedTitle(Title title) {this.updatedTitle = updatedTitle; }

    public String getUpdatedFirstName() { return updatedFirstName; }

    public void setUpdatedFirstName(String updatedFirstName) { this.updatedFirstName = updatedFirstName; }

    public String getUpdatedLastName() { return updatedLastName; }

    public void setUpdatedLastName(String updatedLastName) { this.updatedLastName = updatedLastName; }

    public Boolean getNewJobContext() { return newJobContext; }

    public void setNewJobContext(Boolean newJobContext) { this.newJobContext = newJobContext; }

    public String getNewClinicalDetails() { return newClinicalDetails; }

    public void setNewClinicalDetails(String newClinicalDetails) { this.newClinicalDetails = newClinicalDetails; }

    public Boolean getNewUnwell() { return newUnwell; }

    public void setNewUnwell(Boolean newUnwell) { this.newUnwell = newUnwell; }

    public Integer getNewBed() { return newBed; }

    public void setNewBed(Integer newBed) { this.newBed = newBed; }

    public Long getNewWardId() { return newWardId; }

    public void setNewWardId(Long newWardId) { this.newWardId = newWardId; }

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

package com.quantech.model;

import com.quantech.service.PatientService.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

public class JobContextFormBackingObject {

    @Autowired
    PatientServiceImpl patientService;

    private String clinicalDetails;

    private Boolean unwell;

    private Integer bed;

    private Ward ward;

    private Long patientId;

    public String getClinicalDetails() { return clinicalDetails; }

    public void setClinicalDetails(String clinicalDetails) { this.clinicalDetails = clinicalDetails; }

    public Boolean getUnwell() { return unwell; }

    public void setUnwell(Boolean unwell) { this.unwell = unwell; }

    public Integer getBed() { return bed; }

    public void setBed(Integer bed) { this.bed = bed; }

    public Ward getWard() { return ward; }

    public void setWard(Ward ward) { this.ward = ward; }

    public Long getPatientId() { return patientId; }

    public void setPatientId(Long patientId) { this.patientId = patientId; }

}

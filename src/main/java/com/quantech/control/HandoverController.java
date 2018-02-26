package com.quantech.control;

import com.quantech.model.JobContext;
import com.quantech.model.Patient;
import com.quantech.model.PatientFormBackingObject;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HandoverController {

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    WardServiceImpl wardService;

    @GetMapping(value="/createHandover/newPatient")
    public String newPatient(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        return "handover/newPatient";
    }

    @PostMapping(value="/createHandover/newPatient")
    public String addPatient(@ModelAttribute("patient") PatientFormBackingObject patient, Model model) {
        //TODO: Run validation on patientFormBackingObject
        //TODO: Check to see if patient already in database
        //TODO: If yes, redirect to view and select or go back to patient details
        //Save patient
        Patient newPatient = patient.toPatient();
        patientService.savePatient(newPatient);
        //Pass on to job context page

//        model.addAttribute("patient", newPatient);
//        model.addAttribute("jobContexts", newPatient.getJobContexts());
//        JobContext newJobContext = new JobContext();
//        newJobContext.setPatient(newPatient);
//        model.addAttribute("newJobContext", newJobContext);
//        model.addAttribute("wards", wardService.getAllWards());
//        return "doctor/chooseJobContext";
    }

    @GetMapping(value="/createHandover/existingPatient")
    public String existingPatient(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        return "handover/choosePatient";
    }

}

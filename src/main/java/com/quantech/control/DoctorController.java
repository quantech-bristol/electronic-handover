package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.JobsService.JobsServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class DoctorController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    JobsServiceImpl jobsService;

    @GetMapping(value="/createHandover")
    public String createHandover(Model model) {
        model.addAttribute("handover", new HandoverFormBackingObject());
        model.addAttribute("allPatients", patientService.getAllPatients());
        model.addAttribute("newPatient", new PatientFormBackingObject());
        model.addAttribute("allJobContexts", jobsService.getAllJobContexts());
        model.addAttribute("newJobContext", new JobContext());
        model.addAttribute("job", new Job());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctor/handover";
    }

    @PostMapping(value="/createHandover")
    public String createHandover(@ModelAttribute("handover") HandoverFormBackingObject handover) {
        if (handover.getNewPatient()) {
            //newPatient validity tests
            //Save newPatient
//            Patient pt = patient.toPatient();
//            patientService.savePatient(pt);
            //Keep id of new patient
        } else {
            //get id of existing patient
        }
//        if (handover.getNewJobContext()) {
//            //Save new job context to patient
//        } else {
//            //Get id of existing job context
//        }
        //Save new job
            return "redirect:/";
    }

    @GetMapping("/patient/hospitalNumber={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientByHospitalNumber(id));
        return "doctor/viewPatient";
    }

}
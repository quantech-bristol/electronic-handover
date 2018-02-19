package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Patient;
import com.quantech.model.PatientFormBackingObject;
import com.quantech.model.user.UserCore;
import com.quantech.service.PatientService.PatientServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class DoctorController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    PatientServiceImpl patientService;

    @GetMapping(value="/createHandover")
    public String createHandover(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        model.addAttribute("allPatients", patientService.getAllPatients());
        return "doctor/handover";
    }

    @PostMapping(value="/createHandover")
    public String createHandover(@ModelAttribute("patient") PatientFormBackingObject patient) {
        Patient pt = patient.toPatient();
        patientService.savePatient(pt);
        return "redirect:/";
    }

    @GetMapping("/patient/hospitalNumber={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientByHospitalNumber(id));
        return "doctor/viewPatient";
    }

}
package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.JobsService.JobsServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.UserService.UserServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class HandoverController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    WardServiceImpl wardService;

    @Autowired
    JobsServiceImpl jobsService;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping(value="/newPatient")
    public String newPatient(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        return "handover/newPatient";
    }

    @PostMapping(value="/newPatient")
    public String addPatient(@ModelAttribute("patient") PatientFormBackingObject patientFBO,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Patient patient = patientFBO.toPatient();
        patientService.savePatient(patient);
        redirectAttributes.addAttribute("patient", patient);
        return "redirect:/patient/createHandover/clinicalDetails";
    }

    @GetMapping(value="/searchPatients")
    public String searchPatient(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        return "handover/searchPatients";
    }

    @PostMapping(value="/searchPatients")
    public String filterPatients(@ModelAttribute("patient") PatientFormBackingObject patient,
                                 RedirectAttributes redirectAttributes) {
        List<Patient> patients = patientService.findMatchesFromFilter(patient);
        redirectAttributes.addAttribute("patients", patients);
        return "redirect:/choosePatient";
    }

    @GetMapping(value="/choosePatient")
    public String choosePatient(@RequestParam("patients") List<Patient> patients,
                                Model model) {
        model.addAttribute("patients", patients);
        return "handover/choosePatient";
    }

    @GetMapping(value="/selectPatient/{id}")
    public String selectPatient(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        Patient patient = patientService.getPatientById(id);
        redirectAttributes.addAttribute("patient", patient);
        return "redirect:/patient/createHandover/clinicalDetails";
    }

    @GetMapping(value="/patient/createHandover/clinicalDetails")
    public String chooseJobContext(@RequestParam("patient") Patient patient,
                                   Model model) {
        model.addAttribute("patientInfo", patient);
        JobContextFormBackingObject jobContextFormBackingObject = new JobContextFormBackingObject();
        jobContextFormBackingObject.setPatientId(patient.getId());
        model.addAttribute("newJobContext", jobContextFormBackingObject);
        model.addAttribute("wards", wardService.getAllWards());
        model.addAttribute("jobContexts", patient.getJobContexts());
        return "handover/jobContext";
    }

    @PostMapping(value="/patient/createHandover/clinicalDetails")
    public String addJobContext(@ModelAttribute("newJobContext") JobContextFormBackingObject jobContextFormBackingObject,
                                RedirectAttributes redirectAttributes) {
        JobContext jobContext = new JobContext();
        jobContext.setClinicalDetails(jobContextFormBackingObject.getClinicalDetails());
        jobContext.setUnwell(jobContextFormBackingObject.getUnwell());
        jobContext.setPatient(patientService.getPatientById(jobContextFormBackingObject.getPatientId()));
        jobContext.setBed(jobContextFormBackingObject.getBed());
        jobContext.setWard(jobContextFormBackingObject.getWard());
        jobsService.saveJobContext(jobContext);
        redirectAttributes.addAttribute("patient", jobContext.getPatient());
        redirectAttributes.addAttribute("jobContext", jobContext);
        return "redirect:/patient/createHandover";
    }

    @GetMapping(value="/patient/createHandover")
    public String newJob(@RequestParam("patient") Patient patient,
                         @RequestParam("jobContext") JobContext jobContext,
                         Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        model.addAttribute("currentDoctorId", doctorService.getDoctor(userInfo).getId());
        model.addAttribute("patient", patient);
        model.addAttribute("jobContext", jobContext);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
        JobFormBackingObject jobFormBackingObject = new JobFormBackingObject();
        jobFormBackingObject.setContextId(jobContext.getId());
        model.addAttribute("job", jobFormBackingObject);
        return "handover/chooseJob";
    }

    @PostMapping(value="/patient/createHandover")
    public String addJob(@ModelAttribute("job") JobFormBackingObject jobFormBackingObject) {
        Job job = new Job();
        job.setDescription(jobFormBackingObject.getDescription());
        job.setCategory(categoryService.getCategory(jobFormBackingObject.getCategoryId()));
        job.setJobContext(jobsService.getJobContext(jobFormBackingObject.getContextId()));
        job.setDoctor(doctorService.getDoctor(userService.findUserById(jobFormBackingObject.getDoctorId())));
        jobsService.saveJob(job);
        return "redirect:/";
    }

}

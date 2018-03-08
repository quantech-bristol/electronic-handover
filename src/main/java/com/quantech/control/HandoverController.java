package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.JobsService.JobsServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.UserService.UserService;
import com.quantech.service.WardService.WardServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    UserService userService;

    @GetMapping(value="/newPatient")
    public String newPatient(Model model) {
        return newPatient(model,new PatientFormBackingObject());
    }

    private String newPatient(Model model, PatientFormBackingObject patient) {
        model.addAttribute("patient", patient);
        return "handover/newPatient";
    }

    @PostMapping(value="/newPatient")
    public String addPatient(@Valid @ModelAttribute("patient") PatientFormBackingObject patientFBO,
                             Model model,
                             BindingResult result,
                             Errors errors,
                             RedirectAttributes redirectAttributes) {
        patientService.CheckValidity(result,patientFBO);
        if (errors.hasErrors()) {
            return newPatient(model,patientFBO);
        } else {
            Patient patient = patientFBO.toPatient();
            patientService.savePatient(patient);
            redirectAttributes.addAttribute("patient", patient);
            return "redirect:/patient/createHandover/clinicalDetails";
        }
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
        return chooseJobContext(patient,model,new JobContextFormBackingObject());
    }

    private String chooseJobContext(@RequestParam("patient") Patient patient,
                                   Model model,
                                   JobContextFormBackingObject job) {
        model.addAttribute("patientInfo", patient);
        job.setPatientId(patient.getId());
        model.addAttribute("newJobContext", job);
        model.addAttribute("wards", wardService.getAllWards());
        model.addAttribute("jobContexts", patient.getJobContexts());
        return "handover/jobContext";
    }

    @PostMapping(value="/patient/createHandover/clinicalDetails")
    public String addJobContext(@Valid @ModelAttribute("newJobContext") JobContextFormBackingObject jobContextFormBackingObject,
                                RedirectAttributes redirectAttributes,
                                BindingResult result,
                                Errors errors,
                                Model model,
                                HttpServletRequest request
                                ) {
        jobsService.CheckJobContextFormValidity(result,jobContextFormBackingObject);
        if (errors.hasErrors()) {
            Patient p = patientService.getPatientById(jobContextFormBackingObject.getPatientId());
            request.setAttribute("patient",p);
            return chooseJobContext(p,model,jobContextFormBackingObject);
        } else {
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
    }

    @GetMapping(value="/selectJobContext/{id}")
    public String selectJobContext(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        JobContext jobContext = jobsService.getJobContext(id);
        Patient patient = jobContext.getPatient();
        redirectAttributes.addAttribute("jobContext", jobContext);
        redirectAttributes.addAttribute("patient", patient);
        return "redirect:/patient/createHandover";
    }

    @GetMapping(value="/patient/createHandover")
    public String newJob(@RequestParam("patient") Patient patient,
                         @RequestParam("jobContext") JobContext jobContext,
                         Model model) {
        return newJob(patient,jobContext,new JobFormBackingObject(),model);
    }

    private String newJob(@RequestParam("patient") Patient patient,
                         @RequestParam("jobContext") JobContext jobContext,
                         JobFormBackingObject job,
                         Model model) {
        UserCore userInfo =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();
        model.addAttribute("currentDoctorId", doctorService.getDoctor(userInfo).getId());
        model.addAttribute("patient", patient);
        model.addAttribute("jobContext", jobContext);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());

        job.setContextId(jobContext.getId());
        model.addAttribute("job", job);
        return "handover/chooseJob";
    }

    @PostMapping(value="/patient/createHandover")
    public String addJob(@Valid @ModelAttribute("job") JobFormBackingObject jobFormBackingObject,
                         BindingResult result,
                         Errors errors,
                         Model model,
                         HttpServletRequest request) {
        jobsService.CheckJobValidity(result,jobFormBackingObject);
        if (errors.hasErrors()) {
            JobContext jc = jobsService.getJobContext(jobFormBackingObject.getContextId());
            Patient p = jc.getPatient();
            request.setAttribute("patient",p);
            request.setAttribute("jobContext",jc);
            return newJob(p,jc,jobFormBackingObject,model);
        } else {
            Job job = new Job();
            job.setDescription(jobFormBackingObject.getDescription());
            job.setCategory(categoryService.getCategory(jobFormBackingObject.getCategoryId()));
            job.setJobContext(jobsService.getJobContext(jobFormBackingObject.getContextId()));
            job.setDoctor(doctorService.getDoctor(userService.findUserById(jobFormBackingObject.getDoctorId())));
            jobsService.saveJob(job);
            return "redirect:/";
        }
    }

}

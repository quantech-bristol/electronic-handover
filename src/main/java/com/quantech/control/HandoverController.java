package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.service.CategoryService.CategoryService;
import com.quantech.service.DoctorService.DoctorService;
import com.quantech.service.JobsService.JobsService;
import com.quantech.service.LoggingService.LogServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.RiskService.RiskService;
import com.quantech.service.UserService.UserService;
import com.quantech.service.WardService.WardService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HandoverController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    JobsService jobsService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    RiskService riskService;

    @Autowired
    UserService userService;

    @Autowired
    WardService wardService;

    @GetMapping(value="/patients")
    public String viewPatients(@RequestParam(value = "firstName", required = false) String firstName,
                                 @RequestParam(value = "lastName", required = false) String lastName,
                                 Model model) {
        List<Patient> patients = patientService.findMatchesFromFilter(firstName, lastName);
        model.addAttribute("patients", patients);
        model.addAttribute("newPatient", new PatientFormBackingObject());
        model.addAttribute("errs", false);
        return "doctor/patients";
    }

    @PostMapping(value="/patients")
    public String addPatient(@Valid @ModelAttribute("newPatient") PatientFormBackingObject patientFormBackingObject,
                             Model model,
                             BindingResult result,
                             Errors errors,
                             RedirectAttributes redirectAttributes) {
        patientService.CheckValidity(result, patientFormBackingObject);
        if (errors.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("newPatient", patientFormBackingObject);
            model.addAttribute("errs", true);
            return "doctor/patients";
        } else {
            Patient patient = patientFormBackingObject.toPatient();
            patientService.savePatient(patient);
            redirectAttributes.addAttribute("patientId", patient.getId());
            return "redirect:/patient/{patientId}";
        }
    }

    @GetMapping(value="/patient/{patientId}")
    public String viewPatient(@PathVariable(value="patientId") Long patientId,
                                Model model) {
        UserCore user =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();
        Patient patient = patientService.getPatientById(patientId);
        model.addAttribute("patientInfo", patient);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newJobContext", new JobContextFormBackingObject());
        model.addAttribute("newJob", new JobFormBackingObject());
        model.addAttribute("wards", wardService.getAllWards());
        List<JobContext> jobContexts = jobsService.sortJobContextsByCreationDate(patient.getJobContexts());
        model.addAttribute("jobContexts", jobContexts);
        model.addAttribute("jobContextsCount", patient.getJobContexts().size());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
        model.addAttribute("currentDoctor", user);

        return "doctor/patient";
    }

    @PostMapping(value="/patient/{patientId}")
    public String addJobContext(@Valid @ModelAttribute("newJobContext") JobContextFormBackingObject jobContextFormBackingObject,
                                @PathVariable(value="patientId") Long patientId,
                                @RequestParam("returnTo") String returnTo,
                                BindingResult result,
                                Errors errors,
                                Model model) {
        jobsService.CheckJobContextFormValidity(result, jobContextFormBackingObject);
        if (errors.hasErrors()) {
            Patient patient = patientService.getPatientById(patientId);
            model.addAttribute("patientInfo", patient);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newJob", new JobFormBackingObject());
            model.addAttribute("wards", wardService.getAllWards());
            List<JobContext> jobContexts = jobsService.sortJobContextsByCreationDate(patient.getJobContexts());
            model.addAttribute("jobContexts", jobContexts);
            model.addAttribute("jobContextsCount", patient.getJobContexts().size());
            model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
            model.addAttribute("newJobContext", jobContextFormBackingObject);
            model.addAttribute("patientId", patientId);
            model.addAttribute("errs", "jc");
            return "doctor/patient";
        } else {
            JobContext jobContext = new JobContext();
            jobContext.setClinicalDetails(jobContextFormBackingObject.getClinicalDetails());
            jobContext.setUnwell(jobContextFormBackingObject.getUnwell());
            jobContext.setPatient(patientService.getPatientById(patientId));
            jobContext.setBed(jobContextFormBackingObject.getBed());
            jobContext.setWard(jobContextFormBackingObject.getWard());
            jobsService.saveJobContext(jobContext);
            if (returnTo.equals("patient")) return "redirect:/patient/{patientId}";
            else return "redirect:/";
        }
    }

    @Transactional
    @PostMapping(value="/addJob/{jobContextId}")
    public String addJob(@Valid @ModelAttribute("newJob") JobFormBackingObject job,
                         @RequestParam("returnTo") String returnTo,
                         @PathVariable("jobContextId") Long jobContextId,
                         RedirectAttributes redirectAttributes,
                         BindingResult result,
                         Errors errors,
                         Model model) {
        jobsService.CheckJobValidity(result,job);
        if (errors.hasErrors()) {
            Patient patient = jobsService.getJobContext(jobContextId).getPatient();
            model.addAttribute("patientInfo", patient);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newJobContext", new JobContextFormBackingObject());
            model.addAttribute("wards", wardService.getAllWards());
            List<JobContext> jobContexts = jobsService.sortJobContextsByCreationDate(patient.getJobContexts());
            model.addAttribute("jobContexts", jobContexts);
            model.addAttribute("jobContextsCount", patient.getJobContexts().size());
            model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
            model.addAttribute("newJob", job);
            return "doctor/patient";
        }
        else {
            Job j = new Job();
            j.setCategory(categoryService.getCategory(job.getCategoryId()));
            j.setCreationDate(new Date());
            j.setDoctor(doctorService.getDoctor(userService.findUserById(job.getDoctorId())));
            j.setJobContext(jobsService.getJobContext(job.getContextId()));
            j.setDescription(job.getDescription());
            jobsService.saveJob(j);
            if (returnTo.equals("patient")) {
                redirectAttributes.addAttribute("patientId", j.getJobContext().getPatient().getId());
                return "redirect:/patient/{patientId}";
            }
            else return "redirect:/";
        }
    }

    @Transactional
    @PostMapping(value="/handoverJob")
    public String handoverJob(@RequestParam(value = "jobId", required=true) Long id,
                              @RequestParam("jobDescription") String description,
                              @RequestParam("doctor") Doctor doctor,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("returnTo") String returnTo) {
        Job job = jobsService.getJob(id);
        job.setDescription(description);
        job.setDoctor(doctor);
        jobsService.saveJob(job);
        if (returnTo.equals("patient")) {
            redirectAttributes.addAttribute("patientId", job.getJobContext().getPatient().getId());
            return "redirect:/patient/{patientId}";
        } else return "redirect:/";
    }

    @PostMapping(value="/completeJob")
    public String completeJob(@RequestParam("job") Job job,
                              @RequestParam("returnTo") String returnTo,
                              RedirectAttributes redirectAttributes) {
        jobsService.completeJob(job);
        if (returnTo.equals("patient")) {
            redirectAttributes.addAttribute("patientId", job.getJobContext().getPatient().getId());
            return "redirect:/patient/{patientId}";
        } else return "redirect:/";
    }

}

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

    @GetMapping(value="/newPatient")
    public String newPatient(Model model) {
        return newPatient(model, new PatientFormBackingObject());
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
        patientService.CheckValidity(result, patientFBO);
        if (errors.hasErrors()) {
            return newPatient(model, patientFBO);
        } else {
            Patient patient = patientFBO.toPatient();
            patientService.savePatient(patient);
            redirectAttributes.addAttribute("patientId", patient.getId());
            return "redirect:/patient/{patientId}";
        }
    }

    @GetMapping(value="/searchPatients")
    public String searchPatient(Model model) {
        model.addAttribute("patient", new PatientFormBackingObject());
        return "handover/searchPatients";
    }

    @GetMapping(value="/filterPatients")
    public String filterPatients(@RequestParam(value = "firstName", required = false) String firstName,
                                 @RequestParam(value = "lastName", required = false) String lastName,
                                 Model model) {
        List<Patient> patients = patientService.findMatchesFromFilter(firstName, lastName);
        model.addAttribute("patients", patients);
        return "handover/choosePatient";
    }

    @GetMapping(value="/patient/{patientId}")
    public String selectPatient(@PathVariable(value="patientId") Long patientId,
                                Model model) {
        Patient patient = patientService.getPatientById(patientId);
        model.addAttribute("patientInfo", patient);
        model.addAttribute("newJobContext", new JobContextFormBackingObject());
        model.addAttribute("wards", wardService.getAllWards());
        model.addAttribute("jobContexts", patient.getJobContexts());
        return "handover/jobContext";
    }

//    TODO separate adding job context to new page
    @PostMapping(value="/patient/{patientId}")
    public String newJobContext(@Valid @ModelAttribute("newJobContext") JobContextFormBackingObject jobContextFormBackingObject,
                                @PathVariable(value="patientId") Long patientId,
                                BindingResult result,
                                Errors errors) {
        jobsService.CheckJobContextFormValidity(result,jobContextFormBackingObject);
        if (errors.hasErrors()) {
            return "redirect:/patient/{patientId}";
//            TODO error messages once above todo is complete
        } else {
            JobContext jobContext = new JobContext();
            jobContext.setClinicalDetails(jobContextFormBackingObject.getClinicalDetails());
            jobContext.setUnwell(jobContextFormBackingObject.getUnwell());
            jobContext.setPatient(patientService.getPatientById(patientId));
            jobContext.setBed(jobContextFormBackingObject.getBed());
            jobContext.setWard(jobContextFormBackingObject.getWard());
            jobsService.saveJobContext(jobContext);
            return "redirect:/patient/{patientId}";
        }
    }

//----------------------------------------------------------------------------------------------------------------------
//  ADDING AND PASSING A JOB

    @GetMapping(value="/createJob")
    public String createJob(@RequestParam(value = "jobContextId", required=true) Long id, Model model) {
        return createJob(id, model, new JobFormBackingObject());
    }

    private String createJob(Long id, Model model, JobFormBackingObject job) {
        UserCore userInfo =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();

        model.addAttribute("job", job);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorId",userInfo.getId());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
        model.addAttribute("contextId",id);
        return "doctor/newJob";
    }

    @Transactional
    @PostMapping(value="/createJob")
    public String createJob(@Valid @ModelAttribute("job") JobFormBackingObject job,
                            BindingResult result,
                            Errors errors,
                            Model model,
                            HttpServletRequest request) {
        jobsService.CheckJobValidity(result,job);
        if (errors.hasErrors()){
            request.setAttribute("jobContextId",job.getContextId());
            return createJob(job.getContextId(),model,job);
            //return "doctor/newJob";
        }
        else {
            Job j = new Job();
            j.setCategory(categoryService.getCategory(job.getCategoryId()));
            j.setCreationDate(new Date());
            j.setDoctor(doctorService.getDoctor(userService.findUserById(job.getDoctorId())));
            j.setJobContext(jobsService.getJobContext(job.getContextId()));
            j.setDescription(job.getDescription());
            jobsService.saveJob(j);
            return "redirect:/";
        }
    }

    @GetMapping(value="/handoverJob")
    public String handoverJob(@RequestParam(value = "jobId", required=true) Long id, Model model) {
        UserCore userInfo =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();
        model.addAttribute("jobId",id);
        model.addAttribute("description",jobsService.getJob(id).getDescription());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());

        return "doctor/handoverJob";
    }

    @Transactional
    @PostMapping(value="/handoverJob")
    public String handoverJob(@RequestParam(value = "jobId", required=true) Long id,
                              @RequestParam("jobDescription") String description,
                              @RequestParam("doctor") Doctor doctor) {
        Job job = jobsService.getJob(id);
        job.setDescription(description);
        job.setDoctor(doctor);

        jobsService.saveJob(job);
        return "redirect:/";
    }

//----------------------------------------------------------------------------------------------------------------------
//  COMPLETE JOB

    @PostMapping(value="/completeJob")
    public String completeJob(@RequestParam("job") Job job) {
        jobsService.completeJob(job);
        return "redirect:/";
    }

}

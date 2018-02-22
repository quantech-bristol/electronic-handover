package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.*;
import com.quantech.model.user.Title;
import com.quantech.model.user.UserCore;
import com.quantech.repo.PatientRepository;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.JobsService.JobsServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.UserService.UserServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;
import jdk.nashorn.internal.scripts.JO;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    JobsServiceImpl jobsService;

    @Autowired
    WardServiceImpl wardService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping(value="/createHandover")
    public String createHandover(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        model.addAttribute("currentUserId", userInfo.getId());
        model.addAttribute("handover", new HandoverFormBackingObject());
        List<Patient> allPatients = patientService.getAllPatients();
        model.addAttribute("allPatients", allPatients);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());
        model.addAttribute("wards", wardService.getAllWards());
        model.addAttribute("list", new ArrayList<>());
        return "doctor/handover";
    }

    @Transactional
    @PostMapping(value="/createHandover")
    public String createHandover(@ModelAttribute("handover") HandoverFormBackingObject handover) {
        Patient newPatient = new Patient(handover.getNewTitle(), handover.getNewFirstName(), handover.getNewLastName(), LocalDate.of(handover.getNewYear(), handover.getNewMonth(), handover.getNewDay()), handover.getNewHospitalNumber(), handover.getNewNHSNumber(), new ArrayList<>());
        JobContext newJobContext = new JobContext(handover.getNewClinicalDetails(), handover.getNewUnwell(), new Date(), newPatient, handover.getNewBed(), wardService.getWard(handover.getNewWardId()), new ArrayList<>(), new ArrayList<>());
        Doctor doctor = doctorService.getDoctor(userService.findUserById(handover.getUserId()));
        Job newJob = new Job(handover.getJobDescription(), categoryService.getCategory(handover.getCategoryId()), new Date(), null, newJobContext, doctor);
        patientService.savePatient(newPatient);
        jobsService.saveJobContext(newJobContext);
        jobsService.saveJob(newJob);
        return "redirect:/";
    }

    @GetMapping(value="/createJob")
    public String createJob(@RequestParam(value = "jobContextId", required=true) Long id, Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        JobFormBackingObject job = new JobFormBackingObject();

        model.addAttribute("job", job);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorId",userInfo.getId());
        model.addAttribute("contextId",id);
        return "doctor/newJob";
    }

    @Transactional
    @PostMapping(value="/createJob")
    public String createJob(@ModelAttribute("job") JobFormBackingObject job) {
        Job j = new Job();
        j.setCategory(categoryService.getCategory(job.getCategoryId()));
        j.setCreationDate(new Date());
        j.setDoctor(doctorService.getDoctor(userService.findUserById(job.getDoctorId())));
        j.setJobContext(jobsService.getJobContext(job.getContextId()));
        j.setDescription(job.getDescription());

        jobsService.saveJob(j);
        return "redirect:/";
    }

    @GetMapping(value="/handoverJob")
    public String handoverJob(@RequestParam(value = "jobId", required=true) Long id, Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Job job = jobsService.getJob(id);

        model.addAttribute("job", job);
        model.addAttribute("doctorUsers", userService.getAllDoctorUsers());

        return "doctor/handoverJob";
    }

    @Transactional
    @PostMapping(value="/handoverJob")
    public String handoverJob(@ModelAttribute("job") Job job) {
        jobsService.saveJob(job);
        return "redirect:/";
    }

    @GetMapping("/patient/id={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "doctor/viewPatient";
    }

}
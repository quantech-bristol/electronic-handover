package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.misc.PdfGenerator;
import com.quantech.model.*;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.repo.PatientRepository;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.JobsService.JobsServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.UserService.UserService;
import com.quantech.service.WardService.WardServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    UserService userService;

    @GetMapping(value="/createJob")
    public String createJob(@RequestParam(value = "jobContextId", required=true) Long id, Model model) {
        return createJob(id, model, new JobFormBackingObject());
    }

    private String createJob(Long id, Model model, JobFormBackingObject job) {
        UserCore userInfo =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();

        model.addAttribute("job", job);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("doctorId",userInfo.getId());
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

    @PostMapping(value="/completeJob")
    public String completeJob(@RequestParam("job") Job job) {
        jobsService.completeJob(job);
        return "redirect:/";
    }

    @GetMapping(value="/filterJobContexts")
    public String filter(RedirectAttributes request,
                         @RequestParam(name="unwell",required=false) String unwell,
                         @RequestParam(name="risk", required=false) Long[] riskIDs,
                         @RequestParam(name="category", required=false) Long[] categoryIDs,
                         @RequestParam(name="ward", required =false) Long[] wardIDs,
                         @RequestParam(name="complete",required = false) String complete,
                         @RequestParam(name="sort",required = false) String sort)  {
        request.addAttribute("unwell",unwell);
        if (riskIDs != null)
            request.addAttribute("risk",riskIDs);

        if (wardIDs != null)
            request.addAttribute("ward",wardIDs);

        if (categoryIDs != null)
            request.addAttribute("category",categoryIDs);

        request.addAttribute("complete",complete);
        request.addAttribute("sort",sort);
        return "redirect:/";
    }

    @GetMapping("/patient/id={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "doctor/viewPatient";
    }

    @RequestMapping(value="/pdf", method=RequestMethod.POST, produces="application/pdf")
    @ResponseBody
    public FileSystemResource patientPdf(List<JobContext> jcs) throws Exception {
        PdfGenerator pdfGen = new PdfGenerator();

        pdfGen.gen(jcs);

        return new FileSystemResource("pdfout.pdf");
    }


}
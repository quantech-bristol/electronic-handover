package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.misc.PdfGenerator;
import com.quantech.model.Doctor;
import com.quantech.model.Job;
import com.quantech.model.JobContext;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.service.CategoryService.CategoryService;
import com.quantech.service.DoctorService.DoctorService;
import com.quantech.service.JobsService.JobsService;
import com.quantech.service.RiskService.RiskService;
import com.quantech.service.WardService.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Controller
@SessionAttributes({"jobContexts"})
public class DoctorController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    JobsService jobsService;

    @Autowired
    RiskService riskService;

    @Autowired
    WardService wardService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String home(@RequestParam(value = "unwell", required = false) String unwell,
                       @RequestParam(value = "risk", required = false) Long[] riskId,
                       @RequestParam(value = "category",required = false) Long[] categoryId,
                       @RequestParam(value = "ward", required = false) Long[] wardId,
                       @RequestParam(value = "complete", required = false) String completed,
                       @RequestParam(value = "sort", required = false) String sort,
                       Model model) {
        UserCore user =  ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();


        if (user.isDoctor()) {
            model.addAttribute("risks",riskService.getAllRisks());
            model.addAttribute("wards",wardService.getAllWards());
            model.addAttribute("category",categoryService.getAllCategories());

            Doctor d = doctorService.getDoctor(user);
            List<JobContext> jcs = jobsService.getJobContextsUnderCareOf(d);

            // Compiling job context filter predicates
            Set<Predicate<JobContext>> p = new HashSet<>();
            if (unwell != null)
                p.add(jobsService.patientIsUnwell());
            if (riskId != null)
                for (Long id : riskId)
                    p.add(jobsService.patientHasRisk(riskService.getRisk(id)));
            if (wardId != null)
                for (Long id : wardId)
                    p.add(jobsService.patientIsInWard(wardService.getWard(id)));

            // Compiling job filter predicates.
            Set<Predicate<Job>> p2 = new HashSet<>();
            if (categoryId != null)
                for (Long id : categoryId)
                    p2.add(jobsService.jobIsOfCategory(categoryService.getCategory(id)));
            if (completed != null) {
                if (completed.equals("true"))
                    p2.add(jobsService.jobIsComplete());
                if (completed.equals("false")) {
                    p2.add(j -> j.getCompletionDate() == null);
                }
            }

            // Applying both sets of predicates to filter the job contexts.
            jcs = jobsService.filterJobContextsBy(jcs,p,p2);

            // Now apply sort.
            if (sort != null) {
                if (sort.equals("firstName"))
                    jcs = jobsService.sortJobContextsByFirstName(jcs);
                if (sort.equals("lastName"))
                    jcs = jobsService.sortJobContextsByLastName(jcs);
                if (sort.equals("contextCreationDate"))
                    jcs = jobsService.sortJobContextsByCreationDate(jcs);
            }
            else {
                jcs = jobsService.sortJobContextsByCreationDate(jcs);
            }

            // Sort individual jobs by creation date (default).
            for (JobContext jc : jcs) {
                jc.setJobs(jobsService.sortJobsNewestFirst(jc.getJobs()));
            }

            model.addAttribute("jobContexts", jcs);
            //model.addAttribute("jcsWrapped", new JobContextsWrapper(jcs));

            return "doctor/doctorHome";

        } else if (user.isAdmin()) {
            return "redirect:/admin";
        } else {
            return "doctor/doctorHome";
        }
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

    @RequestMapping(value="/pdf", method=RequestMethod.POST, produces="application/pdf", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public FileSystemResource patientPdf(@SessionAttribute("jobContexts") List<JobContext> jcs) throws Exception {

        try { System.out.println(jcs); }
        catch(Exception e)
        { System.out.println("very null"); }

        PdfGenerator pdfGen = new PdfGenerator();
        pdfGen.gen(jcs);

        return new FileSystemResource("pdfout.pdf");
    }

}
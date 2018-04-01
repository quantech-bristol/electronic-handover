package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Category;
import com.quantech.model.Log.Log;
import com.quantech.model.Log.LogOperations.AddUser;
import com.quantech.model.Log.LogOperations.LogFilterBackingObject;
import com.quantech.model.Risk;
import com.quantech.model.Ward;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.service.CategoryService.CategoryService;
import com.quantech.service.DoctorService.DoctorService;
import com.quantech.service.JobsService.JobsService;
import com.quantech.service.LoggingService.LogServiceImpl;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.RiskService.RiskService;
import com.quantech.service.UserService.UserService;
import com.quantech.service.WardService.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@SessionAttributes({"logList","MatchedUsersMap"})
public class AdminController {

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

    @GetMapping(value="/admin")
    public String adminPage() {
        return "admin/adminHome";
    }

    @GetMapping(value="/admin/users")
    public String users(@RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName,
                        @RequestParam("username") String username,
                        @RequestParam("email") String email,
                        Model model) {
        model.addAttribute("usercore", new UserFormBackingObject());
        HashMap<Long, UserCore> userCoreHashMap = userService.findMatchesFromFilter(firstName, lastName, username, email);
        model.addAttribute("MatchedUsers",userCoreHashMap.values());
        return "admin/users";
    }

    @PostMapping(value="/admin/addUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user,
                             BindingResult result,
                             Errors errors,
                             Model model) {
        userService.CheckValidity(result, true, user);
        if (errors.hasErrors()) {
            model.addAttribute("MatchedUsers", userService.getAllUsers());
            model.addAttribute("errs", true);
            return "admin/users";
        } else {
            userService.createUser(user);
            return "redirect:/admin";
        }
    }

//    @GetMapping(value = "/admin/editUser/{id}")
//    public String editUser(Model model, @PathVariable("id") long id )
//    {
//        UserCore user = userService.findUserById(id);
//
//        model.addAttribute("UserFiltering","True");
//        model.addAttribute("Title","Edit User");
//        model.addAttribute("usercore", new UserFormBackingObject(user));
//        model.addAttribute("postUrl", "/admin/editUser");
//        return "admin/createUser";
//    }

//    @PostMapping(value = "/admin/editUser")
//    public String editUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors)
//    {
//        userService.CheckValidity(result, false, user);
//        if (errors.hasErrors()) {
//            return "admin/createUser";
//        }
//        else {
//            userService.editUser(user);
//            return "redirect:/admin";
//        }
//    }

//    @DeleteMapping(value = "/admin/deleteUser/{id}")
//    public String DeleteUser(@PathVariable("id") long id)
//    {
//        userService.deleteUserById(id);
//        return "redirect:/admin";
//    }

    @GetMapping(value="/admin/wards")
    public String wards(Model model) {
        model.addAttribute("newWard", new Ward());
        model.addAttribute("wards", wardService.getAllWards());
        model.addAttribute("errs", false);
        return "admin/wards";
    }

    @PostMapping(value="/admin/addWard")
    public String addWard(@ModelAttribute("newWard") Ward ward,
                          BindingResult result,
                          Errors errors,
                          Model model) {
        wardService.CheckValidity(result, ward);
        if (errors.hasErrors()) {
            model.addAttribute("wards", wardService.getAllWards());
            model.addAttribute("newWard", ward);
            model.addAttribute("errs", true);
            return "admin/wards";
        } else {
            wardService.saveWard(ward);
            return "redirect:/admin/wards";
        }
    }

    @PostMapping(value="/admin/renameWard")
    public String renameWard(@ModelAttribute("newWard") Ward ward, @RequestParam(value = "id", required=true) Long id,
                             Errors errors,
                             Model model,
                             BindingResult result) {
        wardService.CheckValidity(result, ward);
        if (errors.hasErrors()) {
            model.addAttribute("newWard", ward);
            model.addAttribute("wards", wardService.getAllWards());
            model.addAttribute("wardId", id);
            return "admin/wards";
        } else {
            ward.setId(id);
            wardService.saveWard(ward);
            return "redirect:/admin/wards";
        }
    }

    @GetMapping(value="/admin/deleteWard")
    public String deleteWard(@ModelAttribute Ward ward, @RequestParam(value = "id", required=true) Long id) {
        wardService.deleteWard(id);
        return "redirect:/admin/wards";
    }

    @GetMapping(value="/admin/categories")
    public String categories(Model model) {
        model.addAttribute("newCat", new Category());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @RequestMapping(value="/admin/addCategory", method=RequestMethod.POST)
    public String addCategory(@ModelAttribute("newCat") Category category,
                              Errors errors,
                              Model model,
                              BindingResult result) {
        categoryService.CheckValidity(result, category);
        if (errors.hasErrors()) {
            model.addAttribute("newCat", category);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("errs", true);
            return "admin/categories";
        } else {
            categoryService.saveCategory(category);
            return "redirect:/admin/categories";
        }
    }

    @PostMapping(value="/admin/renameCategory")
    public String renameCategory(@ModelAttribute("newCat") Category category, @RequestParam(value = "id", required=true) Long id,
                                 BindingResult result,
                                 Errors errors,
                                 Model model) {
        categoryService.CheckValidity(result, category);
        if (errors.hasErrors()) {
            model.addAttribute("newCat", category);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("categoryId", id);
            return "admin/categories";
        } else {
            category.setId(id);
            categoryService.saveCategory(category);
            return "redirect:/admin/categories";
        }
    }

    @GetMapping(value="/admin/deleteCategory")
    public String deleteRisk(@ModelAttribute Category category, @RequestParam(value = "id", required=true) Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping(value="/admin/risks")
    public String risks(Model model) {
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/risks";
    }

    @PostMapping(value="/admin/addRisk")
    public String addRisk(@ModelAttribute("newRisk") Risk risk,
                          BindingResult result,
                          Errors errors,
                          Model model) {
        riskService.CheckValidity(result, risk);
        if (errors.hasErrors()) {
            model.addAttribute("newRisk", risk);
            model.addAttribute("risks", riskService.getAllRisks());
            model.addAttribute("errs", true);
            return "admin/risks";
        } else {
            riskService.saveRisk(risk);
            return "redirect:/admin/risks";
        }
    }

    @PostMapping(value="/admin/renameRisk")
    public String renameRisk(@ModelAttribute("newRisk") Risk risk, @RequestParam(value = "id", required=true) Long id,
                             BindingResult result,
                             Errors errors,
                              Model model) {
        riskService.CheckValidity(result, risk);
        if (errors.hasErrors()) {
            model.addAttribute("newRisk", risk);
            model.addAttribute("risks", riskService.getAllRisks());
            model.addAttribute("riskId", id);
            return "admin/risks";
        } else {
            risk.setId(id);
            riskService.saveRisk(risk);
            return "redirect:/admin/risks";
        }
    }

    @GetMapping(value="/admin/deleteRisk")
    public String deleteRisk(@ModelAttribute("newRisk") Risk risk, @RequestParam(value = "id", required=true) Long id) {
        riskService.deleteRisk(id);
        return "redirect:/admin/risks";
    }

    @GetMapping(value = "/admin/filterLogs")
    public String filterLogs(Model model)
    {
        model.addAttribute("UserFiltering",false);
        model.addAttribute("Title","FilterLogs");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("logObject", new LogFilterBackingObject());
        model.addAttribute("postUrl", "/admin/filterLogs");
        return "logging/logFilter";
    }

    @PostMapping(value = "/admin/filterLogs")
    public String filterLogsPost(@ModelAttribute("logObject") LogFilterBackingObject lo, @Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors, Model model)
    {
        HashMap<Long, UserCore> userCoreHashMap = userService.findMatchesFromFilter(user);

        List<Log> logList = logService.returnMatchingLogs(lo,(userCoreHashMap.keySet()));
        model.addAttribute("MatchedUsersMap",userCoreHashMap);
        model.addAttribute("logList",logList);

        if (lo.getOperation() == null){return "redirect:/admin/displayAllLogs";}
        switch (lo.getOperation())
        {
            case AddUser:
                return "redirect:/admin/FilterAddLogs";

        }
        return "logging/logFilter";
    }

    @GetMapping(value = "admin/displayAllLogs")
    public String displayAllLogs(@SessionAttribute("logList") List<Log> logs, @SessionAttribute("MatchedUsersMap") HashMap<Long,UserCore> users)
    {
        return "/logging/displayAllLogs";
    }

    @GetMapping(value = "admin/FilterAddLogs")
    public String filterAddLogs(Model model)
    {

        model.addAttribute("UserFiltering",true);
        model.addAttribute("Title","Select Created User Details");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/FilterAddLogs");
        return "/admin/userFilterFields";
    }

    @PostMapping(value = "admin/FilterAddLogs")
    public String FilterAddLogs(@SessionAttribute("logList") List<Log> logs, @ModelAttribute("usercore") UserFormBackingObject user, RedirectAttributes flash)
    {
        List<AddUser> addLogs = new ArrayList<>();
        for (Log l:logs)
        {
            AddUser event = (AddUser)l.returnLoggedOperation();
            if (event.meetsFilter(user)){addLogs.add(event);}
        }
        flash.addFlashAttribute("addLogs",addLogs);
        return "redirect:/admin/DisplayAddLogs";
    }

    @GetMapping(value = "admin/DisplayAddLogs")
    public String DisplayAddLogs(@SessionAttribute("MatchedUsersMap") HashMap<Long, UserCore> matchedUsers,@ModelAttribute("addLogs")List<AddUser> addLogs, Model model)
    {
        return "logging/AddUser/DisplayAddUserLogs";
    }

}
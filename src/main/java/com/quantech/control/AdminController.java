package com.quantech.control;

import com.quantech.config.SecurityRoles;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Category;
import com.quantech.model.Doctor;
import com.quantech.model.Risk;
import com.quantech.model.Ward;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.RiskService.RiskServiceImpl;
import com.quantech.service.UserService.UserServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;
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
import javax.validation.Valid;

@Controller
public class AdminController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    WardServiceImpl wardService;

    @Autowired
    RiskServiceImpl riskService;

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping(value="/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping(value="/admin/createUser")
    public String createUser(Model model) {
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/createUser");
        return "admin/createUser";
    }

    @PostMapping(value="/admin/createUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors) {
        userService.CheckValidity(result, true, user);
        if (errors.hasErrors()) {
            return "admin/createUser";
        } else {
            UserCore newUser = user.ToUserCore();
            userService.saveUser(newUser);
            if (newUser.hasAuth(SecurityRoles.Doctor)) {
                Doctor newDoc = new Doctor(newUser);
                doctorService.saveDoctor(newDoc);
            }
            return "redirect:/admin";
        }
    }

    @GetMapping(value="/admin/editUsers")
    public String editUsers() {
        return "admin/editUsers";
    }

    @GetMapping(value="/admin/manageWards")
    public String manageWards(Model model) {
        model.addAttribute("newWard", new Ward());
        model.addAttribute("wards", wardService.getAllWards());
        return "admin/manageWards";
    }

    @PostMapping(value="/admin/manageWards")
    public String addWard(@ModelAttribute Ward ward, Model model) {
        wardService.saveWard(ward);
        model.addAttribute("newWard", new Ward());
        model.addAttribute("wards", wardService.getAllWards());
        return "admin/manageWards";
    }

    @GetMapping(value="/admin/manageCategories")
    public String manageCategories(Model model) {
        model.addAttribute("newCat", new Category());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "admin/manageCategories";
    }
    @RequestMapping(value="/admin/addCategory", method=RequestMethod.POST)
    public String addCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/manageCategories";
    }

    @GetMapping(value="/admin/manageRisks")
    public String manageRisks(Model model) {
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/manageRisks";
    }

    @PostMapping(value="/admin/manageRisks")
    public String addRisk(@ModelAttribute Risk risk, Model model) {
        riskService.saveRisk(risk);
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/manageRisks";
    }

}
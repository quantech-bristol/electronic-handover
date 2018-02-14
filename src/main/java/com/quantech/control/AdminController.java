package com.quantech.control;

import com.quantech.config.SecurityRoles;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Doctor;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.UserService.UserServiceImpl;
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

    @GetMapping(value="/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping(value="/admin/createUser")
    public String createUser(Model model) {
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/createUser");
        return "user/createUser";
    }

    @PostMapping(value="/admin/createUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Model model, Errors errors) {
        userService.CheckValidity(result, true, user);
//        if (errors.hasErrors()) {
//            model.addAttribute("postUrl", "/admin/createUser");
//            model.addAttribute("title", "CreateUser");
//            model.addAttribute("ShowRoles", true);
//            return "user/createUser";
//        }
//        else {
        UserCore newUser = user.ToUserCore();
        userService.saveUser(newUser);
        if(newUser.hasAuth(SecurityRoles.Doctor)) {
            Doctor newDoc = new Doctor(newUser);
            doctorService.saveDoctor(newDoc);
        }
        return "redirect:/admin";
//        }
    }

    @GetMapping(value="/admin/editUsers")
    public String editUsers() {
        return "user/editUsers";
    }

    @GetMapping(value="/admin/createWard")
    public String createWard() {
        return "ward/createWard";
    }

    @GetMapping(value="/admin/editWards")
    public String editWards() {
        return "ward/editWards";
    }

    @GetMapping(value="/admin/manageCategories")
    public String manageCategories() {
        return "job/manageCategories";
    }

    @GetMapping(value="/admin/manageRisks")
    public String manageRisks() {
        return "job/manageRisks";
    }

}
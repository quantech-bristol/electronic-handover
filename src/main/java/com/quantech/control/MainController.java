package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Patient;
import com.quantech.model.user.ChangePassword;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.service.PatientService.PatientServiceImpl;
import com.quantech.service.UserService.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserService userService;

    @Autowired
    PatientServiceImpl patientService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home(Model model) {
        UserCore user =  (UserCore)authenticator.getAuthentication().getPrincipal();
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        if (user.isDoctor()) {
            return "misc/home";
        } else if (user.isAdmin()) {
            return "redirect:/admin";
        } else {
            return "misc/home";
        }
    }

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() { return "misc/login"; }

    @RequestMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {

        // delete the sensitive patient data when user logs out
        File pdf = new File("pdfout.pdf");
        pdf.delete();

        // handle logout if previously logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping(value="/settings")
    public String editSettings(Model model) {
        ChangePassword change = new ChangePassword();
        model.addAttribute("change", change);
        return "user/settings";
    }

    @PostMapping(value="/settings")
    public String changePassword(@ModelAttribute ChangePassword user) {
        UserCore newUser = (UserCore) authenticator.getAuthentication().getPrincipal();
        if (user.getInitial().equals(user.getConfirm()) && newUser.getPassword().equals(user.getCurrent())) {
            newUser.setPassword(user.getInitial());
            userService.saveUser(newUser);
            return "redirect:/";
        }
        return "redirect:/settings";
    }

}
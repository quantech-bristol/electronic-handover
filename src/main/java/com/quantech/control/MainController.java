package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
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

@Controller
public class MainController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserService userService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        UserCore user =  (UserCore)authenticator.getAuthentication().getPrincipal();
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
        UserFormBackingObject user = new UserFormBackingObject((UserCore) authenticator.getAuthentication().getPrincipal());
        user.setPassword(null);
        model.addAttribute("user", user);
        return "user/settings";
    }

    @PostMapping(value="/settings")
    public String changePassword(@ModelAttribute UserFormBackingObject user) {
        UserCore newUser = (UserCore) authenticator.getAuthentication().getPrincipal();
        newUser.setPassword(user.getPassword());
        userService.saveUser(newUser);
        return "redirect:/settings";
    }

}
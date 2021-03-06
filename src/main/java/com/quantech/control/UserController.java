package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.user.ChangePassword;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserEntry;
import com.quantech.service.UserService.UserService;
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
import java.io.File;

@Controller

public class UserController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserService userService;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login() { return "user/login"; }

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
    public String changePassword(@ModelAttribute("change") ChangePassword returnedUser, BindingResult r, Model model, Errors error) {
        UserCore currentUser = ((UserEntry)authenticator.getAuthentication().getPrincipal()).getUserCore();
        if (returnedUser.getInitial().equals(returnedUser.getConfirm()) && userService.checkUserPassword(currentUser,returnedUser.getCurrent())) {
            currentUser.setPassword(returnedUser.getInitial());
            userService.saveUser(currentUser,true);
            return "redirect:/";
        }
        r.rejectValue("current","current.change","Either your passwords did not match, or your current password was input incorrectly.");
        return "user/settings";
    }

}

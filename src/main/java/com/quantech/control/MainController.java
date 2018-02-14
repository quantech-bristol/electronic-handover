package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.user.UserCore;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    IAuthenticationFacade authenticator;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home(Model model) {
        UserCore user =  (UserCore)authenticator.getAuthentication().getPrincipal();
        if (user.isDoctor()) {
            //Get all patients with an uncompleted job related to the doctor
        }
        if (user.isAdmin()) {
            //
        }
        return "misc/home";
    }

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() { return "misc/login"; }

    @RequestMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {

        // handle logout if previously logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping(value="/settings")
    public String editSettings() {
        return "user/settings";
    }

}
package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.misc.Category;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Controller
public class MainController {

    @Autowired
    IAuthenticationFacade authenticator;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
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


}

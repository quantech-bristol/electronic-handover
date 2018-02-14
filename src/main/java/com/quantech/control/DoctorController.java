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
public class DoctorController {

    @Autowired
    IAuthenticationFacade authenticator;

    @GetMapping(value="/createHandover")
    public String createHandover() {
        return "job/handover";
    }

}
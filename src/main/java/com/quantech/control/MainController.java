package com.quantech.control;

import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {

    @Autowired
    IAuthenticationFacade authenticator;

    @RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseBody
    public String home() {
        return "Hello World";
    }

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() { return "misc/login"; }
}

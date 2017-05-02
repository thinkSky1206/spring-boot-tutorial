package com.liuwp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: liuwuping
 * Date: 17/4/30
 * Description:
 */
@Controller
public class LoginController {


    @RequestMapping(value={"/", "/login"})
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/home")
    public String home(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "home";
    }

    @RequestMapping(value = "/fail")
    public String error(){
        return "error";
    }
}

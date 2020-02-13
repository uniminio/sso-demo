package com.para.hezz.sso.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        if (request.getSession().getAttribute("user")==null||request.getSession().getAttribute("user").equals("")) {
            return "redirect:/callback";
        }
        return "index";
    }
}

package com.eoe.jds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/jds")
    @ResponseBody
    public String index() {
        return "Team EoE 가 만든 Just Do Study 입니다.";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/controller/list";
    }
}

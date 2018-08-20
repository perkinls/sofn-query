package com.sofn.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloWeb {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("advanced")
    public String advanced(){
        return "advanced";
    }

    @RequestMapping("result")
    public String result(){
        return "result";
    }

}

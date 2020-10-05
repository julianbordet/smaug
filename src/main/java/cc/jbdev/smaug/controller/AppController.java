package cc.jbdev.smaug.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping("/")
    public String viewHome(){
        return "home";
    }


}

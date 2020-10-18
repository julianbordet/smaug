package cc.jbdev.smaug.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myprojects")
public class MyProjectsController {

    @GetMapping("/main")
    public String showMyProjects(){

        return "myprojectspage";
    }


}

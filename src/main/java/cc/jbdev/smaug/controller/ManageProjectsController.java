package cc.jbdev.smaug.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manageprojects")
public class ManageProjectsController {


    @GetMapping("/main")
    public String showManageProjects(){

        return "manageprojectspage";
    }

}

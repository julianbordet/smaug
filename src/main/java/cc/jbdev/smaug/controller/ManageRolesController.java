package cc.jbdev.smaug.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manageroles")
public class ManageRolesController {


    @GetMapping("/main")
    public String showManageRoles(){

        return "managerolespage";
    }

}

package cc.jbdev.smaug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.attribute.UserDefinedFileAttributeView;

@Controller
public class AppController {

    /*@Autowired
    UserService service = new UserService();*/

    @GetMapping("/dashboard")
    public String viewDashboard(Model theModel){

        /*//get the specific user
        AppUser tempUser = new AppUser();
        tempUser = service.getUser();

        //card1
        //get total bugs owned by user
        int bugTotal = service.getTotalBugs(tempUser);
        theModel.addAttribute("userOpenBugCount", bugTotal);

        //card2
        //get bug severity list
        List<Bug> bugList = service.getBugList(tempUser);
        //figure out how to display donut graph

        //card3
        //to know how many are due
        int bugsDue = service.countDueBugs(bugList);
        //to know how many are not due
        int bugsNotDue = service.countNotDueBugs(bugList);

        theModel.addAttribute("userDueBugCount", bugsDue);
        theModel.addAttribute("userNotDueBugCount", bugsNotDue);
        //COULD USE TWO JOINS WITH UNION IN SQL TO GET THE FULL LIST OF BUGS
        //WITH TAGS ABOUT WHETHER THEY ARE DUE OR NOT
        //figure out how to display donut graph

        //card4
        int totalBugsCrushed = service.getTotalBugsCrushed(tempUser);
        theModel.addAttribute("userTotalBugsCrushed", totalBugsCrushed);*/

        return "dashboardpage";
    }

}
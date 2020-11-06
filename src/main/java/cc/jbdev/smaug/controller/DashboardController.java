package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;


    @GetMapping("/main")
    public String viewDashboard(Model theModel){

        UserUtility userUtility = new UserUtility();

        ///card1
        ///get total ACTIVE bugs owned by user, add a counter for bugs due and not due to the model.
        List<Integer> bugsCounter = bugService.getActiveDueAndNotDueBugsCounterForUser(userUtility.getMyUserName());

        theModel.addAttribute("userBugsDue", bugsCounter.get(0));
        theModel.addAttribute("userBugsNotDue", bugsCounter.get(1));
        //////////////

        ///card2
        ///get total ACTIVE bugs owned by user, then add to the model the count for
        ///each type of severity
        List<Integer> bugsSeverity = bugService.getActiveBugsCounterBySeverity(userUtility.getMyUserName());

        theModel.addAttribute("userCriticalBugCount", bugsSeverity.get(0));
        theModel.addAttribute("userMajorBugCount", bugsSeverity.get(1));
        theModel.addAttribute("userMinorBugCount", bugsSeverity.get(2));
        theModel.addAttribute("userLowBugCount", bugsSeverity.get(3));
        //////////////

        ///card3
        ///show a chart for bug priority
        List<Integer> bugsPriority = bugService.getActiveBugsCounterByPriority(userUtility.getMyUserName());

        theModel.addAttribute("userHighPriority", bugsPriority.get(0));
        theModel.addAttribute("userMediumPriority", bugsPriority.get(1));
        theModel.addAttribute("userLowPriority", bugsPriority.get(2));
        /////////////

        ///card4
        ///show total number of bugs by project
        List<Project> userActiveProjectList = projectService.getActiveProjectsListForUser(userUtility.getMyUserName());
        HashMap<String, Integer> mapProjectsWithBugCounter = bugService.getMapProjectNameBugCounter(userActiveProjectList, userUtility.getMyUserName());

        int counter = 1;

        for (Map.Entry mapEntry : mapProjectsWithBugCounter.entrySet()){

            theModel.addAttribute("project" + counter + "Name", mapEntry.getKey());
            theModel.addAttribute("project" + counter + "Bugs", mapEntry.getValue());

            counter++;
        }
        /////////////


        //--------------------------------//
        //  FOR TESTING PURPOSES ONLY    //
        //------------------------------//




        //----------------------------//


        return "dashboardpage";
    }
}

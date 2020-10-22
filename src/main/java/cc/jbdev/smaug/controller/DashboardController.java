package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.entity.ProjectBugCounter;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;

    @GetMapping("/main")
    public String viewDashboard(Model theModel){


        ///Helper code to get the username of the currently logged in user:
        String myUserName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            myUserName = ((UserDetails)principal).getUsername();
        } else {
            myUserName = principal.toString();
        }
        ///


        /////IDEAL SITUATION
        ///card1
        ///get total ACTIVE bugs owned by user, add a counter for bugs due and not due to the model.
        List<Integer> bugsCounter = bugService.getActiveDueAndNotDueBugsCounterForUser(myUserName);

        theModel.addAttribute("userBugsDue", bugsCounter.get(0));
        theModel.addAttribute("userBugsNotDue", bugsCounter.get(1));
        //////////////

        /////IDEAL SITUATION
        ///card2
        ///get total ACTIVE bugs owned by user, then add to the model the count for
        ///each type of severity
        List<Integer> bugsSeverity = bugService.getActiveBugsCounterBySeverity(myUserName);

        theModel.addAttribute("userCriticalBugCount", bugsSeverity.get(0));
        theModel.addAttribute("userMajorBugCount", bugsSeverity.get(1));
        theModel.addAttribute("userMinorBugCount", bugsSeverity.get(2));
        theModel.addAttribute("userLowBugCount", bugsSeverity.get(3));
        //////////////

        /////IDEAL SITUATION
        ///card3
        ///show a chart for bug priority
        List<Integer> bugsPriority = bugService.getActiveBugsCounterByPriority(myUserName);

        theModel.addAttribute("userHighPriority", bugsPriority.get(0));
        theModel.addAttribute("userMediumPriority", bugsPriority.get(1));
        theModel.addAttribute("userLowPriority", bugsPriority.get(2));
        /////////////

        /////IDEAL SITUATION
        ///card4
        ///show total number of bugs by project
        List<Project> userActiveProjectList = projectService.getActiveProjectsListForUser(myUserName);
        HashMap<String, Integer> mapProjectsWithBugCounter = bugService.getMapProjectNameBugCounter(userActiveProjectList, myUserName);

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

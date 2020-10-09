package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;

    @GetMapping("/dashboard")
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

        ///card1
        ///get total ACTIVE bugs owned by user, add it to the model.
        List<Bug> userActiveBugList = bugService.getActiveBugListForUser(myUserName);
        int userActiveBugCount = userActiveBugList.size();
        theModel.addAttribute("userOpenBugCount", userActiveBugCount);
        ///

        ///card2
        ///get total ACTIVE bugs owned by user, then add to the model the count for
        ///each type of severity
        int criticalBugCount = 0;
        int majorBugCount = 0;
        int minorBugCount = 0;
        int lowBugCount = 0;

        for(Bug bug : userActiveBugList) {
            if (bug.getBugSeverity().equals("CRITICAL")){
                criticalBugCount++;
            }
            if (bug.getBugSeverity().equals("MAJOR")){
                majorBugCount++;
            }
            if (bug.getBugSeverity().equals("MINOR")){
                minorBugCount++;
            }
            if (bug.getBugSeverity().equals("LOW")){
                lowBugCount++;
            }
        }

        theModel.addAttribute("userCriticalBugCount", criticalBugCount);
        theModel.addAttribute("userMajorBugCount", majorBugCount);
        theModel.addAttribute("userMinorBugCount", minorBugCount);
        theModel.addAttribute("userLowBugCount", lowBugCount);
        ///


        ///card3
        ///show a chart for bug priority
        int userHighPriorityBugCount = 0;
        int userMediumPriorityBugCount = 0;
        int userLowPriorityBugCount = 0;

        for(Bug bug : userActiveBugList) {
            if (bug.getBugPriority().equals("HIGH")){
                userHighPriorityBugCount++;
            }
            if (bug.getBugPriority().equals("MEDIUM")){
                userMediumPriorityBugCount++;
            }
            if (bug.getBugPriority().equals("LOW")){
                userLowPriorityBugCount++;
            }
        }
        theModel.addAttribute("userHighPriority", userHighPriorityBugCount);
        theModel.addAttribute("userMediumPriority", userMediumPriorityBugCount);
        theModel.addAttribute("userLowPriority", userLowPriorityBugCount);
        ///


        ///card4
        ///show total number of bugs crushed by the user
        List<Bug> userTotalBugList = bugService.getBugListForUser(myUserName);
        int userTotalBugCount = userTotalBugList.size();
        int userCrushedBugCount = userTotalBugCount - userActiveBugCount;
        theModel.addAttribute("userCrushedBugCount", userCrushedBugCount);
        ///

        //--------------------------------//
        //  FOR TESTING PURPOSES ONLY    //
        //------------------------------//
        List<Bug> allBugsList = bugService.getBugList();
        List<Project> allProjectsList = projectService.getProjectsList();
        List<Bug> specificUserBugList = bugService.getBugListForUser(myUserName);
        int specificUserbugCount = specificUserBugList.size();
        List<Bug> specificUserActiveBugList = bugService.getActiveBugListForUser(myUserName);
        int specificUserActiveBugCount = specificUserActiveBugList.size();
        ///
        ///
        ///
        ///
        return "dashboardpage";
    }


}

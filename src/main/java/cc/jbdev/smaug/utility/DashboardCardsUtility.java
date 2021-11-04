package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DashboardCardsUtility {

    public void generateCardBugsDue(BugService bugService, UserUtility userUtility, Model theModel){

        List<Integer> bugsCounter = bugService.getActiveDueAndNotDueBugsCounterForUser(userUtility.getMyUserName());

        theModel.addAttribute("userBugsDue", bugsCounter.get(0));
        theModel.addAttribute("userBugsNotDue", bugsCounter.get(1));
    }

    public void generateCardSeverityBreakdown(BugService bugService, UserUtility userUtility, Model theModel){

        List<Integer> bugsSeverity = bugService.getActiveBugsCounterBySeverity(userUtility.getMyUserName());

        theModel.addAttribute("userCriticalBugCount", bugsSeverity.get(0));
        theModel.addAttribute("userMajorBugCount", bugsSeverity.get(1));
        theModel.addAttribute("userMinorBugCount", bugsSeverity.get(2));
        theModel.addAttribute("userLowBugCount", bugsSeverity.get(3));

    }

    public void generateCardPriorityBreakdown(BugService bugService, UserUtility userUtility, Model theModel){

        List<Integer> bugsPriority = bugService.getActiveBugsCounterByPriority(userUtility.getMyUserName());

        theModel.addAttribute("userHighPriority", bugsPriority.get(0));
        theModel.addAttribute("userMediumPriority", bugsPriority.get(1));
        theModel.addAttribute("userLowPriority", bugsPriority.get(2));

    }

    public void generateCardBugsByProject(BugService bugService, UserUtility userUtility, ProjectService projectService, Model theModel){

        List<Project> userActiveProjectList = projectService.getActiveProjectsListForUser(userUtility.getMyUserName());
        HashMap<String, Integer> mapProjectsWithBugCounter = bugService.getMapProjectNameBugCounter(userActiveProjectList, userUtility.getMyUserName());

        int counter = 1;

        for (Map.Entry mapEntry : mapProjectsWithBugCounter.entrySet()){

            theModel.addAttribute("project" + counter + "Name", mapEntry.getKey());
            theModel.addAttribute("project" + counter + "Bugs", mapEntry.getValue());

            counter++;
        }

    }

}

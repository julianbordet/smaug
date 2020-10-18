package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

        ///card1
        ///get total ACTIVE bugs owned by user, add a counter for bugs due and not due to the model.
        List<Bug> userActiveBugList = bugService.getActiveBugListForUser(myUserName);
        int bugsDue = 0;
        int bugsNotDue = 0;

        for(Bug bug : userActiveBugList){

            String bugDueDateInString = bug.getBugDueDate();

            Date dueDateInDate = new Date();

            try {
                dueDateInDate = new SimpleDateFormat("yyyy-MM-dd").parse(bugDueDateInString);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            Date today = new Date();

            if (dueDateInDate.after(today)){
                bugsNotDue++;
            } else {
                bugsDue++;
            }
        }
        theModel.addAttribute("userBugsDue", bugsDue);
        theModel.addAttribute("userBugsNotDue", bugsNotDue);
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
        ///show total number of bugs by project

        //I needed a helper class that would hold a key-value pair, so I created the ProjectBugCounter class.
        //Then I create a list of these, which will be helpful down the line
        List<ProjectBugCounter> projectBugCounter = new ArrayList<ProjectBugCounter>();

        //get all active projects for the logged in user
        List<Project> userActiveProjects = projectService.getActiveProjectsListForUser(myUserName);

        //iterate over each active project the user is involved
        for(Project project : userActiveProjects){

            if (project.getIsActive() == 1) {

                //get a list of bugs for the specific project, save them on a list
                List<Bug> bugListForProject = bugService.getProjectActiveBugsByUser(project, myUserName);

                //from the list on the previous step, create an int that holds the amount of bugs in the list
                int bugCounterForProject = bugListForProject.size();

                //get the project name
                String projectName = project.getProjectName();

                //create a ProjectBugCounter class using the previous two values, the project name and the
                //amount of bugs it holds
                ProjectBugCounter projectCounter = new ProjectBugCounter(projectName, bugCounterForProject);

                //add to the list of ProjectCounters.
                projectBugCounter.add(projectCounter);
            }
        }

        //at this point I have a list (projectBugCounter) that holds all projects and their respective bug count
        //only thing left is iterate over them to add them to the Model.
        int counter = 1;
        for(ProjectBugCounter project : projectBugCounter){

            theModel.addAttribute("project" + counter + "Name", project.getProjectName());
            theModel.addAttribute("project" + counter + "Bugs", project.getActiveBugCount());

            counter++;
        }

        return "dashboardpage";
    }















    ///

    //--------------------------------//
    //  FOR TESTING PURPOSES ONLY    //
    //------------------------------//


    //-------------------------------

    ///
    ///
    ///
    ///

}

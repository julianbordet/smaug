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



        //--------------------------------//
        //  FOR TESTING PURPOSES ONLY    //
        //------------------------------//


        //-------------------------------


        return "dashboardpage";
    }

















}

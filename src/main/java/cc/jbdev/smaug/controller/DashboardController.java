package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.DashboardCardsUtility;
import cc.jbdev.smaug.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;

    @Autowired
    DashboardCardsUtility dashboardCardsUtility;


    @GetMapping("/main")
    public String viewDashboard(Model theModel){

        UserUtility userUtility = new UserUtility();

        //CARD 1
        //Show a card that represents the total number of bugs assigned to the user, broken down by due/not due.
        dashboardCardsUtility.generateCardBugsDue(bugService, userUtility, theModel);

        //CARD 2
        //Show a card that breaks down the active bugs assigned to the user by severity.
        dashboardCardsUtility.generateCardSeverityBreakdown(bugService, userUtility, theModel);

        //CARD 3
        //Show a card that breaks down the active bugs assigned to the user by priority.
        dashboardCardsUtility.generateCardPriorityBreakdown(bugService, userUtility, theModel);

        //CARD 4
        ///Show a card that breaks down the bugs assigned to the user by Project.
        dashboardCardsUtility.generateCardBugsByProject(bugService, userUtility, projectService, theModel);

        return "/dashboardPage";
    }
}

package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
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
@RequestMapping("/mybugs")
public class MyBugsController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;



    @GetMapping("/main")
    public String showMyBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){


        ///Helper code to get the username of the currently logged in user:
        String myUserName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            myUserName = ((UserDetails)principal).getUsername();
        } else {
            myUserName = principal.toString();
        }
        ///


        int currentPage = page.orElse(1);
        int pageSize = size.orElse(15);

        Page<Bug> bugPage = bugService.findPaginatedUserActiveBugs(PageRequest.of(currentPage - 1, pageSize), myUserName);

        theModel.addAttribute("bugPage", bugPage);

        int totalPages = bugPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }

        ////////// Add a list of applicable project names to the model


        List<Bug> myBugList = bugService.getActiveBugListForUser(myUserName);
        List<String> listOfApplicableProjectNames = new ArrayList<>();

        for (Bug bug : myBugList){
            Project project = projectService.getProjectById(bug.getProjectId());
            String projectName = project.getProjectName();
            listOfApplicableProjectNames.add(projectName);
        }

        theModel.addAttribute("projectNames", listOfApplicableProjectNames);

        ///////

        return "mybugspage";
    }


    @GetMapping("/showBugDetail")
    public String showBugDetail(@RequestParam("bugId") int theId, Model theModel) {

        Bug bugClicked = bugService.getBugByBugId(theId);

        theModel.addAttribute("theBug", bugClicked);

       /* //Add a copy of the bug to use for reference when checking changes for the transaction history
        Bug bugOriginalState = new Bug();
        bugOriginalState.setBugTitle(bugClicked.getBugTitle());
        bugOriginalState.setBugDescription(bugClicked.getBugDescription());
        bugOriginalState.setStepsToReproduce(bugClicked.getStepsToReproduce());
        bugOriginalState.setBugSeverity(bugClicked.getBugSeverity());
        bugOriginalState.setDateCreated(bugClicked.getDateCreated());
        bugOriginalState.setBugStatus(bugClicked.getBugStatus());
        bugOriginalState.setBugResponsibleDev(bugClicked.getBugResponsibleDev());
        bugOriginalState.setBugDueDate(bugClicked.getBugDueDate());
        bugOriginalState.setBugPriority(bugClicked.getBugPriority());

        theModel.addAttribute("theOriginalBug", bugOriginalState);
        /////////*/

        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        List<Project> activeProjects = projectService.getActiveProjects();
        theModel.addAttribute("projectList", activeProjects);


        //Get a list of BugTransactions for the bug, to show in the bug transaction history
        List<BugTransaction> bugTransactionList = bugService.getBugTransactionsByBugId(theId);
        theModel.addAttribute("bugTransactions", bugTransactionList);


        return "showBugDetailPage";
    }



    @PostMapping("/updateBug")
    public String updateBug(@ModelAttribute("theBug") Bug theBug,
                            @RequestParam(value = "bugOriginalTitle") String bugOriginalTitle,
                            @RequestParam(value = "bugOriginalDescription") String bugOriginalDescription,
                            @RequestParam(value = "bugOriginalProjectId") String bugOriginalProjectId,
                            @RequestParam(value = "bugOriginalSeverity") String bugOriginalSeverity,
                            @RequestParam(value = "bugOriginalPriority") String bugOriginalPriority,
                            @RequestParam(value = "bugOriginalStatus") String bugOriginalStatus,
                            @RequestParam(value = "bugOriginalResponsibleDev") String bugOriginalResponsibleDev,
                            @RequestParam(value = "bugOriginalDueDate") String bugOriginalDueDate,
                            @RequestParam(value = "bugOriginalStepsToReproduce") String bugOriginalStepsToReproduce)
    {

        ///Helper code to get the username of the currently logged in user:
        String myUserName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            myUserName = ((UserDetails)principal).getUsername();
        } else {
            myUserName = principal.toString();
        }
        ///



        ///// Compare updated bug with original bug state

        String changesMade = "";
        String changesMadeDetail = "";


        if ( !(theBug.getBugTitle().equals(bugOriginalTitle)) ){
                changesMade += "Title updated. ";
                changesMadeDetail += "Title updated from: " + bugOriginalTitle + " to: " + theBug.getBugTitle();
                changesMadeDetail += "\n";
        }

        if ( !(theBug.getBugDescription().equals(bugOriginalDescription)) ){
                changesMade += "Description updated. ";
                changesMadeDetail += "Description updated: " + theBug.getBugDescription();
                changesMadeDetail += "\n";
        }


        if ( !(theBug.getProjectId() ==  Integer.parseInt(bugOriginalProjectId) ) ){
            changesMade += "Assigned project changed. ";
            changesMadeDetail += "Project reassigned from: " +
                    projectService.getProjectById(Integer.parseInt(bugOriginalProjectId)).getProjectName() +
                    " to: " + projectService.getProjectById(theBug.getProjectId()).getProjectName() +
                    "\n";
        }

        if ( !(theBug.getBugSeverity().equals(bugOriginalSeverity)) ){
            changesMade += "Severity updated. ";
            changesMadeDetail += "Bug severity updated from: " + bugOriginalSeverity + " to: " +
                    theBug.getBugSeverity() +
                    "\n";
        }

        if ( !(theBug.getBugPriority().equals(bugOriginalPriority)) ){
            changesMade += "Priority updated. ";
            changesMadeDetail += "Priority updated from: " + bugOriginalPriority + " to: " +
                    theBug.getBugPriority() +
                    "\n";
        }

        int updatedBugStatus = theBug.getBugStatus();
        int originalBugStatus = Integer.parseInt(bugOriginalStatus);

        if ( !(updatedBugStatus ==  originalBugStatus) ){
            changesMade += "Status updated. ";

            if(updatedBugStatus == 1) {
                changesMadeDetail += "Status updated from: In Progress to Fixed\n";
            } else {
                changesMadeDetail += "Status updated from: Fixed to In Progress\n";
            }
        }

        if ( !(theBug.getBugResponsibleDev().equals(bugOriginalResponsibleDev)) ){
            changesMade += "Responsible dev changed. ";
            changesMadeDetail += "Responsible dev changed from: " + bugOriginalResponsibleDev + " to: " + theBug.getBugResponsibleDev() +
                    "\n";
        }

        if ( !(theBug.getBugDueDate().equals(bugOriginalDueDate)) ){
            changesMade += "Due date updated. ";
            changesMadeDetail += "Due date updated from: " + bugOriginalDueDate + " to: " + theBug.getBugDueDate() +
                    "\n";
        }

        if ( !(theBug.getStepsToReproduce().equals(bugOriginalStepsToReproduce)) ){
            changesMade += "Steps to reproduce updated. ";
            changesMadeDetail += "Steps to reproduce updated: " + theBug.getStepsToReproduce();
        }









        //Create a new bug with the original state of the bug


        //-- BLE BLE BLE OTROS CHECKS DE IFS PARA LOS OTROS FIELDS



        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today);


        //check if changesMade variable is empty

        if ( !(changesMade.isEmpty()) ){

            //Create a new transaction and add it to the bug
            BugTransaction newBugTransaction = new BugTransaction();
            newBugTransaction.setDate(todayInString);
            newBugTransaction.setTransaction(changesMade);
            newBugTransaction.setTransactionId(0);
            newBugTransaction.setTransactionCreatedBy(myUserName);
            newBugTransaction.setTransactionDetail(changesMadeDetail);
            theBug.addBugTransactions(newBugTransaction);
        }



        //////


        bugService.save(theBug);

        return "redirect:/mybugs/main";
    }


    @GetMapping("/newbug")
    public String newBug(Model theModel){


        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        List<Project> activeProjects = projectService.getActiveProjects();
        theModel.addAttribute("projectList", activeProjects);

        Bug bug = new Bug();
        theModel.addAttribute("theBug", bug);

        return "newBugPage";
    }

    @PostMapping("/createBug")
    public String createBug(@ModelAttribute("theBug") Bug theBug){

        ///Helper code to get the username of the currently logged in user:
        String myUserName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            myUserName = ((UserDetails)principal).getUsername();
        } else {
            myUserName = principal.toString();
        }
        ///




        theBug.setBugId(0);
        theBug.setBugStatus(2);
        theBug.setBugCreatedBy(myUserName);


        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today);
        theBug.setDateCreated(todayInString);


        //Create a new transaction and add it to the bug
        BugTransaction newBugTransaction = new BugTransaction();
        newBugTransaction.setDate(todayInString);
        newBugTransaction.setTransaction("Bug created");
        newBugTransaction.setTransactionId(0);
        newBugTransaction.setTransactionCreatedBy(myUserName);
        newBugTransaction.setTransactionDetail("Bug created");
        theBug.addBugTransactions(newBugTransaction);
        //////



        bugService.save(theBug);

        return "redirect:/mybugs/main";
    }

    @GetMapping("/deletebug")
    public String deleteBug(@RequestParam("bugId") int bugId){

        //Bug theBugToDelete = bugService.getBugByBugId(bugId);


        bugService.delete(bugId);

        return "redirect:/mybugs/main";
    }

}

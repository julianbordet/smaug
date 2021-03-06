package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.UserUtility;
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
@RequestMapping("/mybugs")
public class MyBugsController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;



    @GetMapping("/main")
    public String showMyBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){

        UserUtility userUtility = new UserUtility();


        //Adds a paginated list of active bugs for currently logged in user to the Model
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);

        Page<Bug> bugPage = bugService.paginate(PageRequest.of(currentPage - 1, pageSize), bugService.getActiveBugListForUser(userUtility.getMyUserName()));
        theModel.addAttribute("bugPage", bugPage);

        int totalPages = bugPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
        ////

        ////////// Add a list of applicable project names to the model
        List<Bug> myBugList = bugService.getActiveBugListForUser(userUtility.getMyUserName());
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

    @GetMapping("/inactivebugs")
    public String showInactiveBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size){

        UserUtility userUtility = new UserUtility();


        //paginate inactive bugs
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);

        Page<Bug> inactiveBugPage = bugService.paginate(PageRequest.of(currentPage - 1, pageSize), bugService.getListOfInactiveBugsForUser(userUtility.getMyUserName()));
        theModel.addAttribute("inactiveBugPage", inactiveBugPage);

        int totalPages = inactiveBugPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
        ////////

        ////////// Add a list of applicable project names to the model
        List<Bug> myBugList = bugService.getListOfInactiveBugsForUser(userUtility.getMyUserName());
        List<String> listOfApplicableProjectNames = new ArrayList<>();

        for (Bug bug : myBugList){
            Project project = projectService.getProjectById(bug.getProjectId());
            String projectName = project.getProjectName();
            listOfApplicableProjectNames.add(projectName);
        }

        theModel.addAttribute("projectNames", listOfApplicableProjectNames);
        ///////





        return "inactiveBugsPage";
    }


    @GetMapping("/showBugDetail")
    public String showBugDetail(@RequestParam("bugId") int theId, Model theModel, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {


        Bug bugClicked = bugService.getBugByBugId(theId);
        theModel.addAttribute("theBug", bugClicked);

        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        List<Project> activeProjects = projectService.getActiveProjects();
        theModel.addAttribute("projectList", activeProjects);


        //pagination for bug transaction
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        //Has own pagination method since the results need to be in reverse order, so that newer transactions
        //appear first in the list
        Page<BugTransaction> bugTransactions = bugService.findPaginatedBugTransactions(PageRequest.of(currentPage - 1, pageSize), theId);
        ///

        theModel.addAttribute("bugTransactions", bugTransactions);

        int totalPages = bugTransactions.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
        ///////

        return "showBugDetailPage";
    }



    @PostMapping("/updateBug")
    public String updateBug(@ModelAttribute("theBug") Bug updatedBug,
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

        UserUtility userUtility = new UserUtility();

        ///TESTO
        Bug originalBug = new Bug(bugOriginalTitle, bugOriginalDescription, bugOriginalProjectId,
                            bugOriginalSeverity, bugOriginalPriority, bugOriginalStatus,
                            bugOriginalResponsibleDev, bugOriginalDueDate, bugOriginalStepsToReproduce);

        ///// Compare updated bug with original bug state, create transaction if there's difference
        bugService.compareBugsAndCreateTransaction(updatedBug, originalBug);
        ///


        bugService.save(updatedBug);

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

        UserUtility userUtility = new UserUtility();


        theBug.setBugId(0);
        theBug.setBugStatus(0);
        theBug.setBugCreatedBy(userUtility.getMyUserName());


        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today);
        theBug.setDateCreated(todayInString);


        //Create a new transaction and add it to the bug
        BugTransaction newBugTransaction = new BugTransaction();
        newBugTransaction.setDate(todayInString);
        newBugTransaction.setTransaction("Bug created");
        newBugTransaction.setTransactionId(0);
        newBugTransaction.setTransactionCreatedBy(userUtility.getMyUserName());
        newBugTransaction.setTransactionDetail("Bug created");
        theBug.addBugTransactions(newBugTransaction);
        //////



        bugService.save(theBug);

        return "redirect:/mybugs/main";
    }

    @GetMapping("/deletebug")
    public String deleteBug(@RequestParam("bugId") int bugId){

        bugService.delete(bugId);

        return "redirect:/mybugs/main";
    }

}

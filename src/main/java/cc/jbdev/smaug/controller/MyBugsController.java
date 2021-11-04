package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.auxStructs.BugProjectName;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.ListManipulationUtility;
import cc.jbdev.smaug.utility.PaginationUtility;
import cc.jbdev.smaug.utility.UserUtility;
import cc.jbdev.smaug.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    ValidationUtil validationUtil;

    @Autowired
    PaginationUtility paginationUtility;

    @Autowired
    ListManipulationUtility listManipulationUtility;


    @GetMapping("/main")
    public String showMyBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){

        //0. Util object that returns the data from the user currently logged in
        UserUtility userUtility = new UserUtility();

        //1. Get list of active bugs for the user
        List<Bug> activeBugsForUser = bugService.getActiveBugListForUser(userUtility.getMyUserName());
        ////

        //2. Create an arrayList of <BugProjectName>, which is an aux struct with 2 fields, a bug and its corresponding
        //project name in String format.
        List<BugProjectName> bugProjectNames = listManipulationUtility.makeBugProjectNameList(activeBugsForUser, projectService);
        ////

        //3. Pass the BugProjectName list to the pagination utility method, which will create the page and add it
        //to the Model.
        paginationUtility.paginateBugsForMyBugsSlashMain(theModel, bugService, bugProjectNames, page, size);
        ////

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
    public String updateBug(@Valid @ModelAttribute("theBug") Bug updatedBug,
                            BindingResult theBindingResult,
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

        //1. Validate input from user
        if(theBindingResult.hasErrors() || !(validationUtil.validateUpdatedBug(updatedBug, projectService, bugService)) ){
            return "error-page";
        }
        ////////

        ///2. Copy the original status of the bug into an auxiliar Bug for comparison purposes
        Bug originalBug = new Bug(bugOriginalTitle, bugOriginalDescription, bugOriginalProjectId,
                            bugOriginalSeverity, bugOriginalPriority, bugOriginalStatus,
                            bugOriginalResponsibleDev, bugOriginalDueDate, bugOriginalStepsToReproduce);
        ////////

        //3. Compare updated bug with original bug state from previous step, create transaction (update history) if there's difference
        bugService.compareBugsAndCreateTransaction(updatedBug, originalBug);
        ////////

        //4. If we are updating a bug but not marking it as 'fixed' we need to set the bugDateFixed field to NULL to prevent the DB from rejecting it.
        if(updatedBug.getBugDateFixed() == ""){
            updatedBug.setBugDateFixed(null);
        }
        ////////

        //5. Update the bug in the DB.
        bugService.save(updatedBug);
        ////////

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
    public String createBug(@Valid @ModelAttribute("theBug") Bug theBug, BindingResult theBindingResult){

        UserUtility userUtility = new UserUtility();


        //1. Set standard fields for new bug
        bugService.setNewBugStandardParameters(theBug, userUtility);
        ////////

        //2. Validate inputs provided by user
        if(theBindingResult.hasErrors() || !(validationUtil.validateNewBug(theBug, projectService))){
            return "error-page";
        }
        ////////

        //3. Create a new transaction and add it to the bug

        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today);

        BugTransaction newBugTransaction = new BugTransaction();
        newBugTransaction.setDate(todayInString);
        newBugTransaction.setTransaction("Bug created");
        newBugTransaction.setTransactionId(0);
        newBugTransaction.setTransactionCreatedBy(userUtility.getMyUserName());
        newBugTransaction.setTransactionDetail("Bug created");
        theBug.addBugTransactions(newBugTransaction);
        ////////


        //4. Save the Bug in the DB.
        bugService.save(theBug);
        ////////

        return "redirect:/mybugs/main";
    }

    @GetMapping("/deletebug")
    public String deleteBug(@RequestParam("bugId") int bugId){

        bugService.delete(bugId);

        return "redirect:/mybugs/main";
    }

}

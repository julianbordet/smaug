package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.auxStructs.BugProjectName;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.BugTransactionUtility;
import cc.jbdev.smaug.utility.ListManipulationUtility;
import cc.jbdev.smaug.utility.PaginationUtility;
import cc.jbdev.smaug.utility.UserUtility;
import cc.jbdev.smaug.validation.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/mybugs")
public class MyBugsController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ValidationUtility validationUtility;

    @Autowired
    PaginationUtility paginationUtility;

    @Autowired
    ListManipulationUtility listManipulationUtility;

    @Autowired
    BugTransactionUtility bugTransactionUtility;


    @GetMapping("/main")
    public String showMyBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){

        //0. Util object that returns the data from the user currently logged in.
        UserUtility userUtility = new UserUtility();

        //1. Get list of active bugs for the user.
        List<Bug> activeBugsForUser = bugService.getActiveBugListForUser(userUtility.getMyUserName());

        //2. Create an arrayList of <BugProjectName>, which is an aux struct with 2 fields, a bug and its corresponding
        //project name in String format.
        List<BugProjectName> bugProjectNames = listManipulationUtility.makeBugProjectNameList(activeBugsForUser, projectService);

        //3. Pass the BugProjectName list to the pagination utility method, which will create the page and add it
        //to the Model.
        paginationUtility.paginateBugsForMyBugs(theModel, bugService, bugProjectNames, page, size);

        return "mybugspage";
    }

    @GetMapping("/inactivebugs")
    public String showInactiveBugs(Model theModel, @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size){

        //0. Util object that returns the data from the user currently logged in.
        UserUtility userUtility = new UserUtility();

        //1. Get list of inactive bugs for user.
        List<Bug> inactiveBugsForUser = bugService.getListOfInactiveBugsForUser(userUtility.getMyUserName());

        //2. Create an arrayList of <BugProjectName>, which is an aux struct with 2 fields, a bug and its corresponding
        //project name in String format.
        List<BugProjectName> inactiveBugProjectNames = listManipulationUtility.makeBugProjectNameList(inactiveBugsForUser, projectService);

        //3. Pass the BugProjectName list to the pagination utility method, which will create the page and add it
        //to the Model.
        paginationUtility.paginateBugsForMyBugs(theModel, bugService, inactiveBugProjectNames, page, size);

        return "inactiveBugsPage";
    }

    @GetMapping("/showBugDetail")
    public String showBugDetail(@RequestParam("bugId") int theId, Model theModel, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

        //1. Get bug that was clicked and add it to the Model, so that we can show all its details.
        Bug bugClicked = bugService.getBugByBugId(theId);
        theModel.addAttribute("theBug", bugClicked);

        //2. Get list of valid developers and add it to the model(since from the HTML view the user can change it).
        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        //3. Get list of active projects and add it to the model(since from th eHTML view the user can change it).
        List<Project> activeProjects = projectService.getActiveProjects();
        theModel.addAttribute("projectList", activeProjects);

        //4. Get bug update history, paginate it and add it to the Model.
        paginationUtility.paginateBugUpdateHistory(theModel, bugService, theId, page, size);

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


        //1. Validate input from user
        if(theBindingResult.hasErrors() || !(validationUtility.validateUpdatedBug(updatedBug, projectService, bugService)) ){
            return "error-page";
        }

        ///2. Copy the original status of the bug into an auxiliar Bug for comparison purposes.
        Bug originalBug = new Bug(bugOriginalTitle, bugOriginalDescription, bugOriginalProjectId,
                            bugOriginalSeverity, bugOriginalPriority, bugOriginalStatus,
                            bugOriginalResponsibleDev, bugOriginalDueDate, bugOriginalStepsToReproduce);

        //3. Compare updated bug with original bug state from previous step, create transaction (update history) if
        // there's difference.
        bugService.compareBugsAndCreateTransaction(updatedBug, originalBug);

        //4. If we are updating a bug but not marking it as 'fixed' we set the bugDateFixed field to null.
        if(updatedBug.getBugDateFixed().equals("")){ updatedBug.setBugDateFixed(null); }

        //5. Update the bug in the DB.
        bugService.save(updatedBug);


        return "redirect:/mybugs/main";
    }


    @GetMapping("/newbug")
    public String newBug(Model theModel){

        //1. Get list of active developers and add it to the Model.
        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        //2. Get list of active projects and add it to the Model.
        List<Project> activeProjects = projectService.getActiveProjects();
        theModel.addAttribute("projectList", activeProjects);

        //3. Initialize a new Bug and add it to the Model.
        Bug bug = new Bug();
        theModel.addAttribute("theBug", bug);

        return "newBugPage";
    }

    @PostMapping("/createBug")
    public String createBug(@Valid @ModelAttribute("theBug") Bug theBug, BindingResult theBindingResult){

        //0. Util object that returns the data from the user currently logged in.
        UserUtility userUtility = new UserUtility();

        //1. Set fields that are not user selectable to standard values for a new bug.
        bugService.setNewBugStandardParameters(theBug, userUtility);

        //2. Validate value inputs provided by user for the bug fields.
        if(theBindingResult.hasErrors() || !(validationUtility.validateNewBug(theBug, projectService))){
            return "error-page";
        }

        //3. Create a new bug transaction (update history) and add it to the bug.
        bugTransactionUtility.createTransaction(theBug, userUtility);

        //4. Save the Bug in the DB.
        bugService.save(theBug);


        return "redirect:/mybugs/main";
    }

    @GetMapping("/deletebug")
    public String deleteBug(@RequestParam("bugId") int bugId){

        //1. Delete the bug.
        bugService.delete(bugId);

        return "redirect:/mybugs/main";
    }

}

package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import cc.jbdev.smaug.utility.PaginationUtility;
import cc.jbdev.smaug.utility.UserUtility;
import cc.jbdev.smaug.validation.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/myprojects")
public class MyProjectsController {

    @Autowired
    BugService bugService;

    @Autowired
    ProjectService projectService;

    @Autowired
    PaginationUtility paginationUtility;

    @GetMapping("/main")
    public String showMyProjects(Model theModel, @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {

        //0. Util object that returns the data from the user currently logged in.
        UserUtility userUtility = new UserUtility();

        //1. Get list of active projects for the user
        List<Project> userProjectList = projectService.getActiveProjectsListForUser(userUtility.getMyUserName());

        //2. Pass the project list to the pagination utility method, which will create the page and add it
        //to the Model.
        paginationUtility.paginateProjectsForMyProjects(theModel, projectService, userProjectList, page, size);
        

        return "myprojectspage";
    }


    @GetMapping("/showProjectDetail")
    public String showProjectDetail(@RequestParam("projectId") int theId, Model theModel, @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {

        Project elProjectClickeadoEs = projectService.getProjectById(theId);

        theModel.addAttribute("theProject", elProjectClickeadoEs);


        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);


        int currentPage = 1;
        int pageSize = 15;

        ///PAGINATION UPDATE
        Page<Developer> developerPage = projectService.paginate(PageRequest.of(currentPage - 1, pageSize), projectService.getProjectById(theId).getDevelopers());
        //

        theModel.addAttribute("developerPage", developerPage);

        int totalPages = developerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }

        return "showProjectDetailPage";
    }


    @PostMapping("/updateProject")
    public String updateProject(@ModelAttribute("theProject") Project theProject){

        ValidationUtility validationUtility = new ValidationUtility();

        //1. Validate
        if(!(validationUtility.validateUpdatedProject(theProject, projectService))){
            return "error-page";
        }
        ////

        projectService.save(theProject);

        return "redirect:/myprojects/main";
    }

    @GetMapping("/newproject")
    public String addNewProject(Model theModel){

        Project theProject = new Project();
        theModel.addAttribute("theProject", theProject);

        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);


        return "newProjectPage";
    }


    @PostMapping("/createproject")
    public String createProject(@ModelAttribute("theProject") Project theProject){

        ValidationUtility validationUtility = new ValidationUtility();

        //1. set standard params for new project
        theProject.setProjectId(0);
        theProject.setIsActive(1);
        ////

        //2. Validate new project
        if(!(validationUtility.validateNewProject(theProject, projectService))){
            return "error-page";
        }
        ////

        //3. Save new project
        projectService.save(theProject);
        ////

        return "redirect:/myprojects/main";
    }

    @GetMapping("/deleteproject")
    public String deleteProject(@RequestParam("projectId") int projectId){

        projectService.delete(projectId);

        return "redirect:/myprojects/main";
    }

    @GetMapping("/adddeveloper")
    public String addDeveloper(@RequestParam("projectId") int projectId, Model theModel){

        Developer newDeveloper = new Developer();

        theModel.addAttribute("newDeveloper", newDeveloper);
        theModel.addAttribute("projectId", projectId);

        String projectName = projectService.getProjectById(projectId).getProjectName();
        theModel.addAttribute("projectName", projectName);

        List<String> activeDevelopers = projectService.getListOfActiveDevelopers();
        theModel.addAttribute("devList", activeDevelopers);

        return "addDeveloperPage";
    }


    @PostMapping("/adddeveloperconfirm")
    public String addDeveloperConfirm(@RequestParam("projectId") int projectId, @ModelAttribute("newDeveloper") Developer theDeveloper){

        ValidationUtility validationUtility = new ValidationUtility();

        //1. Validate developer
        if(!(validationUtility.validateDeveloper(projectService.getListOfActiveDevelopers(), theDeveloper.getUsername()))){
            return "error-page";
        }
        ////

        //2. Get the project to which the developer will be added
        Project theProject = projectService.getProjectById(projectId);
        ////

        //3. Add developer to project
        projectService.addDeveloperToProject(theProject, theDeveloper);
        ////

        return "redirect:/myprojects/main";
    }


    @GetMapping("/removedeveloper")
    public String removeDeveloper(@RequestParam("projectId") int projectId, Model theModel){

        Project theProject = projectService.getProjectById(projectId);
        List<Developer> projectDevList = theProject.getDevelopers();

        theModel.addAttribute("projectDevList", projectDevList);
        theModel.addAttribute("projectId", projectId);

        return "removeDeveloperPage";
    }


    @PostMapping("/removedeveloperconfirm")
    public String removedeveloperconfirm(@RequestParam("devSelected") String devSelected, @RequestParam("projectId") int projectId){

        ValidationUtility validationUtility = new ValidationUtility();

        //0. Get list of active developers in project
        List<Developer> projectDevList = new ArrayList<>();
        projectDevList = projectService.getProjectById(projectId).getDevelopers();
        ////

        //1. Validate developer to be removed is a valid developer and is assigned to the project
        if(!(validationUtility.validateDeveloperAssgignedToProject(projectDevList, devSelected))){
            return "error-page";
        }
        ////

        //2. Get the project where the developer will be removed from
        Project theProject = projectService.getProjectById(projectId);
        ////

        //3. Get the developer that will be removed from the Project
        Developer theDeveloper = theProject.getDeveloper(devSelected);
        ////

        //4. Remove the developer from the Project
        projectService.removeDeveloperFromProject(theProject, theDeveloper);
        ////

        return "redirect:/myprojects/main";
    }

}

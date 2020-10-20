package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Developer;
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

    @GetMapping("/main")
    public String showMyProjects(Model theModel, @RequestParam("page") Optional<Integer> page,
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

        Page<Project> projectPage = projectService.findPaginatedUserActiveProjects(PageRequest.of(currentPage - 1, pageSize), myUserName);

        theModel.addAttribute("projectPage", projectPage);

        int totalPages = projectPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }



        return "myprojectspage";
    }


    @GetMapping("/newproject")
    public String addNewProject(Model theModel){

        Project theProject = new Project();

        theModel.addAttribute("theProject", theProject);

        return "newProjectPage";
    }

    @PostMapping("/createproject")
    public String createProject(@ModelAttribute("theProject") Project theProject){

        theProject.setProjectId(0);
        projectService.save(theProject);


        return "dashboardpage";
    }

    @GetMapping("/showProjectDetail")
    public String showBugDetail(@RequestParam("projectId") int theId, Model theModel) {

        Project elProjectClickeadoEs = projectService.getProjectById(theId);

        theModel.addAttribute("theProject", elProjectClickeadoEs);

        List<Developer> projectDevList = elProjectClickeadoEs.getDevelopers();

        theModel.addAttribute("devList", projectDevList);





        int currentPage = 1;
        int pageSize = 15;

        Page<Developer> developerPage = projectService.findPaginatedProjectActiveDevelopers(PageRequest.of(currentPage - 1, pageSize), theId);

        theModel.addAttribute("developerPage", developerPage);

        int totalPages = developerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
        //

        return "showProjectDetailPage";

    }

    @PostMapping("/updateProject")
    public String updateBug(@ModelAttribute("theProject") Project theProject){


        projectService.save(theProject);

        return "dashboardpage";

    }

    @GetMapping("/deleteproject")
    public String deleteBug(@RequestParam("projectId") int projectId){

        projectService.delete(projectId);

        return "dashboardpage";
    }

    @GetMapping("/adddeveloper")
    public String addDeveloper(@RequestParam("projectId") int projectId, Model theModel){

        Developer newDeveloper = new Developer();

        theModel.addAttribute("newDeveloper", newDeveloper);

        return "addDeveloperPage";
        //addDeveloperPage HTML PAGE NEEDS TO BE CREATED AND IMPLEMENTED
    }

    @GetMapping("/removedeveloper")
    public String removeDeveloper(@RequestParam("projectId") int projectId){

        return "removeDeveloperPage";
        //removeDeveloperPage HTML PAGE NEEDS TO BE CREATED AND IMPLEMENTED
    }

}

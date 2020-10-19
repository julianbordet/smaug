package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
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


}

package cc.jbdev.smaug.controller;

import cc.jbdev.smaug.entity.Bug;
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

        return "mybugspage";
    }


    @GetMapping("/showBugDetail")
    public String showBugDetail(@RequestParam("bugId") int theId, Model theModel) {

        Bug elBugClickeadoEs = bugService.getBugByBugId(theId);

        theModel.addAttribute("theBug", elBugClickeadoEs);

        return "showBugDetailPage";

    }



    @PostMapping("/updateBug")
    public String updateBug(@ModelAttribute("theBug") Bug theBug){


        bugService.save(theBug);

        return "dashboardpage";

    }

    @GetMapping("/newbug")
    public String newBug(Model theModel){

        Bug bug = new Bug();

        theModel.addAttribute("theBug", bug);

        return "newBugPage";
    }

    @PostMapping("/createBug")
    public String createBug(@ModelAttribute("theBug") Bug theBug){

        theBug.setBugId(0);
        bugService.save(theBug);

        return "dashboardpage";
    }

    @GetMapping("/deletebug")
    public String deleteBug(@RequestParam("bugId") int bugId){

        bugService.delete(bugId);

        return "dashboardpage";
    }



}

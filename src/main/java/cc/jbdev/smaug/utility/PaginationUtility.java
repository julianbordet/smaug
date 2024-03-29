package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.auxStructs.BugProjectName;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.service.BugService;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PaginationUtility {

    public void paginateBugsForMyBugs(Model theModel, BugService bugService, List<BugProjectName> activeBugListForUser, Optional<Integer> page, Optional<Integer> size){
        //Adds a paginated list of active bugs for currently logged in user to the Model
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);

        Page<Bug> bugPage = bugService.paginate(PageRequest.of(currentPage - 1, pageSize), activeBugListForUser);
        theModel.addAttribute("bugPage", bugPage);

        int totalPages = bugPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }

    }

    public void paginateBugUpdateHistory(Model theModel, BugService bugService, int theId, Optional<Integer> page, Optional<Integer> size){

        //pagination for bug transaction
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        //Has own pagination method since the results need to be in reverse order, so that newer transactions
        //appear first in the list
        Page<BugTransaction> bugTransactions = bugService.findPaginatedBugTransactions(PageRequest.of(currentPage - 1, pageSize), theId);

        theModel.addAttribute("bugTransactions", bugTransactions);

        int totalPages = bugTransactions.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
    }

    public void paginateProjectsForMyProjects(Model theModel, ProjectService projectService, List<Project> projectList, Optional<Integer> page, Optional<Integer> size){


        int currentPage = page.orElse(1);
        int pageSize = size.orElse(15);

        Page<Project> projectPage = projectService.paginate(PageRequest.of(currentPage - 1, pageSize), projectList);

        theModel.addAttribute("projectPage", projectPage);

        int totalPages = projectPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }

    }

    public void paginateDevelopersforMyProject(Model theModel, ProjectService projectService, int theId){

        int currentPage = 1;
        int pageSize = 15;

        Page<Developer> developerPage = projectService.paginate(PageRequest.of(currentPage - 1, pageSize), projectService.getProjectById(theId).getDevelopers());

        theModel.addAttribute("developerPage", developerPage);

        int totalPages = developerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            theModel.addAttribute("pageNumbers", pageNumbers);
        }
    }

}

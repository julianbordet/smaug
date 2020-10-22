package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface BugService {

    //get bug by bugId
    Bug getBugByBugId(int bugId);

    //gets list of all bugs
    List<Bug> getBugList();

    //gets list of all bugs (active and inactive) for a certain user
    List<Bug> getBugListForUser(String username);

    //gets list of only active bugs for a specific user
    List<Bug> getActiveBugListForUser(String username);

    //gets active bug list for a specific user
    List<Bug> getProjectActiveBugsByUser(Project project, String username);

    //paginates active bugs for a specific user
    Page<Bug> findPaginatedUserActiveBugs(Pageable pageable, String username);

    //save updates/creates new bug
    void save(Bug theBug);

    //deletes bug
    void delete(int bugId);

    //returns an Integer List representing the total amount of active (not closed as fixed or archived) bugs for a
    // specific user. The element 0 in the list represents the count for due bugs, the element 1 in the list represents
    // the counter for bugs not due
    List<Integer> getActiveDueAndNotDueBugsCounterForUser(String username);

    //returns a list of 4 elements. Element 0 is a counter for bugs with Critical Priority, element 1 = major priority,
    //element 2 = minor priority, element 3 = low priority
    List<Integer> getActiveBugsCounterBySeverity(String username);

    //Returns a list of 3 elements. Element 0 is a counter for bugs with High priority, element 1 = medium priority
    //element 2 = low priority
    List<Integer> getActiveBugsCounterByPriority(String username);

    //Returns a hashmap with a key representing the project name, and the value representing the active bug amount
    //for such project.
    HashMap<String, Integer> getMapProjectNameBugCounter(List<Project> projectList, String username);
}

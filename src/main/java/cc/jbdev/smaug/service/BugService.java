package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}

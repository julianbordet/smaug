package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BugService {

    //get bug by bugId
    public Bug getBugByBugId(int bugId);

    //gets list of all bugs
    public List<Bug> getBugList();

    //gets list of all bugs (active and inactive) for a certain user
    public List<Bug> getBugListForUser(String username);

    //gets list of only active bugs for a certain user
    public List<Bug> getActiveBugListForUser(String username);

    List<Bug> getProjectActiveBugsByUser(Project project, String username);

    //add method to paginate results
    Page<Bug> findPaginatedUserActiveBugs(Pageable pageable, String username);

    //save updates to bug
    void save(Bug theBug);

}

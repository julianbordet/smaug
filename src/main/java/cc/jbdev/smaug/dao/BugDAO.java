package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface BugDAO {

    //get bug by ID
    Bug getBugByBugId(int bugId);

    //Get list of all bugs
    List<Bug> getBugList();

    //gets list of all bugs (active and inactive) for a certain user
    List<Bug> getBugListForUser(String username);

    //gets active bug list for a specific user
    List<Bug> getActiveBugListForUser(String username);

    //paginates active bugs for a specific user
    List<Bug> getProjectActiveBugsByUser(Project project, String username);

    //save updates/creates new bug
    void save(Bug theBug);

    //deletes bug
    void delete(int bugId);
}

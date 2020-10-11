package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface BugService {

    //gets list of all bugs
    public List<Bug> getBugList();

    //gets list of all bugs (active and inactive) for a certain user
    public List<Bug> getBugListForUser(String username);

    //gets list of only active bugs for a certain user
    public List<Bug> getActiveBugListForUser(String username);

    List<Bug> getProjectActiveBugsByUser(Project project, String username);
}

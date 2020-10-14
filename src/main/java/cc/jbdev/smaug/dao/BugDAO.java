package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface BugDAO {

    Bug getBugByBugId(int bugId);

    List<Bug> getBugList();

    List<Bug> getBugListForUser(String username);

    List<Bug> getActiveBugListForUser(String username);

    List<Bug> getProjectActiveBugsByUser(Project project, String username);
}

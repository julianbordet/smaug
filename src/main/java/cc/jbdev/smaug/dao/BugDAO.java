package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;

import java.util.List;

public interface BugDAO {

    List<Bug> getBugList();

    List<Bug> getBugListForUser(String username);

    List<Bug> getActiveBugListForUser(String username);
}
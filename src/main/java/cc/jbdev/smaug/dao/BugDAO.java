package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;

import java.util.List;

public interface BugDAO {

    public List<Bug> getBugList();

    List<Bug> getBugListForUser(String username);
}

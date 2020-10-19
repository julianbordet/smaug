package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface ProjectDAO {

    public List<Project> getProjectsList();

    public List<Project> getActiveProjectsListForUser(String username);

    public void save(Project theProject);

}


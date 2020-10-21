package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface ProjectDAO {

    public List<Project> getProjectsList();

    public List<Project> getActiveProjectsListForUser(String username);

    public void save(Project theProject);

    Project getProjectById(int theId);

    void delete(int projectId);

    void addDeveloperToProject(Project theProject, Developer theDeveloper);

}


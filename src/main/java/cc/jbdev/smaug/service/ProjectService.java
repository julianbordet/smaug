package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.utility.UserUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public interface ProjectService {

    //returns a list of ALL projects
    List<Project> getProjectsList();

    //returns a list of ALL ACTIVE projects
    List<Project> getActiveProjects();

    //returns a list of ACTIVE projects by username
    List<Project> getActiveProjectsListForUser(String username);

    //gets Project by projectId
    Project getProjectById(int theId);


    Page paginate(Pageable pageable, List theParameterList);


    //save updates/creates new project
    void save(Project theProject);

    //deletes project by projectId
    void delete(int projectId);

    //adds a Developer to a Project
    void addDeveloperToProject(Project theProject, Developer theDeveloper);

    //removes a Developer from a Project
    void removeDeveloperFromProject(Project theProject, Developer theDeveloper);

    //gets a String list containing all active user ids
    List<String> getListOfActiveDevelopers();

    //Sets the ProjectId to 0, isActive to 1.
    void setNewProjectStandardParameters(Project theProject);
}

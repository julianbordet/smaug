package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public interface ProjectService {

    //returns a list of ALL projects
    List<Project> getProjectsList();

    //returns a list of ACTIVE projects by username
    List<Project> getActiveProjectsListForUser(String username);

    //gets Project by projectId
    Project getProjectById(int theId);

    //paginates Project list by username
    Page<Project> findPaginatedUserActiveProjects(Pageable pageable, String username);

    //paginates a list of ACTIVE developers for a spcific project
    Page<Developer> findPaginatedProjectActiveDevelopers(Pageable pageable, int projectId);

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
}

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

    List<Project> getProjectsList();

    List<Project> getActiveProjectsListForUser(String username);

    Project getProjectById(int theId);

    //add method to paginate results for User --> Active Projects
    Page<Project> findPaginatedUserActiveProjects(Pageable pageable, String username);

    //add method to paginate results for Project --> Active developers
    Page<Developer> findPaginatedProjectActiveDevelopers(Pageable pageable, int projectId);

    //save updates to bug
    void save(Project theProject);

    //remove bug from project
    void delete(int projectId);

    void addDeveloperToProject(Project theProject, Developer theDeveloper);


    void removeDeveloperFromProject(Project theProject, Developer theDeveloper);

}

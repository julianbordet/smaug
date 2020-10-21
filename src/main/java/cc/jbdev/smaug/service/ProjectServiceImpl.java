package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.ProjectDAO;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Developer;
import cc.jbdev.smaug.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectDAO projectDAO;

    public List<Project> getProjectsList(){
        return projectDAO.getProjectsList();
    }

    @Override
    public List<Project> getActiveProjectsListForUser(String username) {
        return projectDAO.getActiveProjectsListForUser(username);
    }

    @Override
    public Page<Project> findPaginatedUserActiveProjects(Pageable pageable, String username) {

        List<Project> userProjectList = projectDAO.getActiveProjectsListForUser(username);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Project> list;

        if (userProjectList.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, userProjectList.size());
            list = userProjectList.subList(startItem, toIndex);
        }

        Page<Project> projectPage = new PageImpl<Project>(list, PageRequest.of(currentPage, pageSize), userProjectList.size());

        return projectPage;
    }

    @Override
    public Page<Developer> findPaginatedProjectActiveDevelopers(Pageable pageable, int projectId) {


        List<Developer> developerProjectList = projectDAO.getProjectById(projectId).getDevelopers();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Developer> list;

        if (developerProjectList.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, developerProjectList.size());
            list = developerProjectList.subList(startItem, toIndex);
        }

        Page<Developer> developerPage = new PageImpl<Developer>(list, PageRequest.of(currentPage, pageSize), developerProjectList.size());

        return developerPage;




    }

    @Override
    public void save(Project theProject) {
        projectDAO.save(theProject);
    }

    @Override
    public Project getProjectById(int theId) {
         return projectDAO.getProjectById(theId);
    }

    @Override
    public void delete(int projectId) {
        projectDAO.delete(projectId);
    }

    @Override
    public void addDeveloperToProject(Project theProject, Developer theDeveloper) {
        projectDAO.addDeveloperToProject(theProject, theDeveloper);
    }

    @Override
    public void removeDeveloperFromProject(Project theProject, Developer theDeveloper) {
        projectDAO.removeDeveloperFromProject(theProject, theDeveloper);
    }
}

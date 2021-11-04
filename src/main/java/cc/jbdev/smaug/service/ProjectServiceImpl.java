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
    public Page paginate(Pageable pageable, List theParameterList) {

            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;
            List list;

            if (theParameterList.size() < startItem){
                list = Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, theParameterList.size());
                list = theParameterList.subList(startItem, toIndex);
            }

            Page thePage = new PageImpl(list, PageRequest.of(currentPage, pageSize), theParameterList.size());


            return thePage;
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

    @Override
    public List<String> getListOfActiveDevelopers() {
        return projectDAO.getListOfActiveDevelopers();
    }

    @Override
    public List<Project> getActiveProjects() {
        return projectDAO.getActiveProjects();
    }

    public void setNewProjectStandardParameters(Project theProject){

        theProject.setProjectId(0);
        theProject.setIsActive(1);

    }

}

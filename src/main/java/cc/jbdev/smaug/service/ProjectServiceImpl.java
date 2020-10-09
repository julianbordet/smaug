package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.ProjectDAO;
import cc.jbdev.smaug.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectDAO projectDAO;

    public List<Project> getProjectsList(){
        return projectDAO.getProjectsList();
    }

}

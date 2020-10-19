package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
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

    //add method to paginate results
    Page<Project> findPaginatedUserActiveProjects(Pageable pageable, String username);


    //save updates to bug
    void save(Project theProject);


}

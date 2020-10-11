package cc.jbdev.smaug.service;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getProjectsList();

    List<Project> getActiveProjectsListForUser(String username);



}

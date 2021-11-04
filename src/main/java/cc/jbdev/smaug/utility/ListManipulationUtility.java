package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.auxStructs.BugProjectName;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListManipulationUtility {

    public ListManipulationUtility() {
    }

    public List<BugProjectName> makeBugProjectNameList(List<Bug> activeBugsForUser, ProjectService projectService){

        List<BugProjectName> bugProjectNameList = new ArrayList<>();

        for(Bug bug : activeBugsForUser){
            String projectName = projectService.getProjectById(bug.getProjectId()).getProjectName();
            bugProjectNameList.add(new BugProjectName(bug, projectName));
        }

        return bugProjectNameList;
    }
}

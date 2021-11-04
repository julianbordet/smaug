package cc.jbdev.smaug.auxStructs;

import cc.jbdev.smaug.entity.Bug;

public class BugProjectName {

    private Bug bug;
    private String projectName;

    public BugProjectName() {
    }

    public BugProjectName(Bug bug, String projectName) {
        this.bug = bug;
        this.projectName = projectName;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

package cc.jbdev.smaug.entity;

public class ProjectBugCounter {

    private String projectName;

    private int activeBugCount;

    public ProjectBugCounter(){

    }

    public ProjectBugCounter(String projectName, int activeBugCount) {
        this.projectName = projectName;
        this.activeBugCount = activeBugCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getActiveBugCount() {
        return activeBugCount;
    }

    public void setActiveBugCount(int activeBugCount) {
        this.activeBugCount = activeBugCount;
    }
}

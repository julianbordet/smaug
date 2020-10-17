package cc.jbdev.smaug.entity;

import javax.persistence.*;
import java.util.Date;


public class BugT {


    private int bugId;


    private int projectId;


    private String bugTitle;


    private String bugDescription;


    private String stepsToReproduce;


    private String bugSeverity;


    private String dateCreated;


    private String bugStatus;


    private String bugCreatedBy;

    private String bugResponsibleDev;

    private String bugDueDate;

    private String bugPriority;

    public BugT(){

    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getBugTitle() {
        return bugTitle;
    }

    public void setBugTitle(String bugTitle) {
        this.bugTitle = bugTitle;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
    }

    public String getBugSeverity() {
        return bugSeverity;
    }

    public void setBugSeverity(String bugSeverity) {
        this.bugSeverity = bugSeverity;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    public String getBugCreatedBy() {
        return bugCreatedBy;
    }

    public void setBugCreatedBy(String bugCreatedBy) {
        this.bugCreatedBy = bugCreatedBy;
    }

    public String getBugResponsibleDev() {
        return bugResponsibleDev;
    }

    public void setBugResponsibleDev(String bugResponsibleDev) {
        this.bugResponsibleDev = bugResponsibleDev;
    }

    public String getBugDueDate() {
        return bugDueDate;
    }

    public void setBugDueDate(String bugDueDate) {
        this.bugDueDate = bugDueDate;
    }

    public String getBugPriority() {
        return bugPriority;
    }

    public void setBugPriority(String bugPriority) {
        this.bugPriority = bugPriority;
    }

    @Override
    public String toString() {
        return "Bug{" +
                "bugId=" + bugId +
                ", projectId=" + projectId +
                ", bugTitle='" + bugTitle + '\'' +
                ", bugDescription='" + bugDescription + '\'' +
                ", stepsToReproduce='" + stepsToReproduce + '\'' +
                ", bugSeverity='" + bugSeverity + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", bugStatus='" + bugStatus + '\'' +
                ", bugCreatedBy='" + bugCreatedBy + '\'' +
                ", bugResponsibleDev='" + bugResponsibleDev + '\'' +
                ", bugDueDate='" + bugDueDate + '\'' +
                '}';
    }
}

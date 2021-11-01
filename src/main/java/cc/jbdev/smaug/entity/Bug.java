package cc.jbdev.smaug.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bugs")
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bug_id")
    private int bugId;

    @Column(name="project_id")
    private int projectId;

    @Column(name="title")
    private String bugTitle;

    @Column(name="description")
    private String bugDescription;

    @Column(name="steps_to_reproduce")
    private String stepsToReproduce;

    @Column(name="severity")
    private String bugSeverity;

    @Column(name="date_created")
    private String dateCreated;

    //status = 1 = bug is closed/fixed
    //status = 0 = bug is still open
    @Column(name="is_fixed")
    private int bugStatus;

    @Column(name="created_by")
    private String bugCreatedBy;

    @Column(name="responsible_dev")
    private String bugResponsibleDev;

    @Column(name="due_date")
    private String bugDueDate;

    @Column(name="date_fixed")
    private String bugDateFixed;

    @Column(name="priority")
    private String bugPriority;


    @OneToMany(mappedBy="bugId",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
                //  cascade = {CascadeType.ALL})
    private List<BugTransaction> bugTransactions;



    public Bug(){

    }

    public Bug(String bugOriginalTitle, String bugOriginalDescription, String bugOriginalProjectId,
               String bugOriginalSeverity, String bugOriginalPriority, String bugOriginalStatus,
               String bugOriginalResponsibleDev, String bugOriginalDueDate, String bugOriginalStepsToReproduce){

        this.bugTitle = bugOriginalTitle;
        this.bugDescription = bugOriginalDescription;
        this.projectId = Integer.parseInt(bugOriginalProjectId);
        this.bugSeverity = bugOriginalSeverity;
        this.bugPriority = bugOriginalPriority;
        this.bugStatus = Integer.parseInt(bugOriginalStatus);
        this.bugResponsibleDev = bugOriginalResponsibleDev;
        this.bugDueDate = bugOriginalDueDate;
        this.stepsToReproduce = bugOriginalStepsToReproduce;
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


    public int getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(int bugStatus) {
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

    public List<BugTransaction> getBugTransactions() {
        return bugTransactions;
    }

    public void addBugTransactions(BugTransaction newBugTransaction) {

        if(this.bugTransactions == null){
            this.bugTransactions = new ArrayList<>();
        }

        bugTransactions.add(newBugTransaction);

        newBugTransaction.setBugId(this);

    }

    public String getBugDateFixed() {
        return bugDateFixed;
    }

    public void setBugDateFixed(String bugDateFixed) {
        this.bugDateFixed = bugDateFixed;
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

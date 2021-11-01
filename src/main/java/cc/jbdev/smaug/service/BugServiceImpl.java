package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.BugDAO;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.utility.BugTransactionComparator;
import cc.jbdev.smaug.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private BugDAO bugDAO;

    @Autowired
    private ProjectService projectService;


    @Override
    public List<Bug> getBugList() {
        return bugDAO.getBugList();
    }

    @Override
    public List<Bug> getBugListForUser(String username) {
        return bugDAO.getBugListForUser(username);
    }

    @Override
    public List<Bug> getActiveBugListForUser(String username) {
        return bugDAO.getActiveBugListForUser(username);
    }

    @Override
    public List<Bug> getListOfInactiveBugsForUser(String username) {
        return bugDAO.getListOfInactiveBugsForUser(username);
    }

    @Override
    public List<Bug> getProjectActiveBugsByUser(Project project, String username) {
        return bugDAO.getProjectActiveBugsByUser(project, username);
    }



    public Page paginate(Pageable pageable, List theParameterList){

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
    public Page<BugTransaction> findPaginatedBugTransactions(Pageable pageable, int bugId) {

        List<BugTransaction> bugTransactions = getBugTransactionsByBugId(bugId);

        BugTransactionComparator transactionComparator = new BugTransactionComparator();

        Collections.sort(bugTransactions, Collections.reverseOrder(transactionComparator));



        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BugTransaction> list;

        if (bugTransactions.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, bugTransactions.size());
            list = bugTransactions.subList(startItem, toIndex);
        }

        Page<BugTransaction> bugTransactionPage = new PageImpl<BugTransaction>(list, PageRequest.of(currentPage, pageSize), bugTransactions.size());

        return bugTransactionPage;
    }


    @Override
    public Bug getBugByBugId(int bugId) {
        return bugDAO.getBugByBugId(bugId);
    }

    @Override
    public void save(Bug theBug) {
        bugDAO.save(theBug);
    }

    @Override
    public void delete(int bugId) {
        bugDAO.delete(bugId);
    }

    @Override
    public List<Integer> getActiveDueAndNotDueBugsCounterForUser(String username) {

        List<Bug> userActiveBugList = getActiveBugListForUser(username);

        Integer bugsDue = 0;
        Integer bugsNotDue = 0;

        for(Bug bug : userActiveBugList){

            String bugDueDateInString = bug.getBugDueDate();

            Date dueDateInDate = new Date();

            try {
                dueDateInDate = new SimpleDateFormat("yyyy-MM-dd").parse(bugDueDateInString);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            Date today = new Date();

            if (dueDateInDate.after(today)){
                bugsNotDue++;
            } else {
                bugsDue++;
            }
        }

        List<Integer> bugsDueAndNotDueCounter = new ArrayList<>();
        bugsDueAndNotDueCounter.add(bugsDue);
        bugsDueAndNotDueCounter.add(bugsNotDue);

        return bugsDueAndNotDueCounter;
    }

    @Override
    public List<Integer> getActiveBugsCounterBySeverity(String username) {

        Integer criticalBugCount = 0;
        Integer majorBugCount = 0;
        Integer minorBugCount = 0;
        Integer lowBugCount = 0;

        List<Bug> userActiveBugList = getActiveBugListForUser(username);

        for(Bug bug : userActiveBugList) {
            if (bug.getBugSeverity().equals("CRITICAL")){
                criticalBugCount++;
            }
            if (bug.getBugSeverity().equals("MAJOR")){
                majorBugCount++;
            }
            if (bug.getBugSeverity().equals("MINOR")){
                minorBugCount++;
            }
            if (bug.getBugSeverity().equals("LOW")){
                lowBugCount++;
            }
        }

        List<Integer> listOfBugsBySeverity = new ArrayList<>();
        listOfBugsBySeverity.add(criticalBugCount);
        listOfBugsBySeverity.add(majorBugCount);
        listOfBugsBySeverity.add(minorBugCount);
        listOfBugsBySeverity.add(lowBugCount);

        return listOfBugsBySeverity;
    }

    @Override
    public List<Integer> getActiveBugsCounterByPriority(String username) {

        Integer userHighPriorityBugCount = 0;
        Integer userMediumPriorityBugCount = 0;
        Integer userLowPriorityBugCount = 0;

        List<Bug> userActiveBugList = getActiveBugListForUser(username);

        for(Bug bug : userActiveBugList) {
            if (bug.getBugPriority().equals("HIGH")){
                userHighPriorityBugCount++;
            }
            if (bug.getBugPriority().equals("MEDIUM")){
                userMediumPriorityBugCount++;
            }
            if (bug.getBugPriority().equals("LOW")){
                userLowPriorityBugCount++;
            }
        }

        List<Integer> listOfBugsByPriority = new ArrayList<>();
        listOfBugsByPriority.add(userHighPriorityBugCount);
        listOfBugsByPriority.add(userMediumPriorityBugCount);
        listOfBugsByPriority.add(userLowPriorityBugCount);

        return listOfBugsByPriority;
    }

    @Override
    public HashMap<String, Integer> getMapProjectNameBugCounter(List<Project> projectList, String myUserName) {

        HashMap<String, Integer> mapProjectBugCounter = new HashMap<>();

        for (Project project : projectList){

            List<Bug> bugListForProject = getProjectActiveBugsByUser(project, myUserName);

            Integer bugCounterForProject = bugListForProject.size();
            String projectName = project.getProjectName();

            mapProjectBugCounter.put(projectName, bugCounterForProject);
        }

        return mapProjectBugCounter;
    }

    @Override
    public List<BugTransaction> getBugTransactionsByBugId(int bugId) {
        return bugDAO.getBugTransactionsByBugId(bugId);
    }

    @Override
    public void compareBugsAndCreateTransaction(Bug updatedBug, Bug originalBug) {

        UserUtility userUtility = new UserUtility();

        String changesMade = "";
        String changesMadeDetail = "";


        if ( !(updatedBug.getBugTitle().equals(originalBug.getBugTitle())) ){
            changesMade += "Title updated. ";
            changesMadeDetail += "Title updated from: " + originalBug.getBugTitle() + " to: " + updatedBug.getBugTitle();
            changesMadeDetail += "\n";
        }

        if ( !(updatedBug.getBugDescription().equals(originalBug.getBugDescription())) ){
            changesMade += "Description updated. ";
            changesMadeDetail += "Description updated: " + updatedBug.getBugDescription();
            changesMadeDetail += "\n";
        }


        if ( !(updatedBug.getProjectId() ==  originalBug.getProjectId() ) ){
            changesMade += "Assigned project changed. ";
            changesMadeDetail += "Project reassigned from: " +
                    projectService.getProjectById(originalBug.getProjectId()).getProjectName() +
                    " to: " + projectService.getProjectById(updatedBug.getProjectId()).getProjectName() +
                    "\n";
        }

        if ( !(updatedBug.getBugSeverity().equals(originalBug.getBugSeverity())) ){
            changesMade += "Severity updated. ";
            changesMadeDetail += "Bug severity updated from: " + originalBug.getBugSeverity() + " to: " +
                    updatedBug.getBugSeverity() +
                    "\n";
        }

        if ( !(updatedBug.getBugPriority().equals(originalBug.getBugPriority())) ){
            changesMade += "Priority updated. ";
            changesMadeDetail += "Priority updated from: " + originalBug.getBugPriority() + " to: " +
                    updatedBug.getBugPriority() +
                    "\n";
        }

        int updatedBugStatus = updatedBug.getBugStatus();
        int originalBugStatus = originalBug.getBugStatus();

        if ( !(updatedBugStatus ==  originalBugStatus) ){
            changesMade += "Status updated. ";

            if(updatedBugStatus == 1) {
                changesMadeDetail += "Status updated from: In Progress to Fixed\n";
            } else {
                changesMadeDetail += "Status updated from: Fixed to In Progress\n";
            }
        }

        if ( !(updatedBug.getBugResponsibleDev().equals(originalBug.getBugResponsibleDev())) ){
            changesMade += "Responsible dev changed. ";
            changesMadeDetail += "Responsible dev changed from: " + originalBug.getBugResponsibleDev() + " to: " + updatedBug.getBugResponsibleDev() +
                    "\n";
        }

        if ( !(updatedBug.getBugDueDate().equals(originalBug.getBugDueDate())) ){
            changesMade += "Due date updated. ";
            changesMadeDetail += "Due date updated from: " + originalBug.getBugDueDate() + " to: " + updatedBug.getBugDueDate() +
                    "\n";
        }

        if ( !(updatedBug.getStepsToReproduce().equals(originalBug.getStepsToReproduce())) ){
            changesMade += "Steps to reproduce updated. ";
            changesMadeDetail += "Steps to reproduce updated: " + updatedBug.getStepsToReproduce();
        }


        //Create a new date for the bug transaction
        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today);


        //check if changesMade variable is empty
        if ( !(changesMade.isEmpty()) ){

            //If it's not empty create a new transaction and add it to the bug
            BugTransaction newBugTransaction = new BugTransaction();
            newBugTransaction.setDate(todayInString);
            newBugTransaction.setTransaction(changesMade);
            newBugTransaction.setTransactionId(0);
            newBugTransaction.setTransactionCreatedBy(userUtility.getMyUserName());
            newBugTransaction.setTransactionDetail(changesMadeDetail);
            updatedBug.addBugTransactions(newBugTransaction);
        }

    }

    public void setNewBugStandardParameters(Bug theBug, UserUtility userUtility){

        //New Bug standard setup
        theBug.setBugId(0);
        theBug.setBugStatus(0);
        theBug.setBugCreatedBy(userUtility.getMyUserName());

        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd").format(today);
        theBug.setDateCreated(todayInString);
        ////

    }

}

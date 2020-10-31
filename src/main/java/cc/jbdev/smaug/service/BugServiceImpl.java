package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.BugDAO;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import cc.jbdev.smaug.entity.Project;
import cc.jbdev.smaug.utility.BugTransactionComparator;
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

    @Override
    public Page<Bug> findPaginatedUserActiveBugs(Pageable pageable, String username) {

        List<Bug> userBugList = getActiveBugListForUser(username);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Bug> list;

        if (userBugList.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, userBugList.size());
            list = userBugList.subList(startItem, toIndex);
        }

        Page<Bug> bugPage = new PageImpl<Bug>(list, PageRequest.of(currentPage, pageSize), userBugList.size());

        return bugPage;
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
}

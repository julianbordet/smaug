package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.BugDAO;
import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
}

package cc.jbdev.smaug.service;

import cc.jbdev.smaug.dao.BugDAO;
import cc.jbdev.smaug.entity.Bug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

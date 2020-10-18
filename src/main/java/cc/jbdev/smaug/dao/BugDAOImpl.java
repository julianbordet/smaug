package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class BugDAOImpl implements BugDAO {


    private EntityManager entityManager;

    @Autowired
    public BugDAOImpl(EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }

    @Override
    @Transactional
    public List<Bug> getBugList() {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //create the query to get the bug list from mysql
        /////////////////
        //NOTE TO SELF://
        /////////////////
        //Since the query below is done in HQL the "from Bug" should refer to the @Entity class name
        //not to the table name, which in this case is "bugs".
        Query<Bug> theQuery = currentSession.createQuery("from Bug", Bug.class);

        //execute query and get result list
        List<Bug> bugList = theQuery.getResultList();

        return bugList;
    }

    @Override
    @Transactional
    public List<Bug> getBugListForUser(String username) {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //create the query to get the bug list from mysql
        /////////////////
        //NOTE TO SELF://
        /////////////////
        //Since the query below is done in HQL the "from Bug" should refer to the @Entity class name
        //not to the table name, which in this case is "bugs".
        Query<Bug> theQuery = currentSession.createQuery("from Bug where responsible_dev = '" + username + "'", Bug.class);

        //execute query and get result list
        List<Bug> bugList = theQuery.getResultList();

        return bugList;
    }

    @Override
    @Transactional
    public List<Bug> getActiveBugListForUser(String username) {
        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //create the query to get the bug list from mysql
        /////////////////
        //NOTE TO SELF://
        /////////////////
        //Since the query below is done in HQL the "from Bug" should refer to the @Entity class name
        //not to the table name, which in this case is "bugs".
        Query<Bug> theQuery = currentSession.createQuery("from Bug where responsible_dev = '" + username + "'" + "AND status = 'not crushed'", Bug.class);

        //execute query and get result list
        List<Bug> bugList = theQuery.getResultList();

        return bugList;
    }

    @Override
    @Transactional
    public List<Bug> getProjectActiveBugsByUser(Project project, String username) {

        Session currentSession = entityManager.unwrap(Session.class);

        //create the query to get the bug list from mysql
        /////////////////
        //NOTE TO SELF://
        /////////////////
        //Since the query below is done in HQL the "from Bug" should refer to the @Entity class name
        //not to the table name, which in this case is "bugs".
        Query<Bug> theQuery = currentSession.createQuery("from Bug where responsible_dev = '" + username + "'" + "AND status = 'not crushed' AND project_id = '" + project.getProjectId() + "'", Bug.class);

        List<Bug> bugListByProject = theQuery.getResultList();

        return bugListByProject;
    }

    @Override
    @Transactional
    public Bug getBugByBugId(int bugId) {

        Session currentSession = entityManager.unwrap(Session.class);

        Query<Bug> theQuery = currentSession.createQuery("from Bug where bug_id = '" + bugId + "'", Bug.class);

        Bug theBug = theQuery.getSingleResult();

        return theBug;
    }

    @Override
    @Transactional
    public void save(Bug theBug) {

        Session currentSession = entityManager.unwrap(Session.class);

        currentSession.saveOrUpdate(theBug);
    }

    @Override
    @Transactional
    public void delete(int bugId) {

        

        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("delete from Bug where bug_id = '" + bugId + "'");



        theQuery.executeUpdate();

    }
}

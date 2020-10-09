package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;
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
}

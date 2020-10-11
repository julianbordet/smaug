package cc.jbdev.smaug.dao;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.Project;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {


    private EntityManager entityManager;

    @Autowired
    public ProjectDAOImpl(EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }

    @Override
    @Transactional
    public List<Project> getProjectsList() {

        //get current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        //create the query to get the bug list from mysql
        /////////////////
        //NOTE TO SELF://
        /////////////////
        //Since the query below is done in HQL the "from Project" should refer to the @Entity class name
        //not to the table name, which in this case is "projects".
        Query<Project> theQuery = currentSession.createQuery("from Project", Project.class);

        //execute query and get result list
        List<Project> projectList = theQuery.getResultList();

        return projectList;

    }

    @Override
    @Transactional
    public List<Project> getActiveProjectsListForUser(String username) {

        Session currentSession = entityManager.unwrap(Session.class);

        Query<Project> theQuery = currentSession.createQuery("from Project where owner = '" + username + "'" + "AND is_active = '1'", Project.class);

        List<Project> projectList = theQuery.getResultList();

        return projectList;
    }
}

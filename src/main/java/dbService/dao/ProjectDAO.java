package dbService.dao;

import dbService.dataSets.ProjectDataSet;
import dbService.dataSets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Evgeny Levin
 */
public class ProjectDAO {
    private final Session session;

    public ProjectDAO(Session session) {
        this.session = session;
    }

    public ProjectDataSet get(long id)  throws HibernateException {
        return (ProjectDataSet) session.get(ProjectDataSet.class, id);
    }

    public ProjectDataSet get(String title)  throws HibernateException {
        Criteria criteria = session.createCriteria(ProjectDataSet.class);
        return (ProjectDataSet) criteria.add(Restrictions.eq("title", title)).uniqueResult();
    }

    public long addProject(String title, String description, UserDataSet creator) {
        return (long) session.save(new ProjectDataSet(title, description, creator));
    }

    public List selectAll() {
        Criteria criteria = session.createCriteria(ProjectDataSet.class);
        return criteria.list();
    }
}

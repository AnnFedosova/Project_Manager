package dbService.dao;

import dbService.dataSets.ProjectDataSet;
import dbService.dataSets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.awt.*;
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
        return session.get(ProjectDataSet.class, id);
    }

    public ProjectDataSet get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectDataSet> criteria = builder.createQuery(ProjectDataSet.class);
        Root<ProjectDataSet> root = criteria.from(ProjectDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<ProjectDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addProject(String title, String description, UserDataSet creator) {
        return (long) session.save(new ProjectDataSet(title, description, creator));
    }
    public long addProject(ProjectDataSet project) {
        return (long) session.save(project);
    }

    public List<ProjectDataSet> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectDataSet> criteria = builder.createQuery(ProjectDataSet.class);
        Root<ProjectDataSet> root = criteria.from(ProjectDataSet.class);
        criteria.select(root);
        Query<ProjectDataSet> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

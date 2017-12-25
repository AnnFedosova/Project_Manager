package dbService.dao;

import dbService.entities.ProjectEntity;
import dbService.entities.UserEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class ProjectDAO {
    private final Session session;

    public ProjectDAO(Session session) {
        this.session = session;
    }

    public ProjectEntity get(long id)  throws HibernateException {
        return session.get(ProjectEntity.class, id);
    }

    public ProjectEntity get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectEntity> criteria = builder.createQuery(ProjectEntity.class);
        Root<ProjectEntity> root = criteria.from(ProjectEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<ProjectEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addProject(String title, String description, UserEntity creator) {
        return (long) session.save(new ProjectEntity(title, description, creator));
    }
    public long addProject(ProjectEntity project) {
        return (long) session.save(project);
    }

    public List<ProjectEntity> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProjectEntity> criteria = builder.createQuery(ProjectEntity.class);
        Root<ProjectEntity> root = criteria.from(ProjectEntity.class);
        criteria.select(root);
        Query<ProjectEntity> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

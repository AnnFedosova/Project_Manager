package dbService.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import dbService.entities.TaskStateEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

/**
 * @author Evgeny Levin
 */
public class TaskStateDAO {
    private final Session session;

    public TaskStateDAO(Session session) {
        this.session = session;
    }

    public TaskStateEntity get(long id)  throws HibernateException {
        return session.get(TaskStateEntity.class, id);
    }

    public TaskStateEntity get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskStateEntity> criteria = builder.createQuery(TaskStateEntity.class);
        Root<TaskStateEntity> root = criteria.from(TaskStateEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<TaskStateEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String title) {
        return (long) session.save(new TaskStateEntity(title));
    }

    public long addState(TaskStateEntity state) {
        return (long) session.save(state);
    }
}

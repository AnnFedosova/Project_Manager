package dbService.dao;

import dbService.dataSets.TaskStateDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class TaskStateDAO {
    private final Session session;

    public TaskStateDAO(Session session) {
        this.session = session;
    }

    public TaskStateDataSet get(long id)  throws HibernateException {
        return session.get(TaskStateDataSet.class, id);
    }

    public TaskStateDataSet get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskStateDataSet> criteria = builder.createQuery(TaskStateDataSet.class);
        Root<TaskStateDataSet> root = criteria.from(TaskStateDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<TaskStateDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String name) {
        return (long) session.save(new TaskStateDataSet(name));
    }

    public long addState(TaskStateDataSet state) {
        return (long) session.save(state);
    }
}

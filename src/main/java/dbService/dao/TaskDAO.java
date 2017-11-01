package dbService.dao;

import dbService.dataSets.TaskDataSet;
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
public class TaskDAO {
    private final Session session;

    public TaskDAO(Session session) {
        this.session = session;
    }

    public TaskDataSet get(long id)  throws HibernateException {
        return session.get(TaskDataSet.class, id);
    }

    public TaskDataSet get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskDataSet> criteria = builder.createQuery(TaskDataSet.class);

        Root<TaskDataSet> root = criteria.from(TaskDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<TaskDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addTask(TaskDataSet task) {
        return (long) session.save(task);
    }

    public List<TaskDataSet> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskDataSet> criteria = builder.createQuery(TaskDataSet.class);
        Root<TaskDataSet> root = criteria.from(TaskDataSet.class);
        criteria.select(root);
        Query<TaskDataSet> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

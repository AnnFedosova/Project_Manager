package dbService.dao;

import dbService.entities.RequestEntity;
import dbService.entities.TaskEntity;
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

    public TaskEntity get(long id)  throws HibernateException {
        return session.get(TaskEntity.class, id);
    }

    public void update(TaskEntity task) throws HibernateException {
        session.update(task);
    }

    public TaskEntity get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteria = builder.createQuery(TaskEntity.class);

        Root<TaskEntity> root = criteria.from(TaskEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<TaskEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addTask(TaskEntity task) {
        return (long) session.save(task);
    }

    public List<TaskEntity> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteria = builder.createQuery(TaskEntity.class);
        Root<TaskEntity> root = criteria.from(TaskEntity.class);
        criteria.select(root);
        Query<TaskEntity> query = session.createQuery(criteria);
        return query.getResultList();
    }

    public List<TaskEntity> getTasksByReauestId(long requestId) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteria = builder.createQuery(TaskEntity.class);

        Root<TaskEntity> root = criteria.from(TaskEntity.class);
        ParameterExpression<RequestEntity> parameter = builder.parameter(RequestEntity.class);
        criteria.select(root).where(builder.equal(root.get("request"), parameter));
        Query<TaskEntity> query = session.createQuery(criteria);
        RequestDAO requestDAO = new RequestDAO(session);
        query.setParameter(parameter, requestDAO.get(requestId));
        return query.getResultList();
    }
}

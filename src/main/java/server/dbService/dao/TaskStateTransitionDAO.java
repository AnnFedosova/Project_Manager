package server.dbService.dao;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.dbService.entities.TaskStateEntity;
import server.dbService.entities.TaskStateTransitionEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class TaskStateTransitionDAO {
    private final Session session;

    public TaskStateTransitionDAO(Session session) {
        this.session = session;
    }

    public void add (TaskStateEntity from, TaskStateEntity to) {
        session.save(new TaskStateTransitionEntity(from, to));
    }

    public List<TaskStateTransitionEntity> getStateTransitionsList(TaskStateEntity fromState)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<TaskStateTransitionEntity> criteria = builder.createQuery(TaskStateTransitionEntity.class);
        Root<TaskStateTransitionEntity> root = criteria.from(TaskStateTransitionEntity.class);
        ParameterExpression<TaskStateEntity> parameter = builder.parameter(TaskStateEntity.class);
        criteria.select(root).where(builder.equal(root.get("fromState"), parameter));
        Query<TaskStateTransitionEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, fromState);

        return query.getResultList();
    }
}

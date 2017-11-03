package dbService.dao;

import dbService.entities.StateEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

/**
 * @author Evgeny Levin
 */
public class StateDAO {
    private final Session session;

    public StateDAO(Session session) {
        this.session = session;
    }

    public StateEntity get(long id)  throws HibernateException {
        return session.get(StateEntity.class, id);
    }

    public StateEntity get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<StateEntity> criteria = builder.createQuery(StateEntity.class);
        Root<StateEntity> root = criteria.from(StateEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<StateEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String name, boolean requestAccord, boolean tasksAccord) {
        return (long) session.save(new StateEntity(name, requestAccord, tasksAccord));
    }

    public long addState(StateEntity state) {
        return (long) session.save(state);
    }
}

package dbService.dao;

import dbService.dataSets.StateDataSet;
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

    public StateDataSet get(long id)  throws HibernateException {
        return session.get(StateDataSet.class, id);
    }

    public StateDataSet get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<StateDataSet> criteria = builder.createQuery(StateDataSet.class);
        Root<StateDataSet> root = criteria.from(StateDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<StateDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String name, boolean requestAccord, boolean tasksAccord) {
        return (long) session.save(new StateDataSet(name, requestAccord, tasksAccord));
    }

    public long addState(StateDataSet state) {
        return (long) session.save(state);
    }
}

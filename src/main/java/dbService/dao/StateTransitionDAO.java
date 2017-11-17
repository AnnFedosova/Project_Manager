package dbService.dao;

import dbService.entities.StateEntity;
import dbService.entities.StateTransitionEntity;
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
public class StateTransitionDAO {
    private final Session session;

    public StateTransitionDAO(Session session) {
        this.session = session;
    }

    public void add (StateEntity from, StateEntity to) {
        session.save(new StateTransitionEntity(from, to));
    }

    public List<StateTransitionEntity> getStateTransitionsList(StateEntity fromState)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<StateTransitionEntity> criteria = builder.createQuery(StateTransitionEntity.class);
        Root<StateTransitionEntity> root = criteria.from(StateTransitionEntity.class);
        ParameterExpression<StateEntity> parameter = builder.parameter(StateEntity.class);
        criteria.select(root).where(builder.equal(root.get("fromState"), parameter));
        Query<StateTransitionEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, fromState);

        return query.getResultList();
    }
}

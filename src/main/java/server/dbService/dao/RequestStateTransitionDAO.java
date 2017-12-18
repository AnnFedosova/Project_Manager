package server.dbService.dao;

import server.dbService.entities.RequestStateEntity;
import server.dbService.entities.RequestStateTransitionEntity;
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
public class RequestStateTransitionDAO {
    private final Session session;

    public RequestStateTransitionDAO(Session session) {
        this.session = session;
    }

    public void add (RequestStateEntity from, RequestStateEntity to) {
        session.save(new RequestStateTransitionEntity(from, to));
    }

    public List<RequestStateTransitionEntity> getStateTransitionsList(RequestStateEntity fromState)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<RequestStateTransitionEntity> criteria = builder.createQuery(RequestStateTransitionEntity.class);
        Root<RequestStateTransitionEntity> root = criteria.from(RequestStateTransitionEntity.class);
        ParameterExpression<RequestStateEntity> parameter = builder.parameter(RequestStateEntity.class);
        criteria.select(root).where(builder.equal(root.get("fromState"), parameter));
        Query<RequestStateTransitionEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, fromState);

        return query.getResultList();
    }
}

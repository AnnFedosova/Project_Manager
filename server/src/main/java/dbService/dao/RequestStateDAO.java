package dbService.dao;

import dbService.entities.RequestStateEntity;
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
public class RequestStateDAO {
    private final Session session;

    public RequestStateDAO(Session session) {
        this.session = session;
    }

    public RequestStateEntity get(long id)  throws HibernateException {
        return session.get(RequestStateEntity.class, id);
    }

    public RequestStateEntity get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestStateEntity> criteria = builder.createQuery(RequestStateEntity.class);
        Root<RequestStateEntity> root = criteria.from(RequestStateEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<RequestStateEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String title) {
        return (long) session.save(new RequestStateEntity(title));
    }

    public long addState(RequestStateEntity state) {
        return (long) session.save(state);
    }
}

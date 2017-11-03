package dbService.dao;

import dbService.entities.RequestEntity;
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
public class RequestDAO {
    private final Session session;

    public RequestDAO(Session session) {
        this.session = session;
    }

    public RequestEntity get(long id)  throws HibernateException {
        return session.get(RequestEntity.class, id);
    }

    public RequestEntity get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestEntity> criteria = builder.createQuery(RequestEntity.class);

        Root<RequestEntity> root = criteria.from(RequestEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<RequestEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addRequest(RequestEntity request) {
        return (long) session.save(request);
    }

    public List<RequestEntity> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestEntity> criteria = builder.createQuery(RequestEntity.class);
        Root<RequestEntity> root = criteria.from(RequestEntity.class);
        criteria.select(root);
        Query<RequestEntity> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

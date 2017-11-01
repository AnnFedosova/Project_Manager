package dbService.dao;

import dbService.dataSets.RequestDataSet;
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

    public RequestDataSet get(long id)  throws HibernateException {
        return session.get(RequestDataSet.class, id);
    }

    public RequestDataSet get(String title)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestDataSet> criteria = builder.createQuery(RequestDataSet.class);

        Root<RequestDataSet> root = criteria.from(RequestDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("title"), parameter));
        Query<RequestDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, title);
        return query.uniqueResult();
    }

    public long addRequest(RequestDataSet request) {
        return (long) session.save(request);
    }

    public List<RequestDataSet> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestDataSet> criteria = builder.createQuery(RequestDataSet.class);
        Root<RequestDataSet> root = criteria.from(RequestDataSet.class);
        criteria.select(root);
        Query<RequestDataSet> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

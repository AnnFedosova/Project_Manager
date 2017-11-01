package dbService.dao;

import dbService.dataSets.RequestStateDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class RequestStateDAO {
    private final Session session;

    public RequestStateDAO(Session session) {
        this.session = session;
    }

    public RequestStateDataSet get(long id)  throws HibernateException {
        return session.get(RequestStateDataSet.class, id);
    }

    public RequestStateDataSet get(String name)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RequestStateDataSet> criteria = builder.createQuery(RequestStateDataSet.class);
        Root<RequestStateDataSet> root = criteria.from(RequestStateDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<RequestStateDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, name);
        return query.uniqueResult();
    }

    public long addState(String name) {
        return (long) session.save(new RequestStateDataSet(name));
    }

    public long addState(RequestStateDataSet state) {
        return (long) session.save(state);
    }
}

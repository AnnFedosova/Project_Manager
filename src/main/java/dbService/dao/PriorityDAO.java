package dbService.dao;

import dbService.dataSets.PriorityDataSet;
import dbService.dataSets.UserDataSet;
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
public class PriorityDAO {
    private final Session session;

    public PriorityDAO(Session session) {
        this.session = session;
    }

    public PriorityDataSet get(long id)  throws HibernateException {
        return session.get(PriorityDataSet.class, id);
    }

    public PriorityDataSet get(String priorityName)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PriorityDataSet> criteria = builder.createQuery(PriorityDataSet.class);

        Root<PriorityDataSet> root = criteria.from(PriorityDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<PriorityDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, priorityName);
        return query.uniqueResult();
    }

    public long addPriority(String name) {
        return (long) session.save(new PriorityDataSet(name));
    }

    public long addPriority(PriorityDataSet priority) {
        return (long) session.save(priority);
    }
}

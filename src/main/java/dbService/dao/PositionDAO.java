package dbService.dao;

import dbService.dataSets.PositionDataSet;
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
public class PositionDAO {
    private final Session session;

    public PositionDAO(Session session) {
        this.session = session;
    }

    public PositionDataSet get(long id) throws HibernateException {
        return session.get(PositionDataSet.class, id);
    }


    public PositionDataSet get(String positionName) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PositionDataSet> criteria = builder.createQuery(PositionDataSet.class);

        Root<PositionDataSet> root = criteria.from(PositionDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<PositionDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, positionName);
        return query.uniqueResult();
    }

//    public long getPositionId(String positionName) throws HibernateException {
//        Criteria criteria = session.createCriteria(PositionDataSet.class);
//        return ((PositionDataSet) criteria.add(Restrictions.eq("name", positionName)).uniqueResult()).getId();
//    }

    public long addPosition(String positonName) throws HibernateException {
        return (long) session.save(new PositionDataSet(positonName));
    }

    public long addPosition(PositionDataSet position) throws HibernateException {
        return (long) session.save(position);
    }
}

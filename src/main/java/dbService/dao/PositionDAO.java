package dbService.dao;

import dbService.entities.PositionEntity;
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

    public PositionEntity get(long id) throws HibernateException {
        return session.get(PositionEntity.class, id);
    }


    public PositionEntity get(String positionName) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PositionEntity> criteria = builder.createQuery(PositionEntity.class);

        Root<PositionEntity> root = criteria.from(PositionEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<PositionEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, positionName);
        return query.uniqueResult();
    }

//    public long getPositionId(String positionName) throws HibernateException {
//        Criteria criteria = session.createCriteria(PositionEntity.class);
//        return ((PositionEntity) criteria.add(Restrictions.eq("name", positionName)).uniqueResult()).getId();
//    }

    public long addPosition(String positonName) throws HibernateException {
        return (long) session.save(new PositionEntity(positonName));
    }

    public long addPosition(PositionEntity position) throws HibernateException {
        return (long) session.save(position);
    }
}

package dbService.dao;

import dbService.dataSets.RoleDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

/**
 * @author Evgeny Levin
 */
public class RoleDAO {
    private final Session session;

    public RoleDAO(Session session) {
        this.session = session;
    }

    public RoleDataSet get(long id) throws HibernateException {
        return session.get(RoleDataSet.class, id);
    }

    public RoleDataSet get(String roleName) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RoleDataSet> criteria = builder.createQuery(RoleDataSet.class);
        Root<RoleDataSet> root = criteria.from(RoleDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<RoleDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, roleName);
        return query.uniqueResult();
    }

//    public long getRoleId(String roleName) throws HibernateException {
//        Criteria criteria = session.createCriteria(RoleDataSet.class);
//        return ((RoleDataSet) criteria.add(Restrictions.eq("name", roleName)).uniqueResult()).getId();
//    }

    public long addRole(String roleName) throws HibernateException {
        return (long) session.save(new RoleDataSet(roleName));
    }
}

package server.dbService.dao;

import server.dbService.entities.RoleEntity;
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
public class RoleDAO {
    private final Session session;

    public RoleDAO(Session session) {
        this.session = session;
    }

    public RoleEntity get(long id) throws HibernateException {
        return session.get(RoleEntity.class, id);
    }

    public RoleEntity get(String roleName) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RoleEntity> criteria = builder.createQuery(RoleEntity.class);
        Root<RoleEntity> root = criteria.from(RoleEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), parameter));
        Query<RoleEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, roleName);
        return query.uniqueResult();
    }

//    public long getRoleId(String roleName) throws HibernateException {
//        Criteria criteria = session.createCriteria(RoleEntity.class);
//        return ((RoleEntity) criteria.add(Restrictions.eq("name", roleName)).uniqueResult()).getId();
//    }

    public long addRole(String roleName) throws HibernateException {
        return (long) session.save(new RoleEntity(roleName));
    }
}

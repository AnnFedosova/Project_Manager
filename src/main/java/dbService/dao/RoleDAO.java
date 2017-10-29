package dbService.dao;

import dbService.dataSets.RoleDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author Evgeny Levin
 */
public class RoleDAO {
    private final Session session;

    public RoleDAO(Session session) {
        this.session = session;
    }

    public RoleDataSet get(long id) throws HibernateException {
        return (RoleDataSet) session.get(RoleDataSet.class, id);
    }

    public long getRoleId(String roleName) throws HibernateException {
        Criteria criteria = session.createCriteria(RoleDataSet.class);
        return ((RoleDataSet) criteria.add(Restrictions.eq("roleName", roleName)).uniqueResult()).getId();
    }

    public long addRole(String roleName) throws HibernateException {
        return (long) session.save(new RoleDataSet(roleName));
    }

}

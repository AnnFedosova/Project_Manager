package dbService.dao;

import dbService.dataSets.RolesDataset;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class RolesDAO {
    private final Session session;

    public RolesDAO(Session session) {
        this.session = session;
    }

    public RolesDataset get(int id) throws HibernateException {
        return (RolesDataset) session.get(RolesDataset.class, id);
    }

    public int getRoleId(String roleName) throws HibernateException {
        Criteria criteria = session.createCriteria(RolesDataset.class);
        return ((RolesDataset) criteria.add(Restrictions.eq("role_name", roleName)).uniqueResult()).getId();
    }

    public int addRole(String roleName) throws HibernateException {
        return (int) session.save(new RolesDataset(roleName));
    }

}

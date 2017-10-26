package dbService.dao;

import dbService.dataSets.UserRoleDataset;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserRolesDAO {
    private final Session session;

    public UserRolesDAO(Session session) {
        this.session = session;
    }

    public void addUserRole(long userId, long roleId) throws HibernateException {
        session.save(new UserRoleDataset(userId, roleId));
    }
}

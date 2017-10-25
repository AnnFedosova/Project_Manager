package dbService.dao;

import dbService.dataSets.UserRolesDataset;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserRolesDAO {
    private final Session session;

    public UserRolesDAO(Session session) {
        this.session = session;
    }

    public void addUserRole(int userId, int roleId) throws HibernateException {
        session.save(new UserRolesDataset(userId, roleId));
    }
}

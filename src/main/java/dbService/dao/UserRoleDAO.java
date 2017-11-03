package dbService.dao;

import dbService.entities.RoleEntity;
import dbService.entities.UserEntity;
import dbService.entities.UserRoleEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author Evgeny Levin
 */
public class UserRoleDAO {
    private final Session session;

    public UserRoleDAO(Session session) {
        this.session = session;
    }

    public void addUserRole(UserEntity user, RoleEntity role) throws HibernateException {
        session.save(new UserRoleEntity(user, role));
    }
}

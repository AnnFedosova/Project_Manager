package server.dbService.dao;

import server.dbService.entities.RoleEntity;
import server.dbService.entities.UserEntity;
import server.dbService.entities.UserRoleEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public List<UserRoleEntity> getRolesByUser(String userLogin)  throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserRoleEntity> criteria = builder.createQuery(UserRoleEntity.class);
        Root<UserRoleEntity> root = criteria.from(UserRoleEntity.class);
        ParameterExpression<UserEntity> parameter = builder.parameter(UserEntity.class);
        criteria.select(root).where(builder.equal(root.get("user"), parameter));
        Query<UserRoleEntity> query = session.createQuery(criteria);
        UserDAO userDAO = new UserDAO(session);
        query.setParameter(parameter, userDAO.get(userLogin));
        return query.getResultList();
    }
}

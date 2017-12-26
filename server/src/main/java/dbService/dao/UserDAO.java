package dbService.dao;

import dbService.entities.UserEntity;
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
public class UserDAO {
    private final Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public UserEntity get(long id) throws HibernateException {
        return session.get(UserEntity.class, id);
    }

//    public UserEntity get(String login) throws HibernateException {
//        Criteria criteria = session.createCriteria(UserEntity.class);
//        return (UserEntity) criteria.add(Restrictions.eq("login", login)).uniqueResult();
//    }

    public UserEntity get(String login) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);

        Root<UserEntity> root = criteria.from(UserEntity.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("login"), parameter));
        Query<UserEntity> query = session.createQuery(criteria);
        query.setParameter(parameter, login);
        return query.uniqueResult();
    }

//    public long getUserId(String login) throws HibernateException {
//        Criteria criteria = session.createCriteria(UserEntity.class);
//        return ((UserEntity) criteria.add(Restrictions.eq("login", login)).uniqueResult()).getId();
//    }

    public void update(UserEntity user) throws HibernateException {
        session.update(user);
    }

    public long addUser(UserEntity user) throws HibernateException {
        return (long) session.save(user);
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName) throws HibernateException {
        return (long) session.save(new UserEntity(login, password, internal, firstName, lastName));
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String patronymic) throws HibernateException {
        return (long) session.save(new UserEntity(login, password, internal, firstName, lastName, patronymic));
    }

    public List<UserEntity> selectAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = criteria.from(UserEntity.class);
        criteria.select(root);
        Query<UserEntity> query = session.createQuery(criteria);
        return query.getResultList();
    }
}

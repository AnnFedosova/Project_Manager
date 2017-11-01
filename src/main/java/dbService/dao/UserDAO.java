package dbService.dao;

import dbService.dataSets.UserDataSet;
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
public class UserDAO {
    private final Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public UserDataSet get(long id) throws HibernateException {
        return session.get(UserDataSet.class, id);
    }

//    public UserDataSet get(String login) throws HibernateException {
//        Criteria criteria = session.createCriteria(UserDataSet.class);
//        return (UserDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
//    }

    public UserDataSet get(String login) throws HibernateException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);

        Root<UserDataSet> root = criteria.from(UserDataSet.class);
        ParameterExpression<String> parameter = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("login"), parameter));
        Query<UserDataSet> query = session.createQuery(criteria);
        query.setParameter(parameter, login);
        return query.uniqueResult();
    }

//    public long getUserId(String login) throws HibernateException {
//        Criteria criteria = session.createCriteria(UserDataSet.class);
//        return ((UserDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult()).getId();
//    }

    public long addUser(UserDataSet user) throws HibernateException {
        return (long) session.save(user);
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName) throws HibernateException {
        return (long) session.save(new UserDataSet(login, password, internal, firstName, lastName));
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String patronymic) throws HibernateException {
        return (long) session.save(new UserDataSet(login, password, internal, firstName, lastName, patronymic));
    }
}

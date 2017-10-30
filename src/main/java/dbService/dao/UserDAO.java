package dbService.dao;

import dbService.dataSets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author Evgeny Levin
 */
public class UserDAO {
    private final Session session;

    public UserDAO(Session session) {
        this.session = session;
    }

    public UserDataSet get(long id) throws HibernateException {
        return (UserDataSet) session.get(UserDataSet.class, id);
    }

    public UserDataSet get(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        return (UserDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
    }

    public long getUserId(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        return ((UserDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult()).getId();
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName) throws HibernateException {
        return (long) session.save(new UserDataSet(login, password, internal, firstName, lastName));
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String patronymic) throws HibernateException {
        return (long) session.save(new UserDataSet(login, password, internal, firstName, lastName, patronymic));
    }
}

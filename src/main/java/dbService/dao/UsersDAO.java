package dbService.dao;

import dbService.dataSets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsersDAO {
    private final Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(int id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet get(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
    }

    public int getUserId(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult()).getId();
    }

    public int addUser(String login, String password, String firstName, String lastName) throws HibernateException {
        return (int) session.save(new UsersDataSet(login, password, firstName, lastName));
    }

    public int addUser(String login, String password, String firstName, String lastName, String patronymic) throws HibernateException {
        return (int) session.save(new UsersDataSet(login, password, firstName, lastName, patronymic));
    }
}

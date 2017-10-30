package dbService;

import dbService.dao.ProjectDAO;
import dbService.dao.RoleDAO;
import dbService.dao.UserRoleDAO;
import dbService.dao.UserDAO;
import dbService.dataSets.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getPostgresConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Configuration getPostgresConfiguration() {
        return getConfiguration("config/hibernate_Postgres.cfg.xml");
    }

    private Configuration getOracleConfiguration() {
        return getConfiguration("config/hibernate_Oracle.cfg.xml");
    }

    private Configuration getConfiguration(String cfgFilePath) {
        Configuration configuration = new Configuration();
        configuration.configure(cfgFilePath);
        configuration.configure("config/hibernate_all.cfg.xml");
        addTables(configuration);
        return configuration;
    }

    private void addTables(Configuration configuration) {
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(RoleDataSet.class);
        configuration.addAnnotatedClass(UserRoleDataset.class);
        configuration.addAnnotatedClass(ProjectDataSet.class);
        configuration.addAnnotatedClass(PositionDataSet.class);
        configuration.addAnnotatedClass(StateDataSet.class);
        configuration.addAnnotatedClass(RequestDataSet.class);
        configuration.addAnnotatedClass(PriorityDataSet.class);
    }

    public void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    //Work with DAO below

    public long addNewRole(String roleName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        RoleDAO roleDAO = new RoleDAO(session);
        long id = roleDAO.addRole(roleName);
        transaction.commit();
        session.close();
        return id;
    }

    public long addNewUser(String login, String password, boolean internal, String firstName, String lastName) throws DBException {
        return addNewUser(login, password, internal, firstName, lastName, null);
    }

    public long addNewUser(String login, String password, boolean internal, String firstName, String lastName, String middleName) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO userDAO = new UserDAO(session);
            RoleDAO roleDAO = new RoleDAO(session);
            UserRoleDAO userRoleDAO = new UserRoleDAO(session);
            long userId = userDAO.addUser(login, password, internal, firstName, lastName, middleName);
            long roleId = roleDAO.getRoleId("user");
            userRoleDAO.addUserRole(userId, roleId);
            transaction.commit();
            session.close();
            return userId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addNewAdmin(String login, String password, String firstName, String lastName) throws DBException {
        return addNewAdmin(login, password, firstName, lastName, null);
    }

    public long addNewAdmin(String login, String password, String firstName, String lastName, String middleName) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO userDAO = new UserDAO(session);
            RoleDAO roleDAO = new RoleDAO(session);
            UserRoleDAO userRoleDAO = new UserRoleDAO(session);
            long userId = userDAO.addUser(login, password, true, firstName, lastName, middleName);
            long roleId = roleDAO.getRoleId("admin");
            userRoleDAO.addUserRole(userId, roleId);
            transaction.commit();
            session.close();
            return userId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List getProjectsList() {
        Session session = sessionFactory.openSession();
        ProjectDAO projectDAO = new ProjectDAO(session);
        List projects = projectDAO.selectAll();
        session.close();
        return projects;
    }

    public long addNewProject(String title, String description, String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ProjectDAO projectDAO = new ProjectDAO(session);
            UserDataSet creator = new UserDAO(session).get(login);
            long projectId = projectDAO.addProject(title, description, creator);
            transaction.commit();
            session.close();
            return projectId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public ProjectDataSet getProject(long id) {
        Session session = sessionFactory.openSession();
        ProjectDAO projectDAO = new ProjectDAO(session);
        ProjectDataSet projet =  projectDAO.get(id);
        session.close();
        return projet;
    }

}

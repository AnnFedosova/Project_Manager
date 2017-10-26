package dbService;

import dbService.dao.RolesDAO;
import dbService.dao.UserRolesDAO;
import dbService.dao.UsersDAO;
import dbService.dataSets.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;


public class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getOracleConfiguration();
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
        configuration.addAnnotatedClass(RequestPositionDataSet.class);
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

    public long addNewUser(String login, String password, String firstName, String lastName) throws DBException {
        return addNewUser(login, password, firstName, lastName, null);
    }

    public long addNewUser(String login, String password, String firstName, String lastName, String patronymic) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO usersDAO = new UsersDAO(session);
            RolesDAO rolesDAO = new RolesDAO(session);
            UserRolesDAO userRolesDAO = new UserRolesDAO(session);
            long userId = usersDAO.addUser(login, password, firstName, lastName, patronymic);
            long roleId = rolesDAO.getRoleId("user");
            userRolesDAO.addUserRole(userId, roleId);
            transaction.commit();
            session.close();
            return userId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

}

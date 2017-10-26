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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "create";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getPostgresConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Configuration getPostgresConfiguration() {
        return getConfiguration("config/hibernate_Postgres.properties");
    }

    private Configuration getOracleConfiguration() {
        return getConfiguration("config/hibernate_Oracle.properties");
    }

    private Configuration getConfiguration(String propertiesFilePath) {
        Configuration configuration = new Configuration();
        setConfigurationProperties(configuration, propertiesFilePath);
        configuration.configure("hibernate_all.cfg.xml");
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
    }

    private void setConfigurationProperties(Configuration configuration, String propertiesFilePath) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(propertiesFilePath)) {
            properties.load(inputStream);
            configuration.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
            configuration.setProperty("hibernate.connection.driver_class", properties.getProperty("hibernate.connection.driver_class"));
            configuration.setProperty("hibernate.connection.url", properties.getProperty("hibernate.connection.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("hibernate.connection.username"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("hibernate.connection.password"));
            configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
            configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

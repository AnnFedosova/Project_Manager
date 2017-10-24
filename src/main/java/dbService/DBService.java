package dbService;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import dbService.dataSets.UsersDataSet;
import dbService.dataSets.RolesDataset;
import dbService.dataSets.UserRolesDataset;

import java.sql.Connection;
import java.sql.SQLException;


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
        Configuration configuration = new Configuration();

        addTables(configuration);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
        configuration.setProperty("hibernate.connection.username", "leo");
        configuration.setProperty("hibernate.connection.password", "leo");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private Configuration getOracleConfiguration() {
        Configuration configuration = new Configuration();

        addTables(configuration);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        configuration.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");
        configuration.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@//localhost:1521/orcl");
        configuration.setProperty("hibernate.connection.username", "project_manager");
        configuration.setProperty("hibernate.connection.password", "pass");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private void addTables(Configuration configuration) {
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(RolesDataset.class);
        configuration.addAnnotatedClass(UserRolesDataset.class);
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
}

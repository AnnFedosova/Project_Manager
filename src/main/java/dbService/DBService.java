package dbService;

import dbService.dao.*;
import dbService.dataSets.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;

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
        //Configuration configuration = getOracleConfiguration();
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
        addTables(configuration);
        return configuration;
    }

    private void addTables(Configuration configuration) {
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(RoleDataSet.class);
        configuration.addAnnotatedClass(UserRoleDataset.class);
        configuration.addAnnotatedClass(ProjectDataSet.class);
        configuration.addAnnotatedClass(PositionDataSet.class);
        configuration.addAnnotatedClass(RequestStateDataSet.class);
        configuration.addAnnotatedClass(RequestDataSet.class);
        configuration.addAnnotatedClass(PriorityDataSet.class);
        configuration.addAnnotatedClass(ProjectPositionDataSet.class);
        configuration.addAnnotatedClass(TaskDataSet.class);
        configuration.addAnnotatedClass(TaskStateDataSet.class);
    }

    public void printConnectInfo() {
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
        sessionFactoryImpl.openSession().doWork(connection -> {
            try {
                System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
                System.out.println("Driver: " + connection.getMetaData().getDriverName());
                System.out.println("Autocommit: " + connection.getAutoCommit());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }



    //Work with DAO below

    //Roles

    public long addRole(String roleName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        RoleDAO roleDAO = new RoleDAO(session);
        long id = roleDAO.addRole(roleName);
        transaction.commit();
        session.close();
        return id;
    }


    //Users
    public long addUser(String login, String password, boolean internal, String firstName, String lastName) throws DBException {
        return addUser(login, password, internal, firstName, lastName, null);
    }

    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String middleName) throws DBException {
        return addPerson(login, password, internal, firstName, lastName, middleName, "user");
    }

    public long addAdmin(String login, String password, String firstName, String lastName) throws DBException {
        return addAdmin(login, password, firstName, lastName, null);
    }

    public long addAdmin(String login, String password, String firstName, String lastName, String middleName) throws DBException {
        return addPerson(login, password, true, firstName, lastName, middleName, "admin");
    }

    private long addPerson(String login, String password, boolean internal, String firstName, String lastName, String middleName, String roleName) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            UserDAO userDAO = new UserDAO(session);
            RoleDAO roleDAO = new RoleDAO(session);
            UserRoleDAO userRoleDAO = new UserRoleDAO(session);

            UserDataSet user = new UserDataSet(login, password, internal, firstName, lastName, middleName);

            long userId = userDAO.addUser(user);
            RoleDataSet role = roleDAO.get(roleName);
            userRoleDAO.addUserRole(user, role);
            transaction.commit();
            session.close();
            return userId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }


    //Projects
    public List getProjectsList() {
        Session session = sessionFactory.openSession();
        ProjectDAO projectDAO = new ProjectDAO(session);
        List projects = projectDAO.selectAll();
        session.close();
        return projects;
    }

    public long addProject(String title, String description, String creatorLogin) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProjectDAO projectDAO = new ProjectDAO(session);
            UserDataSet creator = new UserDAO(session).get(creatorLogin);
            ProjectDataSet project = new ProjectDataSet(title, description, creator);
            long projectId = projectDAO.addProject(title, description, creator);

            transaction.commit();

            transaction = session.beginTransaction();
            addProjectPosition(projectId, "Receptionist", creatorLogin);
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


    //Requests
    public List getRequestsList() {
        Session session = sessionFactory.openSession();
        RequestDAO requestDAO = new RequestDAO(session);
        List projects = requestDAO.selectAll();
        session.close();
        return projects;
    }

//    public long addRequest(String title, String description, String creatorLogin, String customerLogin, String priorityName, String projectTitle) throws DBException {
//        try {
//            Session session = sessionFactory.openSession();
//            Transaction transaction = session.beginTransaction();
//
//            RequestDAO requestDAO = new RequestDAO(session);
//            UserDAO userDAO = new UserDAO(session);
//            PriorityDAO priorityDAO = new PriorityDAO(session);
//            RequestStateDAO requestStateDAO = new RequestStateDAO(session);
//            ProjectDAO projectDAO = new ProjectDAO(session);
//            ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);
//
//            ProjectDataSet project = projectDAO.get(projectTitle);
//            UserDataSet creator = userDAO.get(creatorLogin);
//            UserDataSet customer = userDAO.get(customerLogin);
//            List<ProjectPositionDataSet> creatorPositions = projectPositionDAO.get(creator);
//            List<ProjectPositionDataSet> customerPositions = projectPositionDAO.get(customer);
//
//
//            ProjectPositionDataSet creatorPosition = null;
//            ProjectPositionDataSet customerPosition = null;
//            RequestStateDataSet state = requestStateDAO.get("New");
//            PriorityDataSet priority = priorityDAO.get(priorityName);
//
//            //TODO добавить проверку при создании проекта
//            //if (creatorPositions.contains())
//
//            for (ProjectPositionDataSet projectPosition: creatorPositions) {
//                if (projectPosition.getPosition().getName().toLowerCase().equals("receptionist")) {
//                    creatorPosition = projectPosition;
//                    break;
//                }
//            }
//
//            for (ProjectPositionDataSet projectPosition: customerPositions) {
//                if (projectPosition.getPosition().getName().toLowerCase().equals("customer")) {
//                    customerPosition = projectPosition;
//                    break;
//                }
//            }
//
//            RequestDataSet request = new RequestDataSet(project, title, description, creatorPosition, customerPosition, state, priority);
//            long requestId = requestDAO.addRequest(request);
//
//            transaction.commit();
//            session.close();
//            return requestId;
//        }
//        catch (HibernateException e) {
//            throw new DBException(e);
//        }
//    }

    //TODO добавить проверку при создании request'а
    public long addRequest(String title, String description, String creatorLogin, String customerLogin, String priorityName, long projectId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            RequestStateDAO requestStateDAO = new RequestStateDAO(session);
            ProjectDAO projectDAO = new ProjectDAO(session);

            ProjectDataSet project = projectDAO.get(projectId);
            UserDataSet creator = userDAO.get(creatorLogin);
            UserDataSet customer = userDAO.get(customerLogin);

            RequestStateDataSet state = requestStateDAO.get("New");
            PriorityDataSet priority = priorityDAO.get(priorityName);

            RequestDataSet request = new RequestDataSet(project, title, description, creator, customer, state, priority);
            long requestId = requestDAO.addRequest(request);

            transaction.commit();
            session.close();
            return requestId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    //Positions

    public long addPosition(String positionName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        PositionDAO positionDAO = new PositionDAO(session);

        long id = positionDAO.addPosition(positionName);

        transaction.commit();
        session.close();
        return id;
    }


    //RequestStates

    public long addRequestState(String stateName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        RequestStateDAO requestStateDAO = new RequestStateDAO(session);

        long id = requestStateDAO.addState(stateName);

        transaction.commit();
        session.close();
        return id;
    }


    //TaskStates

    public long addTaskState(String stateName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        TaskStateDAO taskStateDAO = new TaskStateDAO(session);

        long id = taskStateDAO.addState(stateName);

        transaction.commit();
        session.close();
        return id;
    }

    //Tasks
    //TODO добавить проверку при создании task'а
    public long addTask(String title, String description, String creatorLogin, String executorLogin, String priorityName, long requestId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            TaskStateDAO taskStateDAO = new TaskStateDAO(session);
            TaskDAO taskDAO = new TaskDAO(session);

            RequestDataSet request = requestDAO.get(requestId);
            UserDataSet creator = userDAO.get(creatorLogin);
            UserDataSet executor = userDAO.get(executorLogin);

            TaskStateDataSet state = taskStateDAO.get("New");

            TaskDataSet taskDataSet = new TaskDataSet(title, description, request, creator, executor, state);
            long taskId = taskDAO.addTask(taskDataSet);

            transaction.commit();
            session.close();
            return taskId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    //ProjectPositions

    public long addProjectPosition(long projectId, String positionName, String userLogin) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);

        long id = projectPositionDAO.addProjectPosition(projectId, positionName, userLogin);

        transaction.commit();
        session.close();
        return id;
    }


    //Priorities

    public long addPriority(String priorityName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        PriorityDAO priorityDAO = new PriorityDAO(session);
        long id = priorityDAO.addPriority(priorityName);

        transaction.commit();
        session.close();
        return id;
    }

}

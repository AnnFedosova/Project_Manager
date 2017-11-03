package dbService;

import dbService.dao.*;
import dbService.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public DBService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.configure("cfg/hibernate.cfg.xml");
        addTables(configuration);
        return configuration;
    }

    private void addTables(Configuration configuration) {
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(RoleEntity.class);
        configuration.addAnnotatedClass(UserRoleEntity.class);
        configuration.addAnnotatedClass(ProjectEntity.class);
        configuration.addAnnotatedClass(PositionEntity.class);
        configuration.addAnnotatedClass(StateEntity.class);
        configuration.addAnnotatedClass(RequestEntity.class);
        configuration.addAnnotatedClass(PriorityEntity.class);
        configuration.addAnnotatedClass(ProjectPositionEntity.class);
        configuration.addAnnotatedClass(TaskEntity.class);
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
    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String middleName) throws DBException {
        return addPerson(login, password, internal, firstName, lastName, middleName, "user");
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

            UserEntity user = new UserEntity(login, password, internal, firstName, lastName, middleName);

            long userId = userDAO.addUser(user);
            RoleEntity role = roleDAO.get(roleName);
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
            UserEntity creator = new UserDAO(session).get(creatorLogin);
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

    public ProjectEntity getProject(long id) {
        Session session = sessionFactory.openSession();
        ProjectDAO projectDAO = new ProjectDAO(session);
        ProjectEntity projet =  projectDAO.get(id);
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
//            StateDAO requestStateDAO = new StateDAO(session);
//            ProjectDAO projectDAO = new ProjectDAO(session);
//            ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);
//
//            ProjectEntity project = projectDAO.get(projectTitle);
//            UserEntity creator = userDAO.get(creatorLogin);
//            UserEntity customer = userDAO.get(customerLogin);
//            List<ProjectPositionEntity> creatorPositions = projectPositionDAO.get(creator);
//            List<ProjectPositionEntity> customerPositions = projectPositionDAO.get(customer);
//
//
//            ProjectPositionEntity creatorPosition = null;
//            ProjectPositionEntity customerPosition = null;
//            StateEntity state = requestStateDAO.get("New");
//            PriorityEntity priority = priorityDAO.get(priorityName);
//
//            //TODO добавить проверку при создании проекта
//            //if (creatorPositions.contains())
//
//            for (ProjectPositionEntity projectPosition: creatorPositions) {
//                if (projectPosition.getPosition().getName().toLowerCase().equals("receptionist")) {
//                    creatorPosition = projectPosition;
//                    break;
//                }
//            }
//
//            for (ProjectPositionEntity projectPosition: customerPositions) {
//                if (projectPosition.getPosition().getName().toLowerCase().equals("customer")) {
//                    customerPosition = projectPosition;
//                    break;
//                }
//            }
//
//            RequestEntity request = new RequestEntity(project, title, description, creatorPosition, customerPosition, state, priority);
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
            StateDAO stateDAO = new StateDAO(session);
            ProjectDAO projectDAO = new ProjectDAO(session);

            ProjectEntity project = projectDAO.get(projectId);
            UserEntity creator = userDAO.get(creatorLogin);
            UserEntity customer = userDAO.get(customerLogin);

            StateEntity state = stateDAO.get("New");
            PriorityEntity priority = priorityDAO.get(priorityName);

            RequestEntity request = new RequestEntity(project, title, description, creator, customer, state, priority);
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


    //States

    public long addState(String stateName, boolean requestAccord, boolean tasksAccord) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        StateDAO stateDAO = new StateDAO(session);

        long id = stateDAO.addState(stateName, requestAccord, tasksAccord);

        transaction.commit();
        session.close();
        return id;
    }

    //Tasks
    //TODO добавить проверку при создании task'а
    public long addTask(String title, String description, String creatorLogin, String executorLogin, long requestId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            StateDAO stateDAO = new StateDAO(session);
            TaskDAO taskDAO = new TaskDAO(session);

            RequestEntity request = requestDAO.get(requestId);
            UserEntity creator = userDAO.get(creatorLogin);
            UserEntity executor = userDAO.get(executorLogin);

            StateEntity state = stateDAO.get("New");

            TaskEntity taskEntity = new TaskEntity(title, description, request, creator, executor, state);
            long taskId = taskDAO.addTask(taskEntity);

            transaction.commit();
            session.close();
            return taskId;
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    //ProjectPositions

    public void addProjectPosition(long projectId, String positionName, String userLogin) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);

        projectPositionDAO.addProjectPosition(projectId, positionName, userLogin);

        transaction.commit();
        session.close();
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

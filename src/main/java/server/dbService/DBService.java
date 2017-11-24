package server.dbService;

import server.dbService.dao.*;
import server.dbService.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.jetty.util.security.Password;

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
        configuration.configure("hibernate.cfg.xml");
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
        configuration.addAnnotatedClass(StateTransitionEntity.class);
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

    public String getConnectInfo() {
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
        StringBuffer info = new StringBuffer();
        sessionFactoryImpl.openSession().doWork(connection -> {
            try {
                info.append("DB name: ").append(connection.getMetaData().getDatabaseProductName()).append('\n');
                info.append("DB version: ").append(connection.getMetaData().getDatabaseProductVersion()).append('\n');
                info.append("Driver: ").append(connection.getMetaData().getDriverName()).append('\n');
                info.append("Autocommit: ").append(connection.getAutoCommit()).append('\n');
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return info.toString();
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

    public UserEntity getUser(long id) {
        Session session = sessionFactory.openSession();
        UserDAO userDAO = new UserDAO(session);
        UserEntity user = userDAO.get(id);
        session.close();
        return user;
    }

    public UserEntity getUser(String login) {
        Session session = sessionFactory.openSession();
        UserDAO userDAO = new UserDAO(session);
        UserEntity user = userDAO.get(login);
        session.close();
        return user;
    }

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


            UserEntity user = new UserEntity(login, Password.obfuscate(password), internal, firstName, lastName, middleName);

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

    public List<UserEntity> getAllUsers() {
        Session session = sessionFactory.openSession();
        UserDAO userDAO = new UserDAO(session);
        List<UserEntity> users = userDAO.selectAll();
        session.close();
        return users;
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
    public List<RequestEntity> getRequestssList(long projectId) {
        Session session = sessionFactory.openSession();

        RequestDAO requestDAO = new RequestDAO(session);
        List <RequestEntity> list = requestDAO.getRequestsByProjectId(projectId);

        session.close();
        return list;
    }

    public RequestEntity getRequest(long id) {
        Session session = sessionFactory.openSession();
        RequestDAO requestDAO = new RequestDAO(session);
        RequestEntity request = requestDAO.get(id);
        session.close();
        return request;
    }

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


    //TODO добавить проверку при создании request'а
    public long addRequest(String title, String description, String creatorLogin, long customerId, long priorityId, long projectId) throws DBException {
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
            UserEntity customer = userDAO.get(customerId);

            StateEntity state = stateDAO.get("New");
            PriorityEntity priority = priorityDAO.get(priorityId);

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

    public long addState(StateEntity state) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        StateDAO stateDAO = new StateDAO(session);

        long id = stateDAO.addState(state);

        transaction.commit();
        session.close();
        return id;
    }

    //Tasks

    public TaskEntity getTask(long id) {
        Session session = sessionFactory.openSession();
        TaskDAO taskDAO = new TaskDAO(session);
        TaskEntity task = taskDAO.get(id);
        session.close();
        return task;
    }

    public List<TaskEntity> getTasksList(long requestId) {
        Session session = sessionFactory.openSession();

        TaskDAO taskDAO = new TaskDAO(session);
        List <TaskEntity> list = taskDAO.getTasksByReauestId(requestId);

        session.close();
        return list;
    }

    //TODO добавить проверку при создании task'а
    public long addTask(String title, String description, String creatorLogin, long executorId, long requestId) throws DBException {
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
            UserEntity executor = userDAO.get(executorId);

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

    public void updateTask(TaskEntity task) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        TaskDAO taskDAO = new TaskDAO(session);
        taskDAO.update(task);

        transaction.commit();
        session.close();
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

    public List<ProjectPositionEntity> getProjectPositionsList(long projectId) {
        Session session = sessionFactory.openSession();

        ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);
        List <ProjectPositionEntity> list = projectPositionDAO.getByProjectId(projectId);

        session.close();
        return list;
    }

    public List<ProjectPositionEntity> getProjectPositionsByRequestID(long requestId) {
        Session session = sessionFactory.openSession();

        RequestDAO requestDAO = new RequestDAO(session);
        ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);

        long projectId = requestDAO.get(requestId).getProject().getId();
        List<ProjectPositionEntity> list = projectPositionDAO.getByProjectId(projectId);

        session.close();
        return list;
    }

    public List<ProjectPositionEntity> getProjectPositionsByTaskId(long taskId) {
        Session session = sessionFactory.openSession();

        RequestDAO requestDAO = new RequestDAO(session);
        TaskDAO taskDAO = new TaskDAO(session);
        ProjectPositionDAO projectPositionDAO = new ProjectPositionDAO(session);

        long requestId = taskDAO.get(taskId).getRequest().getId();
        long projectId = requestDAO.get(requestId).getProject().getId();
        List<ProjectPositionEntity> list = projectPositionDAO.getByProjectId(projectId);

        session.close();
        return list;
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

    public List<PriorityEntity> getAllPriorities() {
        Session session = sessionFactory.openSession();
        PriorityDAO priorityDAO = new PriorityDAO(session);
        List<PriorityEntity> priorities = priorityDAO.selectAll();
        session.close();
        return priorities;
    }


    //UserRoles

    public boolean isAdmin(String userLogin) {
        Session session = sessionFactory.openSession();

        UserRoleDAO userRoleDAO = new UserRoleDAO(session);
        long adminRoleId = new RoleDAO(session).get("admin").getId();
        List<UserRoleEntity> roles = userRoleDAO.getRolesByUser(userLogin);
        for (UserRoleEntity role: roles) {
            if (role.getRole().getId() == adminRoleId) {
                session.close();
                return true;
            }
        }
        session.close();
        return false;
    }


    //StateTransitions

    public List<StateTransitionEntity> getStateTransitions(StateEntity fromState, RequestOrTask requestOrTask) {
        Session session = sessionFactory.openSession();
        StateTransitionDAO stateTransitionDAO = new StateTransitionDAO(session);
        List<StateTransitionEntity> transitions = stateTransitionDAO.getStateTransitionsList(fromState);
        session.close();

        if (requestOrTask == RequestOrTask.REQUEST) {
            for (StateTransitionEntity transition : transitions) {
                if (!transition.getFromState().getRequestsAccord()) {
                    transitions.remove(transition);
                }
            }
            return transitions;
        }

        if (requestOrTask == RequestOrTask.TASK) {
            for (StateTransitionEntity transition : transitions) {
                if (!transition.getFromState().getTasksAccord()) {
                    transitions.remove(transition);
                }
            }
            return transitions;
        }

        return null;
    }

    public void addStateTransition(StateEntity fromState, StateEntity toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        StateTransitionDAO stateTransitionDAO = new StateTransitionDAO(session);
        stateTransitionDAO.add(fromState, toState);
        transaction.commit();
        session.close();
    }

    public void addStateTransition(String fromState, String toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        StateDAO stateDAO = new StateDAO(session);
        StateTransitionDAO stateTransitionDAO = new StateTransitionDAO(session);
        StateEntity state1 = stateDAO.get(fromState);
        StateEntity state2 = stateDAO.get(toState);
        stateTransitionDAO.add(state1, state2);
        transaction.commit();
        session.close();
    }


    //Other

    public List<UserEntity> getUsersByProjectId(long projectId) {
        Session session = sessionFactory.openSession();

        Set<UserEntity> users = new TreeSet<>();

        List<ProjectPositionEntity> projectPositions = getProjectPositionsList(projectId);
        for(ProjectPositionEntity projectPosition : projectPositions) {
            users.add(projectPosition.getUser());
        }

        session.close();

        List<UserEntity> list = new ArrayList<>();
        list.addAll(users);
        return list;
    }

    public List<UserEntity> getUsersByRequestId(long requestId) {
        Session session = sessionFactory.openSession();

        Set<UserEntity> users = new TreeSet<>();

        List<ProjectPositionEntity> projectPositions = getProjectPositionsByRequestID(requestId);
        for(ProjectPositionEntity projectPosition : projectPositions) {
            users.add(projectPosition.getUser());
        }

        session.close();

        List<UserEntity> list = new ArrayList<>();
        list.addAll(users);
        return list;
    }

    public List<UserEntity> getUsersByTaskId(long taskId) {
        Session session = sessionFactory.openSession();

        Set<UserEntity> users = new TreeSet<>();

        List<ProjectPositionEntity> projectPositions = getProjectPositionsByTaskId(taskId);
        for(ProjectPositionEntity projectPosition : projectPositions) {
            users.add(projectPosition.getUser());
        }

        session.close();

        List<UserEntity> list = new ArrayList<>();
        list.addAll(users);
        return list;
    }
}
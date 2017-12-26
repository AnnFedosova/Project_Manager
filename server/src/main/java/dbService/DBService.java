package dbService;

import org.jboss.crypto.CryptoUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Evgeny Levin
 */

//TODO: Make DBService abstract


public class DBService {
    public static volatile DBService instance;
    private final SessionFactory sessionFactory;

    private DBService() {
        Configuration configuration = getConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public static DBService getInstance() {
        if (instance == null) {
            synchronized (DBService.class) {
                if (instance == null) {
                    instance = new DBService();
                    dbSetupData(instance);
                    fillDB(instance);
                }
            }
        }
        return instance;

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
        configuration.addAnnotatedClass(RequestStateEntity.class);
        configuration.addAnnotatedClass(TaskStateEntity.class);
        configuration.addAnnotatedClass(RequestEntity.class);
        configuration.addAnnotatedClass(PriorityEntity.class);
        configuration.addAnnotatedClass(ProjectPositionEntity.class);
        configuration.addAnnotatedClass(TaskEntity.class);
        configuration.addAnnotatedClass(RequestStateTransitionEntity.class);
        configuration.addAnnotatedClass(TaskStateTransitionEntity.class);
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


            UserEntity user = new UserEntity(login, CryptoUtil.createPasswordHash("MD5", CryptoUtil.BASE64_ENCODING, null, null, password), internal, firstName, lastName, middleName);

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
    public List<ProjectEntity>  getProjectsList() {
        Session session = sessionFactory.openSession();
        ProjectDAO projectDAO = new ProjectDAO(session);
        List<ProjectEntity> projects = projectDAO.selectAll();
        session.close();
        return projects;
    }

    public long addProject(String title, String description, long creatorId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProjectDAO projectDAO = new ProjectDAO(session);
            UserEntity creator = new UserDAO(session).get(creatorId);
            long projectId = projectDAO.addProject(title, description, creator);

            transaction.commit();

            transaction = session.beginTransaction();
            addProjectPosition(projectId, "Receptionist", creator.getLogin());
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

    public void updateProject(ProjectEntity project) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ProjectDAO projectDAO = new ProjectDAO(session);
        projectDAO.update(project);

        transaction.commit();
        session.close();
    }


    //Requests
    public List<RequestEntity> getRequestsList(long projectId) {
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

    public long addRequest(String title, String description, long creatorId, long customerId, long priorityid, long projectId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            RequestStateDAO stateDAO = new RequestStateDAO(session);
            ProjectDAO projectDAO = new ProjectDAO(session);

            ProjectEntity project = projectDAO.get(projectId);
            UserEntity creator = userDAO.get(creatorId);
            UserEntity customer = userDAO.get(customerId);

            RequestStateEntity state = stateDAO.get("New");
            PriorityEntity priority = priorityDAO.get(priorityid);

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


    public long addRequest(String title, String description, String creatorLogin, long customerId, long priorityId, long projectId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            RequestStateDAO stateDAO = new RequestStateDAO(session);
            ProjectDAO projectDAO = new ProjectDAO(session);

            ProjectEntity project = projectDAO.get(projectId);
            UserEntity creator = userDAO.get(creatorLogin);
            UserEntity customer = userDAO.get(customerId);

            RequestStateEntity state = stateDAO.get("New");
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


    //RequestStates

    public long addRequestState(String stateTitle) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        RequestStateDAO stateDAO = new RequestStateDAO(session);

        long id = stateDAO.addState(stateTitle);

        transaction.commit();
        session.close();
        return id;
    }

    public long addRequestState(RequestStateEntity state) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        RequestStateDAO stateDAO = new RequestStateDAO(session);

        long id = stateDAO.addState(state);

        transaction.commit();
        session.close();
        return id;
    }

    public RequestStateEntity getRequestState(long stateId) {
        Session session = sessionFactory.openSession();
        RequestStateDAO requestStateDAO = new RequestStateDAO(session);
        RequestStateEntity state = requestStateDAO.get(stateId);
        session.close();
        return state;
    }

    public void updateRequest(RequestEntity request) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        RequestDAO requestDAO = new RequestDAO(session);
        requestDAO.update(request);

        transaction.commit();
        session.close();
    }


    //TaskStates

    public long addTaskState(String stateTitle) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        TaskStateDAO stateDAO = new TaskStateDAO(session);

        long id = stateDAO.addState(stateTitle);

        transaction.commit();
        session.close();
        return id;
    }

    public long addTaskState(TaskStateEntity state) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        TaskStateDAO stateDAO = new TaskStateDAO(session);

        long id = stateDAO.addState(state);

        transaction.commit();
        session.close();
        return id;
    }

    public TaskStateEntity getTaskState(long stateId) {
        Session session = sessionFactory.openSession();
        TaskStateDAO taskStateDAO = new TaskStateDAO(session);
        TaskStateEntity state = taskStateDAO.get(stateId);
        session.close();
        return state;
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

    public long addTask(String title, String description, long creatorId, long executorId, long requestId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            RequestDAO requestDAO = new RequestDAO(session);
            UserDAO userDAO = new UserDAO(session);
            PriorityDAO priorityDAO = new PriorityDAO(session);
            TaskStateDAO stateDAO = new TaskStateDAO(session);
            TaskDAO taskDAO = new TaskDAO(session);

            RequestEntity request = requestDAO.get(requestId);
            UserEntity creator = userDAO.get(creatorId);
            UserEntity executor = userDAO.get(executorId);

            TaskStateEntity state = stateDAO.get("New");

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

    public PriorityEntity getPriority(long id) {
        Session session = sessionFactory.openSession();
        PriorityDAO priorityDAO = new PriorityDAO(session);
        PriorityEntity priority = priorityDAO.get(id);
        session.close();
        return priority;
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


    //RequestStateTransitions

    public List<RequestStateTransitionEntity> getRequestStateTransitions(RequestStateEntity fromState) {
        Session session = sessionFactory.openSession();
        RequestStateTransitionDAO stateTransitionDAO = new RequestStateTransitionDAO(session);
        List<RequestStateTransitionEntity> transitions = stateTransitionDAO.getStateTransitionsList(fromState);
        session.close();

        return transitions;
    }

    public void addRequestStateTransition(RequestStateEntity fromState, RequestStateEntity toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        RequestStateTransitionDAO stateTransitionDAO = new RequestStateTransitionDAO(session);
        stateTransitionDAO.add(fromState, toState);
        transaction.commit();
        session.close();
    }

    public void addRequestStateTransition(String fromState, String toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        RequestStateDAO stateDAO = new RequestStateDAO(session);
        RequestStateTransitionDAO stateTransitionDAO = new RequestStateTransitionDAO(session);
        RequestStateEntity state1 = stateDAO.get(fromState);
        RequestStateEntity state2 = stateDAO.get(toState);
        stateTransitionDAO.add(state1, state2);
        transaction.commit();
        session.close();
    }


    //TaskStateTransitions

    public List<TaskStateTransitionEntity> getTaskStateTransitions(TaskStateEntity fromState) {
        Session session = sessionFactory.openSession();
        TaskStateTransitionDAO stateTransitionDAO = new TaskStateTransitionDAO(session);
        List<TaskStateTransitionEntity> transitions = stateTransitionDAO.getStateTransitionsList(fromState);
        session.close();

        return transitions;
    }

    public void addTaskStateTransition(TaskStateEntity fromState, TaskStateEntity toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        TaskStateTransitionDAO stateTransitionDAO = new TaskStateTransitionDAO(session);
        stateTransitionDAO.add(fromState, toState);
        transaction.commit();
        session.close();
    }

    public void addTaskStateTransition(String fromState, String toState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        TaskStateDAO stateDAO = new TaskStateDAO(session);
        TaskStateTransitionDAO stateTransitionDAO = new TaskStateTransitionDAO(session);
        TaskStateEntity state1 = stateDAO.get(fromState);
        TaskStateEntity state2 = stateDAO.get(toState);
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

        return new ArrayList<>(users);
    }

    public List<UserEntity> getUsersByRequestId(long requestId) {
        Session session = sessionFactory.openSession();

        Set<UserEntity> users = new TreeSet<>();

        List<ProjectPositionEntity> projectPositions = getProjectPositionsByRequestID(requestId);
        for(ProjectPositionEntity projectPosition : projectPositions) {
            users.add(projectPosition.getUser());
        }

        session.close();

        return new ArrayList<>(users);
    }

    public List<UserEntity> getUsersByTaskId(long taskId) {
        Session session = sessionFactory.openSession();

        Set<UserEntity> users = new TreeSet<>();

        List<ProjectPositionEntity> projectPositions = getProjectPositionsByTaskId(taskId);
        for(ProjectPositionEntity projectPosition : projectPositions) {
            users.add(projectPosition.getUser());
        }

        session.close();

        return new ArrayList<>(users);
    }

    private static void dbSetupData(DBService dbService) {
        try {
            //Roles
            dbService.addRole("admin");
            dbService.addRole("user");

            //Users
            dbService.addAdmin("admin", "admin", "admin", "admin", "admin");

            //Positions
            dbService.addPosition("Receptionist");
            dbService.addPosition("Customer");
            dbService.addPosition("Quality assurance");
            dbService.addPosition("Developer");
            dbService.addPosition("Team leader");
//            dbService.addPosition("System architect");
            dbService.addPosition("Analyst");
            dbService.addPosition("System administrator");

            //RequestStates
            RequestStateEntity requestStatePostponed = new RequestStateEntity("Postponed");
            RequestStateEntity requestStateComplete = new RequestStateEntity("Complete");
            RequestStateEntity requestStateRejected = new RequestStateEntity("Rejected");
            RequestStateEntity requestStateNew = new RequestStateEntity("New");
            RequestStateEntity requestStateOnTheGo = new RequestStateEntity("On the go");
            RequestStateEntity requestStateClosed = new RequestStateEntity("Closed");

            dbService.addRequestState(requestStateNew);
            dbService.addRequestState(requestStateOnTheGo);
            dbService.addRequestState(requestStateClosed);
            dbService.addRequestState(requestStatePostponed);
            dbService.addRequestState(requestStateComplete);
            dbService.addRequestState(requestStateRejected);

            //TaskStates
            TaskStateEntity taskStateAssigned = new TaskStateEntity("Assigned");
            TaskStateEntity taskStateWaitingForTesting = new TaskStateEntity("Complete, waiting for testing");
            TaskStateEntity taskStateInTesting = new TaskStateEntity("In testing");
            TaskStateEntity taskStateNew = new TaskStateEntity("New");
            TaskStateEntity taskStateOnTheGo = new TaskStateEntity("On the go");
            TaskStateEntity taskStateClosed = new TaskStateEntity("Closed");

            dbService.addTaskState(taskStateNew);
            dbService.addTaskState(taskStateOnTheGo);
            dbService.addTaskState(taskStateClosed);
            dbService.addTaskState(taskStateAssigned);
            dbService.addTaskState(taskStateWaitingForTesting);
            dbService.addTaskState(taskStateInTesting);

            //TaskStateTransitions
            dbService.addTaskStateTransition(taskStateNew, taskStateAssigned);
            dbService.addTaskStateTransition(taskStateOnTheGo, taskStateAssigned);
            dbService.addTaskStateTransition(taskStateAssigned, taskStateOnTheGo);
            dbService.addTaskStateTransition(taskStateOnTheGo, taskStateWaitingForTesting);
            dbService.addTaskStateTransition(taskStateWaitingForTesting, taskStateInTesting);
            dbService.addTaskStateTransition(taskStateInTesting, taskStateOnTheGo);
            dbService.addTaskStateTransition(taskStateInTesting, taskStateClosed);

            //RequestStateTransitions
            dbService.addRequestStateTransition(requestStateNew, requestStateRejected);
            dbService.addRequestStateTransition(requestStateNew, requestStateOnTheGo);
            dbService.addRequestStateTransition(requestStateOnTheGo, requestStateComplete);
            dbService.addRequestStateTransition(requestStateComplete, requestStateOnTheGo);
            dbService.addRequestStateTransition(requestStateOnTheGo, requestStatePostponed);
            dbService.addRequestStateTransition(requestStatePostponed, requestStateOnTheGo);
            dbService.addRequestStateTransition(requestStateNew, requestStatePostponed);
            dbService.addRequestStateTransition(requestStatePostponed, requestStateNew);
            dbService.addRequestStateTransition(requestStatePostponed, requestStateComplete);
            dbService.addRequestStateTransition(requestStateComplete, requestStatePostponed);
            dbService.addRequestStateTransition(requestStateComplete, requestStateClosed);

            //Priorities
            dbService.addPriority("High");
            dbService.addPriority("Medium");
            dbService.addPriority("Low");


        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private static void fillDB(DBService dbService) {
        try {
            //Users
            dbService.addUser("leo", "leo", true, "Evgeny", "Levin", "Olegovich");
            dbService.addUser("belovivan", "belovivan", true, "Ivan", "Belov", "Aleksandrovich");
            dbService.addUser("musk", "musk", true, "Elon", "Musk", null);
            dbService.addUser("realtrump", "realtrump", false, "Donald", "Trump", null);

            //Projects
            long project1Id = dbService.addProject("Clean-Air Cabin", "Medical-grade air quality is delivered through a HEPA filtration system, specifically designed to prevent viruses and bacteria from entering the cabin. <BR>There are three modes: circulate with outside air, re-circulate inside air, and a bioweapon defense mode – which creates positive pressure inside the cabin to protect occupants.", 2);
            long project2Id = dbService.addProject("Tesla autopilot", "All Tesla vehicles produced in our factory, including Model 3, have the hardware needed for full self-driving capability at a safety level substantially greater than that of a human driver.", 4);
            long project3Id = dbService.addProject("World’s First Orbital-Class Rocket Reflight", "SpaceX believes a fully and rapidly reusable rocket is the pivotal breakthrough needed to substantially reduce the cost of space access.  The majority of the launch cost comes from building the rocket, which flies only once. Compare that to a commercial airliner – each new plane costs about the same as Falcon 9, but can fly multiple times per day, and conduct tens of thousands of flights over its lifetime. Following the commercial model, a rapidly reusable space launch vehicle could reduce the cost of traveling to space by a hundredfold. <BR>" +
                    "<BR>" +
                    "While most rockets are designed to burn up on reentry, SpaceX rockets can not only withstand reentry, but can also successfuly land back on Earth and refly again. ", 4);


            //ProjectPositions
            dbService.addProjectPosition(3,  "Customer","realtrump");
            dbService.addProjectPosition(3, "Developer", "leo");
            dbService.addProjectPosition(3, "Team leader", "musk");
            dbService.addProjectPosition(1, "Team leader", "musk");
            dbService.addProjectPosition(1, "Developer", "musk");
            dbService.addProjectPosition(1, "Developer", "belovivan");

            //Requests
            long request1Id = dbService.addRequest("Create something amazing.", "Create something amazing, please.", 2, 5, 1, 3);

            //Tasks
            dbService.addTask("Do it!", "Please", 2, 2, request1Id);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}

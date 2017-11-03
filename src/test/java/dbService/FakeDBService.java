package dbService;

import dbService.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Levin
 */
public class FakeDBService extends DBService {
    private List<PositionEntity> positions = new ArrayList<>();
    private List<PriorityEntity> priorities = new ArrayList<>();
    private List<ProjectPositionEntity> projectPositions = new ArrayList<>();
    private List<ProjectEntity> projects = new ArrayList<>();
    private List<RequestEntity> requests = new ArrayList<>();
    private List<RoleEntity> roles = new ArrayList<>();
    private List<StateEntity> states = new ArrayList<>();
    private List<TaskEntity> tasks = new ArrayList<>();
    private List<UserRoleEntity> userRoles = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();


    public List<PositionEntity> getPositions() {
        return positions;
    }

    public List<PriorityEntity> getPriorities() {
        return priorities;
    }

    public List<ProjectPositionEntity> getProjectPositions() {
        return projectPositions;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public List<RequestEntity> getRequests() {
        return requests;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public List<StateEntity> getStates() {
        return states;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public List<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public FakeDBService() {
        super(null);
    }


    //Users

    @Override
    public long addUser(String login, String password, boolean internal, String firstName, String lastName, String middleName) {
        return addPerson(login, password, internal, firstName, lastName, middleName, "user");
    }

    @Override
    public long addAdmin(String login, String password, String firstName, String lastName, String middleName) {
        return addPerson(login, password, true, firstName, lastName, middleName, "admin");
    }

    private long addPerson(String login, String password, boolean internal, String firstName, String lastName, String middleName, String roleName) {
        UserEntity user = new UserEntity(login, password, internal, firstName, lastName, middleName);
        RoleEntity role = new RoleEntity(roleName);
        UserRoleEntity userRole = new UserRoleEntity(user, role);
        users.add(user);
        roles.add(role);
        userRoles.add(userRole);
        return user.getId();
    }
}

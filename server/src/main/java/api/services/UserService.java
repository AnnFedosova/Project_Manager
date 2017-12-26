package api.services;


import entities.User;
import entities.UserWithPassword;
import dbService.DBException;
import dbService.DBService;
import dbService.entities.UserEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Evgeny Levin
 */
@Path("/api/users")
@Produces({"application/json"})
public class UserService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getUser/{userId}")
    public User getUser(@PathParam("userId") long id) {
        return new User(dbService.getUser(id));
    }

    @GET
    @Path("getUserByLogin/{userLogin}")
    public User getUserByLogin(@PathParam("userLogin") String userLogin) {
        return new User(dbService.getUser(userLogin));
    }

    @GET
    @Path("isAdmin/{userLogin}")
    public boolean isAdmin(@PathParam("userLogin") String userLogin) {
        return dbService.isAdmin(userLogin);
    }


    @GET
    @Path("getUsersByTaskId/{taskId}")
    public List<User> getUsersByTaskId(@PathParam("taskId") long id) {
        List<UserEntity> userEntities = dbService.getUsersByTaskId(id);
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(new User(userEntity));
        }
        return users;
    }

    @GET
    @Path("getUsersByRequestId/{requestId}")
    public List<User> getUsersByRequestId(@PathParam("requestId") long id) {
        List<UserEntity> userEntities = dbService.getUsersByRequestId(id);
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(new User(userEntity));
        }
        return users;
    }

    @GET
    @Path("getAllUsers")
    public List<User> getAllUsers() {
        List<UserEntity> userEntities = dbService.getAllUsers();
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(new User(userEntity));
        }
        return users;
    }

    @POST
    @Path("addUser")
    public Response addRequest(UserWithPassword user) {
        try {
            long userId = dbService.addUser(user.getLogin(), user.getPassword(), user.getInternal(), user.getFirstName(), user.getLastName(), user.getMiddleName());
            String result = "User added with id = " + userId;
            return Response.ok().entity(result).build();
        } catch (DBException e) {
            e.printStackTrace();
            String result = "Error :(";
            return Response.serverError().entity(result).build();
        }
    }

    @POST
    @Path("editUser")
    public Response editTask(UserWithPassword user) {
        UserEntity userEntity = dbService.getUser(user.getId());
        userEntity.setLogin(user.getLogin());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setMiddleName(user.getMiddleName());
        userEntity.setPassword(user.getPassword());
        userEntity.setInternal(user.getInternal());
        dbService.updateUser(userEntity);

        String result = "User updated with id = " + user.getId();
        return Response.ok().entity(result).build();
    }

}

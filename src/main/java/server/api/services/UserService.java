package server.api.services;


import server.apiEntities.User;
import server.dbService.DBService;
import server.dbService.entities.UserEntity;

import javax.ws.rs.*;
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
    @Path("getAllUsers")
    public List<User> getAllUsers() {
        List<UserEntity> userEntities = dbService.getAllUsers();
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(new User(userEntity));
        }
        return users;
    }

}

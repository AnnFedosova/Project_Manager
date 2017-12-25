package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import entities.User;
import entities.UserWithPassword;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class UserAPI {
    private final static String URL = ServerConnection.API_URL + "users/";

    public static boolean isAdmin(String userLogin) throws Exception {
        String json = JSONHelper.getJson(URL + "isAdmin/" + userLogin);
        return new Gson().fromJson(json, new TypeToken<Boolean>(){}.getType());
    }

    public static List<User> getAllUsers() throws Exception {
        String json = JSONHelper.getJson(URL +  "getAllUsers");
        return new Gson().fromJson(json, new TypeToken<List<User>>(){}.getType());
    }

    public static User getUser(String userLogin) throws Exception {
        String json = JSONHelper.getJson(URL + "getUserByLogin/" + userLogin);
        return new Gson().fromJson(json, new TypeToken<User>(){}.getType());
    }

    public static User getUser(long userId) throws Exception {
        String json = JSONHelper.getJson(URL + "getUser/" + userId);
        return new Gson().fromJson(json, new TypeToken<User>(){}.getType());
    }

    public static Response addUser(UserWithPassword user) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "addUser");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(user, MediaType.APPLICATION_JSON));
    }
}

package webapp.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import webapp.ServerConnection;
import webapp.entities.User;

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
        String json = JSONHelper.getJson(URL + "getUser/" + userLogin);
        return new Gson().fromJson(json, new TypeToken<User>(){}.getType());
    }
}

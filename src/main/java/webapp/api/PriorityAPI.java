package webapp.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import webapp.ServerConnection;
import webapp.entities.Priority;

import java.util.List;

public class PriorityAPI {
    private final static String URL = ServerConnection.API_URL + "priorities/";

    public static List<Priority> getAllPriorities() throws Exception {
        String json = JSONHelper.getJson(URL + "getAllPriorities");
        return new Gson().fromJson(json, new TypeToken<List<Priority>>(){}.getType());
    }
}

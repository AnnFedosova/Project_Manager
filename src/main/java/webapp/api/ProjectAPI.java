package webapp.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import webapp.ServerConnection;
import webapp.entities.Project;

import java.util.List;

public class ProjectAPI {
    private final static String URL = ServerConnection.API_URL + "projects/";

    public static List<Project> getProjectsList() throws Exception {
        String json = JSONHelper.getJson(URL + "getProjectsList");
        return new Gson().fromJson(json, new TypeToken<List<Project>>(){}.getType());
    }
}

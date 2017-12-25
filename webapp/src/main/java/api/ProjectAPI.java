package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import entities.Project;
import entities.ProjectPosition;

import java.util.List;

public class ProjectAPI {
    private final static String URL = ServerConnection.API_URL + "projects/";

    public static List<Project> getProjectsList() throws Exception {
        String json = JSONHelper.getJson(URL + "getProjectsList");
        return new Gson().fromJson(json, new TypeToken<List<Project>>(){}.getType());
    }

    public static Project getProject(long projectId) throws Exception {
        String json = JSONHelper.getJson(URL + "getProject/" + projectId);
        return new Gson().fromJson(json, new TypeToken<Project>(){}.getType());
    }

    public static List<ProjectPosition> getProjectPositions(long projectId) throws Exception {
        String json = JSONHelper.getJson(URL + "getProjectPositions/" + projectId);
        return new Gson().fromJson(json, new TypeToken<List<ProjectPosition>>(){}.getType());
    }
}

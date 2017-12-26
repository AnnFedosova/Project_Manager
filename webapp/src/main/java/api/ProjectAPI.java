package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Project;
import entities.ProjectPosition;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    public static Response editProject(Project project) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "editProject");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(project, MediaType.APPLICATION_JSON));
    }
}

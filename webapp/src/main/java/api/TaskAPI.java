package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import entities.State;
import entities.Task;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class TaskAPI {
    private final static String URL = ServerConnection.API_URL + "tasks/";

    public static Response addTask(Task task) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "addTask");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(task, MediaType.APPLICATION_JSON));
    }

    public static List<Task> getTasksList(long requestId) throws Exception {
        String json = JSONHelper.getJson(URL + "getTasksList/" + requestId);
        return new Gson().fromJson(json, new TypeToken<List<Task>>(){}.getType());
    }

    public static Task getTask(long taskId) throws Exception {
        String json = JSONHelper.getJson(URL + "getTask/" + taskId);
        return new Gson().fromJson(json, new TypeToken<Task>(){}.getType());
    }

    public static State getState(long taskId) throws Exception {
        String json = JSONHelper.getJson(URL + "getState/" + taskId);
        return new Gson().fromJson(json, new TypeToken<State>(){}.getType());
    }
}

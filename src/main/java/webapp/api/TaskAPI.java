package webapp.api;

import webapp.ServerConnection;
import webapp.entities.Task;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TaskAPI {
    private final static String URL = ServerConnection.API_URL + "tasks/";

    public static Response addTask(Task task) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "addTask");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(task, MediaType.APPLICATION_JSON));
    }
}

package webapp.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import webapp.ServerConnection;
import webapp.entities.Request;
import webapp.entities.State;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RequestAPI {
    private final static String URL = ServerConnection.API_URL + "requests/";

    public static Response addRequest(Request request) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "addRequest");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
    }

    public static List<Request> getRequestsList(long projectId) throws Exception {
        String json = JSONHelper.getJson(URL + "getRequestsList/" + projectId);
        return new Gson().fromJson(json, new TypeToken<List<Request>>(){}.getType());
    }

    public static Request getRequest(long requestId) throws Exception {
        String json = JSONHelper.getJson(URL + "getRequest/" + requestId);
        return new Gson().fromJson(json, new TypeToken<Request>(){}.getType());
    }

    public static State getState(long requestId) throws Exception {
        String json = JSONHelper.getJson(URL + "getState/" + requestId);
        return new Gson().fromJson(json, new TypeToken<State>(){}.getType());
    }
}

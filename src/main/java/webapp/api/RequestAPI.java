package webapp.api;

import webapp.ServerConnection;
import webapp.entities.Request;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RequestAPI {
    private final static String URL = ServerConnection.API_URL + "requests/";

    public static Response addRequest(Request request) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL + "addRequest");
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
    }
}

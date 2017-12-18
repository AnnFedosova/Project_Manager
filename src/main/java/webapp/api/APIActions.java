package webapp.api;

import webapp.ServerConnection;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class APIActions {
    public static Response doPost(String url, String json) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ServerConnection.API_URL + url);
        Invocation.Builder builder = target.request();
        return builder.post(Entity.entity(json, MediaType.APPLICATION_JSON));
    }

    public static void checkResponseStatus(Response restResponse, HttpServletResponse servletResponse) throws IOException {
        if (restResponse.getStatus() == 200) {
            servletResponse.setStatus(HttpServletResponse.SC_OK);
            servletResponse.getWriter().println("Successfully!");
        }
        else {
            servletResponse.getWriter().println("Not created");
            servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



}

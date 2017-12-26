package api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class JSONHelper {
    public static String getJson(String url) throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
//        Invocation.Builder builder = target.request().header(HttpHeaders.AUTHORIZATION, ServerConnection.TOKEN);
        Invocation.Builder builder = target.request();
        Response response = builder.get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(response.readEntity(String.class).getBytes())));

        String output;
        StringBuilder sb = new StringBuilder();
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }
}

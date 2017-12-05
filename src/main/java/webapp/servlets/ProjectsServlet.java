package webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;
import webapp.ServerConnection;
import webapp.templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Projects", urlPatterns = "/projects")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class ProjectsServlet extends HttpServlet {

    private DBService dbService = DBService.getInstance();

    public ProjectsServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables;
        try {
            pageVariables = createPageVariablesMap(request);

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("projects/projects.html", pageVariables));
        } catch (Exception e) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }


    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        pageVariables.put("projects", getProjectsList());
        return pageVariables;
    }

    private List<ProjectEntity> getProjectsList() throws Exception {
        String json = getProjectsListJson();
        return new Gson().fromJson(json, new TypeToken<List<ProjectEntity>>(){}.getType());
    }

    private String getProjectsListJson() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ServerConnection.API_URL + "projects/getProjectsList");
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

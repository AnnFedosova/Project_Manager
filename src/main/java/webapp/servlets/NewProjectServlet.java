package webapp.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webapp.JSONHelper;
import webapp.ServerConnection;
import webapp.api.UserAPI;
import webapp.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "NewProject", urlPatterns = "/projects/new")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class NewProjectServlet  extends HttpServlet {

    public NewProjectServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = null;
        response.setContentType("text/html;charset=utf-8");
        try {
            pageVariables = createPageVariablesMap(request);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("projects/new/new_project.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        if (title == null || description == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        StringBuffer descriptionSB = new StringBuffer(description);

//        int loc = (new String(descriptionSB)).indexOf('\n');
//        while(loc > 0){
//            descriptionSB.replace(loc, loc+1, "<BR>");
//            loc = (new String(descriptionSB)).indexOf('\n');
//        }


        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ServerConnection.API_URL + "projects/addProject");
        Invocation.Builder builder = target.request();

        String input = null;
        try {
            input = "{" +
                    "\"title\": \"" + title + "\"" +
                    ",\"description\": \"" + description + "\"" +
                    ",\"creatorId\": " + UserAPI.getUser(request.getUserPrincipal().getName()).getId() +
                    "}";
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Response restResponse = builder.post(Entity.entity(input, "application/json"));

        response.setContentType("text/html;charset=utf-8");
        if (restResponse.getStatus() == 200) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Successfully!");
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", isAdmin(user.getName()));
        return pageVariables;
    }

    private boolean isAdmin(String userLogin) throws Exception {
        String json = JSONHelper.getJson(ServerConnection.API_URL + "users/isAdmin/" + userLogin);
        return new Gson().fromJson(json, new TypeToken<Boolean>(){}.getType());
    }


}

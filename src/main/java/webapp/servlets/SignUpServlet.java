package webapp.servlets;

import server.dbService.DBException;
import server.dbService.DBService;
import webapp.api.APIActions;
import webapp.api.UserAPI;
import webapp.entities.UserWithPassword;
import webapp.templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "SignUp", urlPatterns = "/signup")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin"}))
public class SignUpServlet extends HttpServlet {

    public SignUpServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("signup/signup.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String middleName = request.getParameter("middle_name");
        String internal = request.getParameter("internal");



        if (login == null || password == null || firstName == null || lastName == null) {
            response.getWriter().println("Not registered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Response restResponse = UserAPI.addUser(new UserWithPassword(login, firstName, lastName, middleName, Boolean.parseBoolean(internal), password));
            APIActions.checkResponseStatus(restResponse, response);
        }
        catch (Exception e) {
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("isAdmin", UserAPI.isAdmin(request.getUserPrincipal().getName()));
        return pageVariables;
    }
}

package servlets;

import api.APIActions;
import api.UserAPI;
import entities.UserWithPassword;
import templater.PageGenerator;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "EditUser", urlPatterns = "/user/edit")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin"}))
public class EditUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, Long.parseLong(id));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("user/edit/edit_user.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType("text/html;charset=utf-8");
        String userId = httpRequest.getParameter("id");
        String login = httpRequest.getParameter("login");
        String password = httpRequest.getParameter("password");
        String firstName = httpRequest.getParameter("firstName");
        String lastName = httpRequest.getParameter("lastName");
        String middleName = httpRequest.getParameter("middleName");
        String internal = httpRequest.getParameter("internal");

        if (userId == null || login == null || password == null || firstName == null || lastName == null) {
            httpResponse.getWriter().println("Not registered");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            UserWithPassword user = new UserWithPassword(UserAPI.getUser(Long.parseLong(userId)));
            user.setLogin(login);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMiddleName(middleName);
            user.setInternal(Boolean.parseBoolean(internal));
            Response response = UserAPI.editUser(user);
            APIActions.checkResponseStatus(response, httpResponse);
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest httpRequest, long userId) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("isAdmin", UserAPI.isAdmin(httpRequest.getUserPrincipal().getName()));
        pageVariables.put("user", UserAPI.getUser(userId));
        return pageVariables;
    }
}

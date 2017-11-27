package webapp.servlets;

import server.dbService.DBService;
import server.dbService.entities.UserEntity;
import webapp.templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "User", urlPatterns = "/user")
public class UserServlet extends HttpServlet{
    private DBService dbService = DBService.getInstance();

    public UserServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("user/user.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        UserEntity user = dbService.getUser(id);

        Principal userPrincipal = request.getUserPrincipal();
        pageVariables.put("isAdmin", dbService.isAdmin(userPrincipal.getName()));
        pageVariables.put("id", user.getId());
        pageVariables.put("firstName", user.getFirstName());
        pageVariables.put("lastName", user.getLastName());
        pageVariables.put("internal", Boolean.toString(user.getInternal()));
        pageVariables.put("login", user.getLogin());

        return pageVariables;
    }
}

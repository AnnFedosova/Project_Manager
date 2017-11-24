package webapp.servlets;

import server.dbService.DBException;
import server.dbService.DBService;
import webapp.templater.PageGenerator;
import javax.servlet.ServletException;
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
public class SignUpServlet extends HttpServlet {
    public static final String PAGE_URL = "/signup";
    private DBService dbService;

    public SignUpServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("signup/signup.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String middleName = request.getParameter("middle_name");
        String internal = request.getParameter("internal");



        if (login == null || password == null || firstName == null || lastName == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not registered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            dbService.addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);
        }
        catch (DBException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not registered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("Registered");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        Principal user = request.getUserPrincipal();

        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        return pageVariables;
    }
}

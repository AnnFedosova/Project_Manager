package servlets;

import dbService.DBException;
import dbService.DBService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/signup/signup.html", null));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String middleName = request.getParameter("middle_name");
        String internal = request.getParameter("internal");

        if (login == null || password == null || firstName == null || lastName == null || internal == null) {
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
}

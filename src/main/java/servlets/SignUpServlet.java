package servlets;

import dbService.DBException;
import dbService.DBService;

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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String patronymic = request.getParameter("patronymic");

        if (login == null || password == null || firstName == null || lastName == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not registered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            if (patronymic == null) {
                dbService.addNewUser(login, password, firstName, lastName);
            }
            else {
                dbService.addNewUser(login, password, firstName, lastName, patronymic);
            }
        }
        catch (DBException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not registered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //accountService.addNewUser(new UserProfile(login));

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("Registered");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Logout", urlPatterns = "/logout")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}

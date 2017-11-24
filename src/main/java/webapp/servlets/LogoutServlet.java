package webapp.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Evgeny Levin
 */
public class LogoutServlet extends HttpServlet {
    public static final String PAGE_URL = "/logout";

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

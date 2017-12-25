package servlets;

import api.UserAPI;
import templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Admin", urlPatterns = "/admin")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin"}))
public class AdminServlet extends HttpServlet {

    public AdminServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("admin/admin.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("isAdmin", UserAPI.isAdmin(request.getUserPrincipal().getName()));
        return pageVariables;
    }
}

package webapp.servlets;

import webapp.api.ProjectAPI;
import webapp.api.RequestAPI;
import webapp.api.UserAPI;
import webapp.entities.Project;
import webapp.entities.ProjectPosition;
import webapp.entities.Request;
import webapp.templater.PageGenerator;

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
import java.util.List;
import java.util.Map;


/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Project", urlPatterns = "/project")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class ProjectServlet extends HttpServlet {
    public ProjectServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, Long.parseLong(id));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("project/project.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Project project = ProjectAPI.getProject(id);
        List<ProjectPosition> positions = ProjectAPI.getProjectPositions(id);
        List<Request> requests = RequestAPI.getRequestsList(id);
        Principal user = request.getUserPrincipal();

        pageVariables.put("isAdmin", UserAPI.isAdmin(user.getName()));
        pageVariables.put("project", project);
        pageVariables.put("creator", UserAPI.getUser(project.getCreatorId()));
        pageVariables.put("positions", positions);
        pageVariables.put("requests", requests);

        return pageVariables;
    }
}

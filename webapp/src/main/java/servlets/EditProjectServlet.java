package servlets;

import api.APIActions;
import api.ProjectAPI;
import api.UserAPI;
import entities.Project;
import templater.PageGenerator;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "EditProject", urlPatterns = "/project/edit")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class EditProjectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("project/edit/edit_project.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType("text/html;charset=utf-8");
        String projectId = httpRequest.getParameter("id");
        String title = httpRequest.getParameter("title");
        String description = httpRequest.getParameter("description");

        if (title == null || description == null || projectId == null) {
            httpResponse.getWriter().println("Not created");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Project project = ProjectAPI.getProject(Long.parseLong(projectId));
            project.setTitle(title);
            project.setDescription(description);
            Response response = ProjectAPI.editProject(project);
            APIActions.checkResponseStatus(response, httpResponse);
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest httpRequest, String projectId) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Project project = ProjectAPI.getProject(Long.parseLong(projectId));

        pageVariables.put("isAdmin", UserAPI.isAdmin(httpRequest.getUserPrincipal().getName()));
        pageVariables.put("project", project);
        return pageVariables;
    }

}

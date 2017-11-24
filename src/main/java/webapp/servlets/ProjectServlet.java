package webapp.servlets;

import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;
import server.dbService.entities.ProjectPositionEntity;
import server.dbService.entities.RequestEntity;
import webapp.templater.PageGenerator;

import javax.servlet.ServletException;
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
public class ProjectServlet extends HttpServlet {
    public static final String PAGE_URL = "/project";
    private DBService dbService;

    public ProjectServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("project/project.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        ProjectEntity project = dbService.getProject(id);
        List<ProjectPositionEntity> positions= dbService.getProjectPositionsList(project.getId());
        List<RequestEntity> requests = dbService.getRequestssList(project.getId());

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        pageVariables.put("id", project.getId());
        pageVariables.put("title", project.getTitle());
        pageVariables.put("description", project.getDescription());
        pageVariables.put("creator", project.getCreator());
        pageVariables.put("positions", positions);
        pageVariables.put("requests", requests);

        return pageVariables;
    }
}

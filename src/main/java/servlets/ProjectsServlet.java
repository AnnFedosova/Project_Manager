package servlets;

import dbService.DBService;
import dbService.dataSets.ProjectDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
public class ProjectsServlet extends HttpServlet {
    public static final String PAGE_URL = "/projects";
    private DBService dbService;

    public ProjectsServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createProjectsLisPageVariablesMap(request);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/projects/projects.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createProjectPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/projects/project.html", pageVariables));
    }

    private Map<String, Object> createProjectsLisPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("projects", this.dbService.getProjectsList());
        return pageVariables;
    }

    private Map<String, Object> createProjectPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        ProjectDataSet project = dbService.getProject(id);
        pageVariables.put("title", project.getTitle());
        pageVariables.put("description", project.getDescription());
        pageVariables.put("creator", project.getCreator());
        return pageVariables;
    }
}

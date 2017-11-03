package servlets;

import dbService.DBService;
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
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/projects/projects.html", pageVariables));
    }


    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("projects", this.dbService.getProjectsList());
        return pageVariables;
    }
}

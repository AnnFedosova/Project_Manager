package servlets;

import dbService.DBService;
import dbService.entities.RequestEntity;
import dbService.entities.TaskEntity;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
public class TaskServlet extends HttpServlet {
    public static final String PAGE_URL = "/task";
    private DBService dbService;

    public TaskServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/task/task.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        TaskEntity task = dbService.getTask(id);

        pageVariables.put("id", task.getId());
        pageVariables.put("title", task.getTitle());
        pageVariables.put("description", task.getDescription());
        pageVariables.put("creator", task.getCreator());
        pageVariables.put("executor", task.getExecutor());
        pageVariables.put("state", task.getState().getName());

        return pageVariables;
    }
}

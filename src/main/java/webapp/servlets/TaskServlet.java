package webapp.servlets;

import server.dbService.DBService;
import server.dbService.entities.TaskEntity;
import webapp.api.TaskAPI;
import webapp.api.UserAPI;
import webapp.entities.Task;
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
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Task", urlPatterns = "/task")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class TaskServlet extends HttpServlet {

    public TaskServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, Long.parseLong(id));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("task/task.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long taskId) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Task task = TaskAPI.getTask(taskId);

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", UserAPI.isAdmin(user.getName()));
        pageVariables.put("task", task);
        pageVariables.put("creator", UserAPI.getUser(task.getCreatorId()));
        pageVariables.put("executor", UserAPI.getUser(task.getExecutorId()));
        pageVariables.put("state", TaskAPI.getState(task.getId()));

        return pageVariables;
    }
}

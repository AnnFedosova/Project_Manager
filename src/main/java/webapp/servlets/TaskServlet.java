package webapp.servlets;

import server.dbService.DBService;
import server.dbService.entities.TaskEntity;
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
    private DBService dbService = DBService.getInstance();

    public TaskServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("task/task.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        TaskEntity task = dbService.getTask(id);

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        pageVariables.put("id", task.getId());
        pageVariables.put("title", task.getTitle());
        pageVariables.put("description", task.getDescription());
        pageVariables.put("creator", task.getCreator());
        pageVariables.put("executor", task.getExecutor());
        pageVariables.put("state", task.getState().getName());

        return pageVariables;
    }
}

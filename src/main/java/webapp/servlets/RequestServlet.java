package webapp.servlets;


import server.dbService.DBService;
import server.dbService.entities.RequestEntity;
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
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Request", urlPatterns = "/request")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class RequestServlet extends HttpServlet {
    private DBService dbService = DBService.getInstance();

    public RequestServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("request/request.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        RequestEntity requestEntity = dbService.getRequest(id);
        List<TaskEntity> tasks = dbService.getTasksList(requestEntity.getId());

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        pageVariables.put("id", requestEntity.getId());
        pageVariables.put("title", requestEntity.getTitle());
        pageVariables.put("description", requestEntity.getDescription());
        pageVariables.put("creator", requestEntity.getCreator());
        pageVariables.put("customer", requestEntity.getCustomer());
        pageVariables.put("priority", requestEntity.getPriority().getName());
        pageVariables.put("state", requestEntity.getState().getName());
        pageVariables.put("tasks", tasks);

        return pageVariables;
    }
}

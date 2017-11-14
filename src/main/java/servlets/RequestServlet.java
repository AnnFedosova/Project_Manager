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
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
public class RequestServlet extends HttpServlet {
    public static final String PAGE_URL = "/request";
    private DBService dbService;

    public RequestServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.parseLong(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/request/request.html", pageVariables));
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

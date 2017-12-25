package webapp.servlets;


import server.dbService.DBService;
import server.dbService.entities.RequestEntity;
import server.dbService.entities.TaskEntity;
import webapp.api.PriorityAPI;
import webapp.api.RequestAPI;
import webapp.api.TaskAPI;
import webapp.api.UserAPI;
import webapp.entities.Request;
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
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "Request", urlPatterns = "/request")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class RequestServlet extends HttpServlet {

    public RequestServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        String id = request.getParameter("id");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, Long.parseLong(id));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("request/request.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long requestId) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Request requestEntity = RequestAPI.getRequest(requestId);

        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", UserAPI.isAdmin(user.getName()));
        pageVariables.put("request", requestEntity);
        pageVariables.put("creator", UserAPI.getUser(requestEntity.getCreatorId()));
        pageVariables.put("customer", UserAPI.getUser(requestEntity.getCustomerId()));
        pageVariables.put("priority", PriorityAPI.getPriority(requestEntity.getPriorityId()));
        pageVariables.put("state", RequestAPI.getState(requestId));
        pageVariables.put("tasks", TaskAPI.getTasksList(requestId));

        return pageVariables;
    }
}

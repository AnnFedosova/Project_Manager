package webapp.servlets;

import server.dbService.DBException;
import server.dbService.DBService;
import webapp.api.APIActions;
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
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "NewTask", urlPatterns = "/task/new")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class NewTaskServlet extends HttpServlet {

    public NewTaskServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request);
            String requestId = request.getParameter("requestid");
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            pageVariables.put("requestid", requestId);
            response.getWriter().println(PageGenerator.instance().getPage("task/new/new_task.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String requestId = request.getParameter("requestid");
        String executorId = request.getParameter("executors");


        if (title == null || description == null || requestId == null || executorId == null) {
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
//        StringBuffer descriptionSB = new StringBuffer(description);
//
//        int loc = (new String(descriptionSB)).indexOf('\n');
//        while (loc > 0) {
//            descriptionSB.replace(loc, loc + 1, "<BR>");
//            loc = (new String(descriptionSB)).indexOf('\n');
//        }


        try {
            long creatorId = UserAPI.getUser(request.getUserPrincipal().getName()).getId();
            Response restResponse = TaskAPI.addTask(new Task(title, description, Long.parseLong(requestId), 1, creatorId, Long.parseLong(executorId)));
            APIActions.checkResponseStatus(restResponse, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", UserAPI.isAdmin(user.getName()));
        pageVariables.put("users", UserAPI.getAllUsers());
        return pageVariables;
    }
}

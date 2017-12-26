package servlets;

import api.TaskAPI;
import api.UserAPI;
import api.ServerConnection;
import entities.Task;
import entities.User;
import templater.PageGenerator;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "EditTask", urlPatterns = "/task/edit")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class EditTaskServlet extends HttpServlet {

    public EditTaskServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("task/edit/edit_task.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String executorId = request.getParameter("executors");
        String taskId = request.getParameter("id");
        String stateId = request.getParameter("states");


        if (title == null || description == null || taskId == null || executorId == null) {
            response.setContentType("text/html;charset=utf-8");
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


        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ServerConnection.API_URL + "tasks/editTask");
        Invocation.Builder builder = target.request();
        String input = "{" +
                "\"id\": " + taskId +
                ",\"title\": \"" + title + "\"" +
                ",\"description\": \"" + description + "\"" +
                ",\"stateId\": \"" + stateId + "\"" +
                ",\"executorId\": " + executorId +
                "}";
        Response restResponse = builder.post(Entity.entity(input, "application/json"));

        response.setContentType("text/html;charset=utf-8");
        if (restResponse.getStatus() == 200) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Successfully!");
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, String id) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Task task = TaskAPI.getTask(Long.parseLong(id));
        List<User> users = UserAPI.getUsersByTaskId(Long.parseLong(id));
        for (User userEntity : users) {
            if (userEntity.getId() == task.getExecutorId()) {
                users.remove(userEntity);
                break;
            }
        }

        pageVariables.put("isAdmin", UserAPI.isAdmin(request.getUserPrincipal().getName()));
        pageVariables.put("task", task);
        pageVariables.put("states", TaskAPI.getStates(task.getId()));
        pageVariables.put("executors", users);
        pageVariables.put("currentExecutor", UserAPI.getUser(task.getExecutorId()));
        pageVariables.put("currentState", TaskAPI.getState(task.getId()));
        return pageVariables;
    }

}

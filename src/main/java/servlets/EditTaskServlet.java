package servlets;

import dbService.DBService;
import dbService.entities.TaskEntity;
import dbService.entities.UserEntity;
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
public class EditTaskServlet extends HttpServlet {
    public static final String PAGE_URL = "/task/edit";
    private DBService dbService;

    public EditTaskServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Map<String, Object> pageVariables = createPageVariablesMap(request, Long.valueOf(id));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/task/edit/edit_task.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String executorId = request.getParameter("executors");
        String taskId = request.getParameter("id");


        if (title == null || description == null || taskId == null || executorId == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        StringBuffer descriptionSB = new StringBuffer(description);

        int loc = (new String(descriptionSB)).indexOf('\n');
        while (loc > 0) {
            descriptionSB.replace(loc, loc + 1, "<BR>");
            loc = (new String(descriptionSB)).indexOf('\n');
        }


        TaskEntity task = dbService.getTask(Long.parseLong(taskId));
        task.setTitle(title);
        task.setDescription(description);
        task.setExecutor(dbService.getUser(Long.parseLong(executorId)));
        dbService.updateTask(task);


        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Successfully!");
    }



    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, long id) {
        Map<String, Object> pageVariables = new HashMap<>();
        Principal user = request.getUserPrincipal();
        TaskEntity task = dbService.getTask(id);
        List<UserEntity> users = dbService.getUsersByTaskId(id);
        for (UserEntity userEntity : users) {
            if (userEntity.getId() == task.getExecutor().getId()) {
                users.remove(userEntity);
                break;
            }
        }

        pageVariables.put("isAdmin", dbService.isAdmin(user.getName()));
        pageVariables.put("id", id);
        pageVariables.put("states", this.dbService.getStateTransitions(task.getState(), DBService.RequestOrTask.TASK));
        pageVariables.put("title", task.getTitle());
        pageVariables.put("description", task.getDescription());
        pageVariables.put("executors", users);
        pageVariables.put("currentExecutor", task.getExecutor());
        return pageVariables;
    }

}
package servlets;

import dbService.DBException;
import dbService.DBService;
import templater.PageGenerator;

import javax.servlet.ServletException;
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
public class NewTaskServlet extends HttpServlet {
    public static final String PAGE_URL = "/task/new";
    private DBService dbService;

    public NewTaskServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        String requestId = request.getParameter("requestid");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        pageVariables.put("requestid", requestId);
        response.getWriter().println(PageGenerator.instance().getPage("html/task/new/new_task.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String requestId = request.getParameter("requestid");
        String executorId = request.getParameter("executors");


        if (title == null || description == null || requestId == null || executorId == null) {
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


        try {
            Principal user = request.getUserPrincipal();
            dbService.addTask(title, descriptionSB.toString(), user.getName(), Long.parseLong(executorId), Long.parseLong(requestId));
        } catch (DBException e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Successfully!");
    }



    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("users", this.dbService.getAllUsers());
        return pageVariables;
    }
}

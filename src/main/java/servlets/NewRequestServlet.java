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
public class NewRequestServlet extends HttpServlet {
    public static final String PAGE_URL = "/request/new";
    private DBService dbService;

    public NewRequestServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        String projectId = request.getParameter("projectid");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        pageVariables.put("projectid", projectId);
        response.getWriter().println(PageGenerator.instance().getPage("html/request/new/new_request.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String projectId = request.getParameter("projectid");
        String customerId = request.getParameter("customers");
        String priorityId = request.getParameter("priorities");


        if (title == null || description == null || projectId == null || customerId == null || priorityId == null) {
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
            dbService.addRequest(title, descriptionSB.toString(), user.getName(), Long.parseLong(customerId), Long.parseLong(priorityId), Long.parseLong(projectId));
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
        pageVariables.put("priorities", this.dbService.getAllPriorities());
        return pageVariables;
    }
}

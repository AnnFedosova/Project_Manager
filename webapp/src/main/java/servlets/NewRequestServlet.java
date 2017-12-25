package servlets;

import api.APIActions;
import api.PriorityAPI;
import api.RequestAPI;
import api.UserAPI;
import entities.Request;
import templater.PageGenerator;

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
@WebServlet(name = "NewRequest", urlPatterns = "/request/new")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class NewRequestServlet extends HttpServlet {

    public NewRequestServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request);
            String projectId = request.getParameter("projectid");
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            pageVariables.put("projectid", projectId);
            response.getWriter().println(PageGenerator.instance().getPage("request/new/new_request.html", pageVariables));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String projectId = request.getParameter("projectid");
        String customerId = request.getParameter("customers");
        String priorityId = request.getParameter("priorities");

        response.setContentType("text/html;charset=utf-8");


        if (title == null || description == null || projectId == null || customerId == null || priorityId == null) {
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
            Response restResponse = RequestAPI.addRequest(new Request(title, description, Long.parseLong(projectId), creatorId, Long.parseLong(customerId), 1, Long.parseLong(priorityId)));
            APIActions.checkResponseStatus(restResponse, response);
        } catch (Exception e) {
            response.getWriter().println("Not created");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Principal user = request.getUserPrincipal();
        pageVariables.put("isAdmin", UserAPI.isAdmin(user.getName()));
        pageVariables.put("users", UserAPI.getAllUsers());
        pageVariables.put("priorities", PriorityAPI.getAllPriorities());
        return pageVariables;
    }


}

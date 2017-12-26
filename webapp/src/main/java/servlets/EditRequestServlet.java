package servlets;

import api.APIActions;
import api.RequestAPI;
import api.ServerConnection;
import api.UserAPI;
import entities.Priority;
import entities.Request;
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
@WebServlet(name = "EditRequest", urlPatterns = "/request/edit")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin", "user"}))
public class EditRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        Map<String, Object> pageVariables = null;
        try {
            pageVariables = createPageVariablesMap(request, id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.instance().getPage("request/edit/edit_request.html", pageVariables));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType("text/html;charset=utf-8");
        String requestId = httpRequest.getParameter("id");
        String title = httpRequest.getParameter("title");
        String description = httpRequest.getParameter("description");
        String customerId = httpRequest.getParameter("customers");
        String stateId = httpRequest.getParameter("states");
        String priorityId = httpRequest.getParameter("priorities");

        if (title == null || description == null || requestId == null || customerId == null || stateId == null || priorityId == null) {
            httpResponse.getWriter().println("Not created");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Request request = RequestAPI.getRequest(Long.parseLong(requestId));
            request.setTitle(title);
            request.setDescription(description);
            request.setCustomerId(Long.parseLong(customerId));
            request.setStateId(Long.parseLong(stateId));
            request.setPriorityId(Long.parseLong(priorityId));
            Response response = RequestAPI.editRequest(request);
            APIActions.checkResponseStatus(response, httpResponse);
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().println("Error!  " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest httpRequest, String requestId) throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        Request request = RequestAPI.getRequest(Long.parseLong(requestId));
        List<User> users = UserAPI.getUsersByRequestId(Long.parseLong(requestId));
        for (User userEntity : users) {
            if (userEntity.getId() == request.getCustomerId()) {
                users.remove(userEntity);
                break;
            }
        }
        List<Priority> priorities = RequestAPI.getAllPriorities();
        for (Priority priority : priorities) {
            if (priority.getId() == request.getPriorityId()) {
                priorities.remove(priority);
                break;
            }
        }

        pageVariables.put("isAdmin", UserAPI.isAdmin(httpRequest.getUserPrincipal().getName()));
        pageVariables.put("request", request);
        pageVariables.put("states", RequestAPI.getStates(request.getId()));
        pageVariables.put("customers", users);
        pageVariables.put("currentCustomer", UserAPI.getUser(request.getCustomerId()));
        pageVariables.put("currentState", RequestAPI.getState(request.getId()));
        pageVariables.put("currentPriority", RequestAPI.getPriority(request.getId()));
        pageVariables.put("priorities", priorities);
        return pageVariables;
    }
}

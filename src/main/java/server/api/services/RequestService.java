package server.api.services;

import server.apiEntities.Request;
import server.apiEntities.State;
import server.dbService.DBException;
import server.dbService.DBService;
import server.dbService.entities.RequestEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/api/requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestService {
    private DBService dbService = DBService.getInstance();

    @POST
    @Path("addRequest")
    public Response addRequest(Request request) {
        try {
            long requestId = dbService.addRequest(request.getTitle(), request.getDescription(), request.getCreatorId(), request.getCustomerId(), request.getPriorityId(), request.getProjectId());
            String result = "Request added with id = " + requestId;
            return Response.ok().entity(result).build();
        } catch (DBException e) {
            e.printStackTrace();
            String result = "Error :(";
            return Response.serverError().entity(result).build();
        }
    }

    @GET
    @Path("getRequestsList/{projectId}")
    public List<Request> getRequestsList(@PathParam("projectId") long projectId) {
        List<Request> requests = new LinkedList<>();
        List<RequestEntity> requestEntities = dbService.getRequestsList(projectId);
        for (RequestEntity requestEntity : requestEntities) {
            requests.add(new Request(requestEntity));
        }
        return requests;
    }

    @GET
    @Path("getRequest/{requestId}")
    public Request getRequest(@PathParam("requestId") long requestId) {
        return new Request(dbService.getRequest(requestId));
    }

    @GET
    @Path("getState/{stateId}")
    public State getState(@PathParam("stateId") long stateId) {
        return new State(dbService.getTaskState(stateId));
    }


}

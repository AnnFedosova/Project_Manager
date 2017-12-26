package api.services;

import apiEntities.Request;
import apiEntities.State;
import dbService.DBException;
import dbService.DBService;
import dbService.entities.RequestEntity;
import dbService.entities.RequestStateTransitionEntity;

import javax.ws.rs.*;
import javax.ws.rs.client.*;
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

    @POST
    @Path("editRequest")
    public Response editRequest(Request request) {
        RequestEntity requestEntity = dbService.getRequest(request.getId());
        requestEntity.setTitle(request.getTitle());
        requestEntity.setDescription(request.getDescription());
        requestEntity.setCreator(dbService.getUser(request.getCreatorId()));
        requestEntity.setCustomer(dbService.getUser(request.getCustomerId()));
        requestEntity.setPriority(dbService.getPriority(request.getPriorityId()));
        requestEntity.setProject(dbService.getProject(request.getProjectId()));
        requestEntity.setState(dbService.getRequestState(request.getStateId()));

        dbService.updateRequest(requestEntity);

        String result = "Request updated with id = " + request.getId();
        return Response.ok().entity(result).build();
    }
    @GET
    @Path("getStates/{id}")
    public List<State> getStates(@PathParam("id") long id) {
        RequestEntity request = dbService.getRequest(id);
        List<State> states = new LinkedList<>();
        List<RequestStateTransitionEntity> requestStateTransitionEntities =  dbService.getRequestStateTransitions(request.getState());

        for (RequestStateTransitionEntity element : requestStateTransitionEntities) {
            states.add(new State(element.getToState()));
        }

        return states;
    }



}

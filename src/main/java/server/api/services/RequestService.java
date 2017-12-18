package server.api.services;

import server.apiEntities.Request;
import server.dbService.DBException;
import server.dbService.DBService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
}

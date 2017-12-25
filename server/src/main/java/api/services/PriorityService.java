package api.services;

import apiEntities.Priority;
import dbService.DBService;
import dbService.entities.PriorityEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/api/priorities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PriorityService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getAllPriorities")
    public List<Priority> getAllPriorities() {
        List<Priority> projects = new LinkedList<>();
        List<PriorityEntity> projectEntities = dbService.getAllPriorities();
        for (PriorityEntity projectEntity : projectEntities) {
            projects.add(new Priority(projectEntity));
        }
        return projects;
    }

    @GET
    @Path("getPriority/{priorityId}")
    public Priority getPriority(@PathParam("priorityId") long priorityId) {
        return new Priority(dbService.getPriority(priorityId));
    }
}

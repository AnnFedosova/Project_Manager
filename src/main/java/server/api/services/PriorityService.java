package server.api.services;

import server.apiEntities.Priority;
import server.dbService.DBService;
import server.dbService.entities.PriorityEntity;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}

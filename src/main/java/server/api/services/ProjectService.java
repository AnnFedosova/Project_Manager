package server.api.services;

import server.api.jwt.Secured;
import server.apiEntities.Project;
import server.dbService.DBException;
import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/api/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getProjectsList")
    //@Secured
    public List<Project> getProjectsList() {
        List<Project> projects = new LinkedList<>();
        List<ProjectEntity> projectEntities = dbService.getProjectsList();
        for (ProjectEntity projectEntity : projectEntities) {
            projects.add(new Project(projectEntity));
        }
        return projects;
    }

    @POST
    @Path("addProject")
    public Response addProject(Project project) {
        try {
            long projectId = dbService.addProject(project.getTitle(), project.getDescription(), project.getCreatorId());
            String result = "Project added with id = " + projectId;
            return Response.ok().entity(result).build();
        } catch (DBException e) {
            e.printStackTrace();
            String result = "Error :(";
            return Response.serverError().entity(result).build();
        }

    }
}

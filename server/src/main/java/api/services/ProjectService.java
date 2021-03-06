package api.services;

import entities.Project;
import entities.ProjectPosition;
import dbService.DBException;
import dbService.DBService;
import dbService.entities.ProjectEntity;
import dbService.entities.ProjectPositionEntity;

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

    @POST
    @Path("editProject")
    public Response editRequest(Project project) {
        ProjectEntity projectEntity = dbService.getProject(project.getId());
        projectEntity.setTitle(project.getTitle());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setCreator(dbService.getUser(project.getCreatorId()));

        dbService.updateProject(projectEntity);

        String result = "Project updated with id = " + project.getId();
        return Response.ok().entity(result).build();
    }

    @GET
    @Path("getProject/{projectId}")
    public Project getProject(@PathParam("projectId") long projectId) {
        ProjectEntity project = dbService.getProject(projectId);
        return new Project(project);
    }

    @GET
    @Path("getProjectPositions/{projectId}")
    public List<ProjectPosition> getProjectPositions(@PathParam("projectId") long projectId) {
        List<ProjectPositionEntity> positionsDB = dbService.getProjectPositionsList(projectId);
        List<ProjectPosition> positions = new LinkedList<>();
        for(ProjectPositionEntity projectPositionEntity : positionsDB) {
            positions.add(new ProjectPosition(projectPositionEntity));
        }
        return positions;
    }
}

package server.api;

import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;
import server.dbService.entities.UserEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/projects")
public class ProjectService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getProjectsList")
    @Produces({"application/json"})
    public Response getProjectsList() {
        List<ProjectEntity> projects = dbService.getProjectsList();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (ProjectEntity project : projects) {
            UserEntity creator = project.getCreator();
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("id", project.getId())
                    .add("title", project.getTitle())
                    .add("description", project.getDescription())
                    .add("creator", Json.createObjectBuilder()
                            .add("id", creator.getId())
                            .add("first_name", creator.getFirstName())
                            .add("last_name", creator.getLastName())
                            .add("middle_name", creator.getMiddleName())
                            .add("login", creator.getLogin())
                            .add("internal", creator.getInternal())
                    )
            );
        }

        return Response.ok(jsonArrayBuilder.build()).build();
    }
}

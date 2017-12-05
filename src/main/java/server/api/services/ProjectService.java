package server.api.services;

import server.api.jwt.Secured;
import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/api/projects")
@Produces({"application/json"})
public class ProjectService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getProjectsList")
    //@Secured
    public List<ProjectEntity> getProjectsList() {
        return dbService.getProjectsList();
    }

//    @GET
//    @Path("getProjectsList")
//    public Response getProjectsList() {
//        List<ProjectEntity> projects = dbService.getProjectsList();
//        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
//
//        for (ProjectEntity project : projects) {
//            UserEntity creator = project.getCreator();
//            jsonArrayBuilder.add(Json.createObjectBuilder()
//                    .add("id", project.getId())
//                    .add("title", project.getTitle())
//                    .add("description", project.getDescription())
//                    .add("creator", Json.createObjectBuilder()
//                            .add("id", creator.getId())
////                            .add("firstName", creator.getFirstName())
////                            .add("lastName", creator.getLastName())
////                            .add("middleName", creator.getMiddleName() == null ? "" : creator.getMiddleName())
////                            .add("login", creator.getLogin())
////                            .add("internal", creator.getInternal())
//                    )
//            );
//        }
//
//        return Response.ok(jsonArrayBuilder.build()).build();
//    }
}

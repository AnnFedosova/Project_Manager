package server.api;

import org.jboss.arquillian.core.api.annotation.Inject;
import server.dbService.DBService;
import server.dbService.entities.ProjectEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;


public class ProjectService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("/projects")
    @Produces({"application/json"})
    public List<ProjectEntity>  getProjectsList() {
        return dbService.getProjectsList();
    }
}

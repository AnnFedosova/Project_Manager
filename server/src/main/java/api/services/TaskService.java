package api.services;

import entities.State;
import entities.Task;
import entities.StateTransition;
import dbService.DBException;
import dbService.DBService;
import dbService.entities.TaskEntity;
import dbService.entities.TaskStateTransitionEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Evgeny Levin
 */
@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskService {
    private DBService dbService = DBService.getInstance();

    @GET
    @Path("getTask/{id}")
    public Task getTask(@PathParam("id") long id) {
        return new Task(dbService.getTask(id));
    }

    @GET
    @Path("getStateTransitions/{id}")
    public List<StateTransition> getStateTransitions(@PathParam("id") long id) {
        TaskEntity task = dbService.getTask(id);
        List<StateTransition> stateTransitions = new LinkedList<>();
        List<TaskStateTransitionEntity> list =  dbService.getTaskStateTransitions(task.getState());

        for (TaskStateTransitionEntity tste : list) {
            stateTransitions.add(new StateTransition(tste));
        }

        return stateTransitions;
    }

    @GET
    @Path("getState/{taskId}")
    public State getState(@PathParam("taskId") long taskId) {
        return new State(dbService.getTask(taskId).getState());
    }

    @GET
    @Path("getStates/{taskId}")
    public List<State> getStates(@PathParam("taskId") long taskId) {
        TaskEntity task = dbService.getTask(taskId);
        List<State> states = new LinkedList<>();
        List<TaskStateTransitionEntity> taskStateTransitionEntities =  dbService.getTaskStateTransitions(task.getState());

        for (TaskStateTransitionEntity element : taskStateTransitionEntities) {
            states.add(new State(element.getToState()));
        }

        return states;
    }

    @POST
    @Path("editTask")
    public Response editTask(Task task) {
        TaskEntity taskEntity = dbService.getTask(task.getId());
        taskEntity.setTitle(task.getTitle());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setExecutor(dbService.getUser(task.getExecutorId()));
        taskEntity.setState(dbService.getTaskState(task.getStateId()));
        dbService.updateTask(taskEntity);

        String result = "Task updated with id = " + task.getId();
        return Response.ok().entity(result).build();
    }

    @POST
    @Path("addTask")
    public Response addTask(Task task) {
        try {
            long taskId = dbService.addTask(task.getTitle(), task.getDescription(), task.getCreatorId(), task.getExecutorId(), task.getRequestId());
            String result = "Task added with id = " + taskId;
            return Response.ok().entity(result).build();
        } catch (DBException e) {
            e.printStackTrace();
            String result = "Error :(";
            return Response.serverError().entity(result).build();
        }
    }

    @GET
    @Path("getTasksList/{requestId}")
    public List<Task> getTasksList(@PathParam("requestId") long requestId) {
        List<Task> tasks = new LinkedList<>();
        List<TaskEntity> taskEntities = dbService.getTasksList(requestId);
        for (TaskEntity taskEntity : taskEntities) {
            tasks.add(new Task(taskEntity));
        }
        return tasks;
    }
}

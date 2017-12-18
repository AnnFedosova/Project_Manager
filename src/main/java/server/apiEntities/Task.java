package server.apiEntities;

import server.dbService.entities.TaskEntity;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    private long id;

    private long requestId;

    private String title;

    private String description;

    private long stateId;

    private long creatorId;

    private long executorId;

    public Task() {
    }

    public Task(TaskEntity taskEntity) {
        this.id = taskEntity.getId();
        this.title = taskEntity.getTitle();
        this.description = taskEntity.getDescription();
        this.requestId = taskEntity.getRequest().getId();
        this.stateId = taskEntity.getState().getId();
        this.creatorId = taskEntity.getCreator().getId();
        this.executorId = taskEntity.getExecutor().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(long executorId) {
        this.executorId = executorId;
    }
}
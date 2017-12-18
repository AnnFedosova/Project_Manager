package server.apiEntities;

import server.dbService.entities.RequestEntity;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 12_12_2017L;

    private long id;
    private String title;
    private String description;
    private long projectId;
    private long creatorId;
    private long customerId;
    private long stateId;
    private long priorityId;

    public Request() {
    }

    public Request(RequestEntity requestEntity) {
        this.id = requestEntity.getId();
        this.title = requestEntity.getTitle();
        this.description = requestEntity.getDescription();
        this.projectId = requestEntity.getProject().getId();
        this.creatorId = requestEntity.getCreator().getId();
        this.customerId = requestEntity.getCustomer().getId();
        this.stateId = requestEntity.getState().getId();
        this.priorityId = requestEntity.getPriority().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(long priorityId) {
        this.priorityId = priorityId;
    }
}


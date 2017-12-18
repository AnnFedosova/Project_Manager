package webapp.entities;

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

    public Request(String title, String description, long projectId, long creatorId, long customerId, long stateId, long priorityId) {
        this.id = -1;
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.customerId = customerId;
        this.stateId = stateId;
        this.priorityId = priorityId;
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


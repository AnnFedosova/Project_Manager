package server.apiEntities;


import server.dbService.entities.ProjectEntity;
import server.dbService.entities.ProjectPositionEntity;
import server.dbService.entities.RequestEntity;
import server.dbService.entities.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
public class Project  implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String title;

    private String description;

    private long creatorId;

    public Project() {
    }

    public Project(ProjectEntity projectEntity) {
        this.id = projectEntity.getId();
        this.title = projectEntity.getTitle();
        this.description = projectEntity.getDescription();
        this.creatorId = projectEntity.getCreator().getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatorId() {
        return creatorId;
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

    public void setTitle(String projectName) {
        this.title = projectName;
    }

    @Override
    public String toString() {
        return "Project {" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + title + '\'' +
                ", creator_id='" + creatorId + '\'' +
                '}';
    }
}

package server.apiEntities;

import server.dbService.entities.ProjectPositionEntity;

import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
public class ProjectPosition implements Serializable, Comparable<ProjectPosition> {
    private static final long serialVersionUID = 23_12_2017L;

    private long projectId;
    private User user;
    private Position position;

    public ProjectPosition() {}

    public ProjectPosition(ProjectPositionEntity projectPositionEntity) {
        this.projectId = projectPositionEntity.getProject().getId();
        this.user = new User(projectPositionEntity.getUser());
        this.position = new Position(projectPositionEntity.getPosition());
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int compareTo(ProjectPosition o) {
        int comp = Long.compare(this.projectId, o.getProjectId());
        if (comp != 0) {
            return comp;
        }
        else {
            return (user.getFirstName() + user.getLastName() + user.getLogin()).compareTo(o.getUser().getFirstName() + o.getUser().getLastName() + o.getUser().getLogin());
        }
    }

    @Override
    public String toString() {
        return "ProjectPositionEntity{" +
//                "id=" + id +
                ", projectId='" + projectId + '\'' +
                ", user='" + user + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}

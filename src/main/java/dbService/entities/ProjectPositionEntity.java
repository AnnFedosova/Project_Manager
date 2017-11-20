package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "project_positions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id", "position_id"}))
public class ProjectPositionEntity implements Serializable, Comparable<ProjectPositionEntity> {
    private static final long serialVersionUID = 20_11_2017L;


    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "position_id")
    private PositionEntity position;


    public ProjectPositionEntity() {

    }

    public ProjectPositionEntity(ProjectEntity project, PositionEntity position, UserEntity user) {
        this.project = project;
        this.position = position;
        this.user = user;
    }


    public PositionEntity getPosition() {
        return position;
    }

    public void setPosition(PositionEntity position) {
        this.position = position;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    @Override
    public int compareTo(ProjectPositionEntity o) {
        int comp = Long.compare(this.project.getId(), o.getProject().getId());
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
                ", project_id='" + project.getId() + '\'' +
                ", user_id='" + user.getId() + '\'' +
                ", position_id='" + position.getId() + '\'' +
                '}';
    }
}

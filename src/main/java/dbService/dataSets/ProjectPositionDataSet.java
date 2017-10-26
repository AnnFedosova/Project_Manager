package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "project_positions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id", "position_id"}))
public class ProjectPositionDataSet implements Serializable{
    private static final long serialVersionUID = 26102017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDataSet project;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Id
    @ManyToOne
    @JoinColumn(name = "position_id")
    private PositionDataSet position;

    public ProjectPositionDataSet() {

    }

    public PositionDataSet getPosition() {
        return position;
    }

    public void setPosition(PositionDataSet position) {
        this.position = position;
    }

    public ProjectDataSet getProject() {
        return project;
    }

    public void setProject(ProjectDataSet project) {
        this.project = project;
    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }
}

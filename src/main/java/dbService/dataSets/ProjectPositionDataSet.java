package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "project_positions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id", "position_id"}))
public class ProjectPositionDataSet implements Serializable{
    private static final long serialVersionUID = 30102017L;

    @Id
    @Column(name = "id ", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDataSet project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private PositionDataSet position;

    @OneToMany(mappedBy = "creator")
    private Set<RequestDataSet> requestCreators;

    @OneToMany(mappedBy = "customer")
    private Set<RequestDataSet> requestCustomers;

    @OneToMany(mappedBy = "creator")
    private Set<TaskDataSet> taskCreators;

    @OneToMany(mappedBy = "project")
    private Set<ProjectPositionDataSet> projectPositions;


    public ProjectPositionDataSet() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    @Override
    public String toString() {
        return "ProjectPositionDataSet{" +
                "id=" + id +
                ", project_id='" + project.getId() + '\'' +
                ", user_id='" + user.getId() + '\'' +
                ", position_id='" + position.getId() + '\'' +
                '}';
    }
}

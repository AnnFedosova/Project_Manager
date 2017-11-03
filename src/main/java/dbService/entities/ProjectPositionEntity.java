package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "project_positions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id", "position_id"}))
public class ProjectPositionEntity implements Serializable{
    private static final long serialVersionUID = 2_11_2017L;

//    @Id
//    @Column(name = "id", unique = true, updatable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "project_positions_id_generator")
//    @SequenceGenerator(name = "project_positions_id_generator", sequenceName = "project_positions_id_seq")
//    private long id;

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


//    @OneToMany(mappedBy = "creator")
//    private Set<RequestEntity> requestCreators;
//
//    @OneToMany(mappedBy = "customer")
//    private Set<RequestEntity> requestCustomers;
//
//    @OneToMany(mappedBy = "creator")
//    private Set<TaskEntity> taskCreators;
//
//    @OneToMany(mappedBy = "executor")
//    private Set<TaskEntity> taskExecutors;


    public ProjectPositionEntity() {

    }

    public ProjectPositionEntity(ProjectEntity project, PositionEntity position, UserEntity user) {
        this.project = project;
        this.position = position;
        this.user = user;
    }

//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getId() {
//        return id;
//    }

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
    public String toString() {
        return "ProjectPositionEntity{" +
//                "id=" + id +
                ", project_id='" + project.getId() + '\'' +
                ", user_id='" + user.getId() + '\'' +
                ", position_id='" + position.getId() + '\'' +
                '}';
    }
}

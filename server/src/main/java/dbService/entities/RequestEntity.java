package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "requests", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "title"}))
public class RequestEntity implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "description", columnDefinition="text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private RequestStateEntity state;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private PriorityEntity priority;

    @OneToMany(mappedBy = "request")
    private Set<TaskEntity> tasks;

    public RequestEntity() {}

    public RequestEntity(ProjectEntity project, String title, String description, UserEntity creator, UserEntity customer, RequestStateEntity state, PriorityEntity priority) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.customer = customer;
        this.state = state;
        this.priority = priority;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityEntity getPriority() {
        return priority;
    }

    public void setPriority(PriorityEntity priority) {
        this.priority = priority;
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

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public RequestStateEntity getState() {
        return state;
    }

    public void setState(RequestStateEntity state) {
        this.state = state;
    }

}

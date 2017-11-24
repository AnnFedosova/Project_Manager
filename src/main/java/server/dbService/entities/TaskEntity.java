package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "tasks", uniqueConstraints = @UniqueConstraint(columnNames = {"request_id", "title"}))
public class TaskEntity implements Serializable{
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestEntity request;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition="text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserEntity executor;

    public TaskEntity() {}

    public TaskEntity(String title, String description, RequestEntity request, UserEntity creator, UserEntity executor, StateEntity state) {
        this.title = title;
        this.description = description;
        this.request = request;
        this.state = state;
        this.creator = creator;
        this.executor = executor;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public UserEntity getExecutor() {
        return executor;
    }

    public void setExecutor(UserEntity executor) {
        this.executor = executor;
    }
}

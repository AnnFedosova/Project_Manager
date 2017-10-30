package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "tasks")
public class TaskDataSet implements Serializable{
    private static final long serialVersionUID = 30102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestDataSet request;

    @Column(name = "task_name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateDataSet state;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private ProjectPositionDataSet creator;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private ProjectPositionDataSet executor;

    public TaskDataSet() {}

    public TaskDataSet(String name, String description, RequestDataSet request, StateDataSet state, ProjectPositionDataSet creator, ProjectPositionDataSet executor) {
        this.name = name;
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

    public ProjectPositionDataSet getCreator() {
        return creator;
    }

    public void setCreator(ProjectPositionDataSet creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String taskName) {
        this.name = taskName;
    }

    public RequestDataSet getRequest() {
        return request;
    }

    public void setRequest(RequestDataSet request) {
        this.request = request;
    }

    public StateDataSet getState() {
        return state;
    }

    public void setState(StateDataSet state) {
        this.state = state;
    }

    public ProjectPositionDataSet getExecutor() {
        return executor;
    }

    public void setExecutor(ProjectPositionDataSet executor) {
        this.executor = executor;
    }
}

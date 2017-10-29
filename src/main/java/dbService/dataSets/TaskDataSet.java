package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "tasks")
public class TaskDataSet implements Serializable{
    private static final long serialVersionUID = 27102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestDataSet request;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "text")
    private String taskDescription;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateDataSet state;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserDataSet executor;

    public TaskDataSet() {}

    public TaskDataSet(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String text) {
        this.taskDescription = text;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public UserDataSet getExecutor() {
        return executor;
    }

    public void setExecutor(UserDataSet executor) {
        this.executor = executor;
    }
}

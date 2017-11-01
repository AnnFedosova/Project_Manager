package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "tasks", uniqueConstraints = @UniqueConstraint(columnNames = {"request_id", "title"}))
public class TaskDataSet implements Serializable{
    private static final long serialVersionUID = 1_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tasks_id_generator")
    @SequenceGenerator(name = "tasks_id_generator", sequenceName = "tasks_id_seq")
    private long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestDataSet request;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private TaskStateDataSet state;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserDataSet creator;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserDataSet executor;

    public TaskDataSet() {}

    public TaskDataSet(String title, String description, RequestDataSet request, UserDataSet creator, UserDataSet executor, TaskStateDataSet state) {
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

    public UserDataSet getCreator() {
        return creator;
    }

    public void setCreator(UserDataSet creator) {
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

    public RequestDataSet getRequest() {
        return request;
    }

    public void setRequest(RequestDataSet request) {
        this.request = request;
    }

    public TaskStateDataSet getState() {
        return state;
    }

    public void setState(TaskStateDataSet state) {
        this.state = state;
    }

    public UserDataSet getExecutor() {
        return executor;
    }

    public void setExecutor(UserDataSet executor) {
        this.executor = executor;
    }
}

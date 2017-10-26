package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "requests")
public class RequestDataSet implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDataSet project;

    @Column(name = "request_name", unique = true)
    private String requestName;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateDataSet state;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private PriorityDataSet priority;

    @OneToMany(mappedBy = "request")
    private Set<RequestPositionDataSet> requestPositionDataSets;

    @Column(name = "text")
    private String text;

    public RequestDataSet() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PriorityDataSet getPriority() {
        return priority;
    }

    public void setPriority(PriorityDataSet priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public ProjectDataSet getProject() {
        return project;
    }

    public void setProject(ProjectDataSet project) {
        this.project = project;
    }

    public StateDataSet getState() {
        return state;
    }

    public void setState(StateDataSet state) {
        this.state = state;
    }
}

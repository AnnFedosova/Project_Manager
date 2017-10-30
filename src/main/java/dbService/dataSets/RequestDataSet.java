package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "requests")
public class RequestDataSet implements Serializable {
    private static final long serialVersionUID = 27102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDataSet project;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private ProjectPositionDataSet creator;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private ProjectPositionDataSet customer;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateDataSet state;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private PriorityDataSet priority;

    @OneToMany(mappedBy = "request")
    private Set<TaskDataSet> tasks;

    public RequestDataSet() {}

    public RequestDataSet(ProjectDataSet project, String title, String description, ProjectPositionDataSet creator, ProjectPositionDataSet customer, StateDataSet state, PriorityDataSet priority) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.customer = customer;
        this.state = state;
        this.priority = priority;
    }

    public void setCreator(ProjectPositionDataSet creator) {
        this.creator = creator;
    }

    public ProjectPositionDataSet getCreator() {
        return creator;
    }

    public void setCustomer(ProjectPositionDataSet customer) {
        this.customer = customer;
    }

    public ProjectPositionDataSet getCustomer() {
        return customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

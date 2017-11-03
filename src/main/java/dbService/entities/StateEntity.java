package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "states")
public class StateEntity implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "requests_accord")
    private boolean requestAccord;

    @Column(name = "tasks_accord")
    private boolean tasksAccord;

    @OneToMany(mappedBy = "state")
    private Set<RequestEntity> requests;

    @OneToMany(mappedBy = "state")
    private Set<TaskEntity> tasks;

    public StateEntity() {
    }

    public StateEntity(String name, boolean requestAccord, boolean tasksAccord) {
        this.name = name;
        this.requestAccord = requestAccord;
        this.tasksAccord = tasksAccord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String stateName) {
        this.name = stateName;
    }

    public boolean getRequestsAccord() {
        return requestAccord;
    }

    public void setRequestAccord(boolean requestAccord) {
        this.requestAccord = requestAccord;
    }

    public boolean getTasksAccord() {
        return tasksAccord;
    }

    public void setTasksAccord(boolean tasksAccord) {
        this.tasksAccord = tasksAccord;
    }

    @Override
    public String toString() {
        return "StateEntity{" +
                "id=" + id +
                "requests_accord=" + requestAccord +
                "tasksAccord=" + tasksAccord +
                ", state_name='" + name + '\'' +
                '}';
    }
}

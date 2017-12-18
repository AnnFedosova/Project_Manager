package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "task_states")
public class TaskStateEntity implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true)
    private String title;

    @OneToMany(mappedBy = "state")
    private Set<RequestEntity> requests;

    @OneToMany(mappedBy = "state")
    private Set<TaskEntity> tasks;

    @OneToMany(mappedBy = "fromState")
    private Set<TaskStateTransitionEntity> fromStateStansitions;

    @OneToMany(mappedBy = "toState")
    private Set<TaskStateTransitionEntity> toStateStansitions;

    public TaskStateEntity() {
    }

    public TaskStateEntity(String title) {
        this.title = title;
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

    public void setTitle(String stateName) {
        this.title = stateName;
    }

    @Override
    public String toString() {
        return "TaskStateEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

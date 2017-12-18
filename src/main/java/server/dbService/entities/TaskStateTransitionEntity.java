package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "task_state_transition")
public class TaskStateTransitionEntity implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "from_state_id")
    private TaskStateEntity fromState;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_state_id")
    private TaskStateEntity toState;

    public TaskStateTransitionEntity() {}

    public TaskStateTransitionEntity(TaskStateEntity from, TaskStateEntity to){
        fromState = from;
        toState = to;
    }

    public TaskStateEntity getFromState() {
        return fromState;
    }

    public TaskStateEntity getToState() {
        return toState;
    }

    public void setFromState(TaskStateEntity fromState) {
        this.fromState = fromState;
    }

    public void setToState(TaskStateEntity toState) {
        this.toState = toState;
    }
}

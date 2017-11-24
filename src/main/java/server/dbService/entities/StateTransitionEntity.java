package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "state_transition")
public class StateTransitionEntity implements Serializable {
    private static final long serialVersionUID = 17_11_2017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "from_state_id")
    private StateEntity fromState;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_state_id")
    private StateEntity toState;

    public StateTransitionEntity() {}

    public StateTransitionEntity(StateEntity from, StateEntity to){
        fromState = from;
        toState = to;
    }

    public StateEntity getFromState() {
        return fromState;
    }

    public StateEntity getToState() {
        return toState;
    }

    public void setFromState(StateEntity fromState) {
        this.fromState = fromState;
    }

    public void setToState(StateEntity toState) {
        this.toState = toState;
    }
}

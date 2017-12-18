package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "request_state_transition")
public class RequestStateTransitionEntity implements Serializable {
    private static final long serialVersionUID = 17_11_2017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "from_state_id")
    private RequestStateEntity fromState;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_state_id")
    private RequestStateEntity toState;

    public RequestStateTransitionEntity() {}

    public RequestStateTransitionEntity(RequestStateEntity from, RequestStateEntity to){
        fromState = from;
        toState = to;
    }

    public RequestStateEntity getFromState() {
        return fromState;
    }

    public RequestStateEntity getToState() {
        return toState;
    }

    public void setFromState(RequestStateEntity fromState) {
        this.fromState = fromState;
    }

    public void setToState(RequestStateEntity toState) {
        this.toState = toState;
    }
}

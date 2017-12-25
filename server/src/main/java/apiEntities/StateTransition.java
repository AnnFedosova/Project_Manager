package apiEntities;

import dbService.entities.TaskStateEntity;
import dbService.entities.TaskStateTransitionEntity;
import java.io.Serializable;

public class StateTransition implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long fromStateId;

    private long toStateId;

    public StateTransition() {
    }

    public StateTransition(TaskStateTransitionEntity taskStateTransitionEntity) {
        this.fromStateId = taskStateTransitionEntity.getFromState().getId();
        this.toStateId = taskStateTransitionEntity.getToState().getId();
    }

    public long getFromStateId() {
        return fromStateId;
    }

    public void setFromStateId(long fromStateId) {
        this.fromStateId = fromStateId;
    }

    public long getToStateId() {
        return toStateId;
    }

    public void setToStateId(long toStateId) {
        this.toStateId = toStateId;
    }
}

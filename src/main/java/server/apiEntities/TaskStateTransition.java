package server.apiEntities;

import server.dbService.entities.TaskStateEntity;
import server.dbService.entities.TaskStateTransitionEntity;
import java.io.Serializable;

public class TaskStateTransition implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long fromStateId;

    private long toStateId;

    public TaskStateTransition() {
    }

    public TaskStateTransition(TaskStateTransitionEntity taskStateTransitionEntity) {
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

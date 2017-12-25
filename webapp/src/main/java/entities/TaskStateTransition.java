package entities;

import java.io.Serializable;

public class TaskStateTransition implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long fromStateId;

    private long toStateId;

    public TaskStateTransition() {
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

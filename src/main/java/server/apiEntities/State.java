package server.apiEntities;

import server.dbService.entities.RequestEntity;
import server.dbService.entities.RequestStateEntity;
import server.dbService.entities.TaskStateEntity;

import java.io.Serializable;

public class State implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String title;

    public State() {
    }

    public State(TaskStateEntity taskStateEntity) {
        this.id = taskStateEntity.getId();
        this.title = taskStateEntity.getTitle();
    }

    public State(RequestStateEntity requestStateEntity) {
        this.id = requestStateEntity.getId();
        this.title = requestStateEntity.getTitle();
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
        return "State {" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

package webapp.entities;

import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
public class TaskState implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String title;

    public TaskState() {
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
        return "TaskState {" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

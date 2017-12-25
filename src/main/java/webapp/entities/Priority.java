package webapp.entities;

import java.io.Serializable;


/**
 * @author Evgeny Levin
 */
public class Priority implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String title;

    public Priority() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Priority {" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

}

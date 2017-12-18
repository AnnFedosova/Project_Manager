package webapp.entities;

import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String name;


    public Position() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String roleName) {
        this.name = roleName;
    }

    @Override
    public String toString() {
        return "Position {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
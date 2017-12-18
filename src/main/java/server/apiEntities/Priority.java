package server.apiEntities;

import server.dbService.entities.PriorityEntity;

import java.io.Serializable;


/**
 * @author Evgeny Levin
 */
public class Priority implements Serializable {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String name;

    public Priority() {
    }

    public Priority(PriorityEntity priorityEntity) {
        this.id = priorityEntity.getId();
        this.name = priorityEntity.getName();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Priority {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

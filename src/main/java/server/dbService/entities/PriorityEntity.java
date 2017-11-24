package server.dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "priorities")
public class PriorityEntity implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "priority")
    private Set<RequestEntity> requestEntities;


    public PriorityEntity() {
    }

    public PriorityEntity(String name) {
        this.name = name;
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
        return "PriorityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

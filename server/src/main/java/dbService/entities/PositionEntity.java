package dbService.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "positions")
public class PositionEntity implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "position")
    private Set<ProjectPositionEntity> projectPositions;

    public PositionEntity() {
    }

    public PositionEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PositionEntity(String name) {
        this.name = name;
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
        return "PositionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

package dbService.dataSets;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "positions")
public class PositionDataSet implements Serializable {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "position")
    private Set<ProjectPositionDataSet> projectPositions;

    public PositionDataSet() {
    }

    public PositionDataSet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PositionDataSet(String name) {
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
        return "PositionDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

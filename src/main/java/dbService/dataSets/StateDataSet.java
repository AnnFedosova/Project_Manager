package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "states")
public class StateDataSet  implements Serializable {
    private static final long serialVersionUID = 30102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "state_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "state")
    private Set<RequestDataSet> requests;

    public StateDataSet() {
    }

    public StateDataSet(String name) {
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

    public void setName(String stateName) {
        this.name = stateName;
    }

    @Override
    public String toString() {
        return "StateDataSet{" +
                "id=" + id +
                ", state_name='" + name + '\'' +
                '}';
    }
}

package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "states")
public class StateDataSet  implements Serializable {
    private static final long serialVersionUID = 25102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "state_name", unique = true)
    private String stateName;

    @OneToMany(mappedBy = "state")
    private Set<RequestDataSet> requests;

    public StateDataSet() {
    }

    public StateDataSet(String stateName) {
        this.stateName = stateName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "ProjectDataSet{" +
                "id=" + id +
                ", state_name='" + stateName + '\'' +
                '}';
    }
}

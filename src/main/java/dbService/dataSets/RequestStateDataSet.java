package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "request_states")
public class RequestStateDataSet implements Serializable {
    private static final long serialVersionUID = 1_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "request_states_id_generator")
    @SequenceGenerator(name = "request_states_id_generator", sequenceName = "request_states_id_seq")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "state")
    private Set<RequestDataSet> requests;

    public RequestStateDataSet() {
    }

    public RequestStateDataSet(String name) {
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
        return "RequestStateDataSet{" +
                "id=" + id +
                ", state_name='" + name + '\'' +
                '}';
    }
}

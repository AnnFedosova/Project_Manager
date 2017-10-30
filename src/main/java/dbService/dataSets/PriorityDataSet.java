package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "Priorities")
public class PriorityDataSet  implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "priority_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "priority")
    private Set<RequestDataSet> RequestDataSets;


    public PriorityDataSet() {
    }

    public PriorityDataSet(String name) {
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
        return "PriorityDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}

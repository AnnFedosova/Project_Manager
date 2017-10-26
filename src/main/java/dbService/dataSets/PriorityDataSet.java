package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Priorities")
public class PriorityDataSet  implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "priority_name", unique = true)
    private String priorityName;

    @OneToMany(mappedBy = "priority")
    private Set<RequestDataSet> RequestDataSets;


    public PriorityDataSet() {
    }

    public PriorityDataSet(String priorityName) {
        this.priorityName = priorityName;
    }


    public long getId() {
        return id;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

}

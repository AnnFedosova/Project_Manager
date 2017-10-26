package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Priorities")
public class PriorityDataSet  implements Serializable {
    private static final long serialVersionUID = 25102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "priority_name", unique = true)
    private String priorityName;


    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public PriorityDataSet() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public PriorityDataSet(long id, String priorityName) {
        this.id = id;
        this.priorityName = priorityName;
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

    @Override
    public String toString() {
        return "RoleDataSet{" +
                "id=" + id +
                ", role_name='" + priorityName + '\'' +
                '}';
    }
}

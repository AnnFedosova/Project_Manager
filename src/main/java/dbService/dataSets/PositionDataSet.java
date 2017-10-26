package dbService.dataSets;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "positions")
public class PositionDataSet implements Serializable {
    private static final long serialVersionUID = 25102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "role_name", unique = true)
    private String roleName;

    @OneToMany(mappedBy = "position")
    private Set<RequestPositionDataSet> requestPositionDataSets;

    public PositionDataSet() {
    }

    public PositionDataSet(long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public PositionDataSet(String roleName) {
        this.roleName = roleName;
    }


    public long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "PositionDataSet{" +
                "id=" + id +
                ", role_name='" + roleName + '\'' +
                '}';
    }



}

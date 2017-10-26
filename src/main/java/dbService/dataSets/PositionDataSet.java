package dbService.dataSets;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "positions")
public class PositionDataSet implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "role_name", unique = true)
    private String positionName;

    @OneToMany(mappedBy = "position")
    private Set<RequestPositionDataSet> requestPositionDataSets;

    public PositionDataSet() {
    }

    public PositionDataSet(long id, String positionName) {
        this.id = id;
        this.positionName = positionName;
    }

    public PositionDataSet(String positionName) {
        this.positionName = positionName;
    }


    public long getId() {
        return id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPositionName(String roleName) {
        this.positionName = roleName;
    }

}

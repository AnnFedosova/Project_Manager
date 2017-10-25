package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class RolesDataset implements Serializable {
    private static final long serialVersionUID = 24102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "role_name", unique = true)
    private String roleName;


    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public RolesDataset() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public RolesDataset(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public RolesDataset(String roleName) {
        this.roleName = roleName;
    }


    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RolesDataset{" +
                "id=" + id +
                ", role_name='" + roleName + '\'' +
                '}';
    }
}
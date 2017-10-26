package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class RoleDataSet implements Serializable {
    private static final long serialVersionUID = 25102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "role_name", unique = true)
    private String roleName;


    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public RoleDataSet() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public RoleDataSet(long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public RoleDataSet(String roleName) {
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
        return "RoleDataSet{" +
                "id=" + id +
                ", role_name='" + roleName + '\'' +
                '}';
    }
}
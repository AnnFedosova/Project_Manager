package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "roles")
public class RoleDataSet implements Serializable {
    private static final long serialVersionUID = 26102017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role_name", unique = true)
    private String roleName;


    public RoleDataSet() {
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
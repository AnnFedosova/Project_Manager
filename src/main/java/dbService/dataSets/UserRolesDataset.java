package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRolesDataset implements Serializable {
    private static final long serialVersionUID = 24102017L;

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "role_id")
    private int roleId;


    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public UserRolesDataset() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public UserRolesDataset(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }


    @Override
    public String toString() {
        return "UserRolesDataset{" +
                "user_id=" + userId +
                ", role_id='" + roleId + '\'' +
                '}';
    }
}
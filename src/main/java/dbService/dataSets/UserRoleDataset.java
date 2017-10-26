package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRoleDataset implements Serializable {
    private static final long serialVersionUID = 25102017L;

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "role_id")
    private long roleId;


    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public UserRoleDataset() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public UserRoleDataset(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }


    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public long getRoleId() {
        return roleId;
    }


    @Override
    public String toString() {
        return "UserRoleDataset{" +
                "user_id=" + userId +
                ", role_id='" + roleId + '\'' +
                '}';
    }
}
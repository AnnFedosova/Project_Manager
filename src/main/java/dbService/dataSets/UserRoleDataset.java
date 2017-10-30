package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRoleDataset implements Serializable {
    private static final long serialVersionUID = 30102017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleDataSet role;


    public UserRoleDataset() {
    }

    public UserRoleDataset(UserDataSet user, RoleDataSet role) {
        this.user = user;
        this.role = role;
    }


    public void setUserId(UserDataSet user) {
        this.user = user;
    }

    public void setRoleId(RoleDataSet role) {
        this.role = role;
    }

    public UserDataSet getUserId() {
        return user;
    }

    public RoleDataSet getRoleId() {
        return role;
    }


    @Override
    public String toString() {
        return "UserRoleDataset{" +
                "user_id=" + user.getId() +
                ", role_id='" + role.getId() + '\'' +
                '}';
    }
}
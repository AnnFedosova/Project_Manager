package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRoleEntity implements Serializable {
    private static final long serialVersionUID = 1_11_2017L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;


    public UserRoleEntity() {
    }

    public UserRoleEntity(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public UserEntity getUser() {
        return user;
    }

    public RoleEntity getRole() {
        return role;
    }


    @Override
    public String toString() {
        return "UserRoleEntity{" +
                "user_id=" + user.getId() +
                ", role_id='" + role.getId() + '\'' +
                '}';
    }
}
package dbService.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Evgeny Levin
 */
@Entity
@Table(name = "users")
public class UserEntity implements Serializable, Comparable<UserEntity> {
    private static final long serialVersionUID = 2_11_2017L;

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name="internal")
    private boolean internal;

    @OneToMany(mappedBy = "creator")
    private Set<TaskEntity> taskCreators;

    @OneToMany(mappedBy = "executor")
    private Set<TaskEntity> taskExecutors;

    @OneToMany(mappedBy = "creator")
    private Set<RequestEntity> requestCreators;

    @OneToMany(mappedBy = "customer")
    private Set<RequestEntity> requestCustomers;

    @OneToMany(mappedBy = "creator")
    private Set<ProjectEntity> projects;

    @OneToMany(mappedBy = "user")
    private Set<UserRoleEntity> userRoleEntitySet;


    public UserEntity() {
    }


    public UserEntity(String login, String password, boolean internal, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.internal = internal;
    }

    public UserEntity(String login, String password, boolean internal, String firstName, String lastName, String middleName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.internal = internal;
        this.middleName = middleName;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public boolean getInternal() {
        return internal;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {this.password = password;}

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String patronymic) {
        this.middleName = patronymic;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + firstName + '\'' +
                ", middle_name='" + middleName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", internal='" + internal + '\'' +
                '}';
    }

    @Override
    public int compareTo(UserEntity o) {
        return (firstName + lastName + login).compareTo(o.getFirstName() + o.getLastName() + o.getLogin());
    }
}

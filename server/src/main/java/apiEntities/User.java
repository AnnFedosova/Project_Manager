package apiEntities;

import dbService.entities.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Evgeny Levin
 */
public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 11_12_2017L;

    private long id;

    private String login;

    private String firstName;

    private String lastName;

    private String middleName;

    private boolean internal;

    public User() {
    }

    public User(String login, String firstName, String lastName, String middleName, boolean internal) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.internal = internal;
    }

    public User(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.login = userEntity.getLogin();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.middleName = userEntity.getMiddleName();
        this.internal = userEntity.getInternal();
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
        return "User {" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", first_name='" + firstName + '\'' +
                ", middle_name='" + middleName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", internal='" + internal + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return (firstName + lastName + login).compareTo(o.getFirstName() + o.getLastName() + o.getLogin());
    }
}

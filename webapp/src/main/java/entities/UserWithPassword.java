package entities;

public class UserWithPassword extends User {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   public UserWithPassword() {}

    public UserWithPassword(String login, String firstName, String lastName, String middleName, boolean internal, String password) {
        super(login, firstName, lastName, middleName, internal);
        this.password = password;
    }
}

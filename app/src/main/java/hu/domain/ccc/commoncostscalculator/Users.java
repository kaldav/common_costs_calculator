package hu.domain.ccc.commoncostscalculator;

/**
 * Created by Gergely on 2015.03.26..
 */
public class Users {

    private String userName;
    private String email;
    private String firstname;
    private String lastname;

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    private String name;

    public Users(String userName, String email, String firstname, String lastname) {
        this.userName = userName;
        this.email = email;
        this.firstname=firstname;
        this.lastname=lastname;
        this.name=firstname+" "+lastname;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}

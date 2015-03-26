package hu.domain.ccc.commoncostscalculator;

/**
 * Created by Gergely on 2015.03.26..
 */
public class Users {

    private String userName;
    private String email;

    public Users(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}

package hu.domain.ccc.commoncostscalculator;

import java.util.ArrayList;

/**
 * A tételeket leíró osztály
 */
public class Items {

    String description; // tétel leírása
    int sum; // tétel összege
    ArrayList<Users> users;

    public String getDescription() {
        return description;
    }

    public int getSum() {
        return sum;
    }

    public ArrayList<Users> getUsers() {
        return users;
    }

    public Items(String description, int sum, ArrayList<Users> users) {

        this.description = description;
        this.sum = sum;
        this.users = users;
    }
}

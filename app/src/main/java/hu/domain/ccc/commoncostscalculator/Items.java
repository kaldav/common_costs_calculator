package hu.domain.ccc.commoncostscalculator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * A tételeket leíró osztály
 */
public class Items implements Parcelable {

    String name; // tétel neve
    String description; // tétel leírása
    int sum; // tétel összege
    int count; // tétel ddarabszám

    ArrayList<Users> users; // tételhez tartozó felhasználók


    public Items(String name, String description, int sum, int count, ArrayList<Users> users) {
        this.name = name;
        this.description = description;
        this.sum = sum;
        this.count = count;
        this.users = users;
    }
    public Items(Parcel parcel) {
        String[] data = new String[4];
        parcel.readStringArray(data);

        ArrayList<Users> u = new ArrayList<Users>();
        parcel.readTypedList(u, Users.CREATOR);

        this.name = data[0];
        this.description = data[1];
        this.sum = Integer.parseInt(data[2]);
        this.count = Integer.parseInt(data[3]);
        this.users = u;
    }


    public String getDescription() {
        return description;
    }

    public int getSum() {
        return sum;
    }

    public ArrayList<Users> getUsers() {
        return users;
    }



    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeStringArray(new String[] {
                                            name,
                                            description,
                                            Integer.toString(sum),
                                            Integer.toString(count)
          });

        dest.writeTypedList(users);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Items(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Items[size];
        }
    };
}

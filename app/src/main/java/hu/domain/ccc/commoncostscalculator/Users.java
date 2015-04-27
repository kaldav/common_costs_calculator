package hu.domain.ccc.commoncostscalculator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gergely on 2015.03.26..
 */
public class Users implements Parcelable{

    private String userName;
    private String email;
    private String firstname;
    private String lastname;
    private boolean checked;
    private String id;

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

    public Users(String id, String userName, String email, String firstname, String lastname) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.firstname=firstname;
        this.lastname=lastname;
        this.name=firstname+" "+lastname;
        this.checked = false;

    }

    public boolean isChecked() {
        return checked;
    }
    //The Cake is a Lie...Tonté hazudott, PairProgramingForEver
    public void switchChecked() {
        //roland meleg de az ő ötlete volt!!!44!!
        this.checked = !this.checked;
    }

    public Users(Parcel parcel)
    {
        String[] data = new String[5];
        parcel.readStringArray(data);
        this.id = data[0];
        this.userName = data[1];
        this.email = data[2];
        this.firstname= data[3];
        this.lastname= data[4];
        this.name= firstname+" "+lastname;

    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeStringArray(new String[] { this.userName,
                                                this.email,
                                                this.firstname,
                                                this.lastname });
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){


        @Override
        public Object createFromParcel(Parcel source) {
            return new Users(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Users[size];
        }
    };
}

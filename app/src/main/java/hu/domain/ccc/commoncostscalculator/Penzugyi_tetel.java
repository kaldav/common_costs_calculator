package hu.domain.ccc.commoncostscalculator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kovács on 2015.05.07..
 */
public class Penzugyi_tetel implements Parcelable{
    private String kinek;
    private int mennyivel;

    public String getKinek() {
        return kinek;
    }

    public int getMennyivel() {
        return mennyivel;
    }

    public Penzugyi_tetel(String kinek, int mennyivel)
    {
        this.kinek = kinek;
        this.mennyivel = mennyivel;
    }

    public Penzugyi_tetel(Parcel parcel)
    {
        String[] data = new String[2];
        parcel.readStringArray(data);
        this.kinek = data[0];
        this.mennyivel = Integer.getInteger(data[1]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.kinek, Integer.toString(this.mennyivel)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){


        @Override
        public Object createFromParcel(Parcel source) {
            return new Penzugyi_tetel(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Penzugyi_tetel[size];
        }
    };
}

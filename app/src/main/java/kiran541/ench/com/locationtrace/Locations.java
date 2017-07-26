package kiran541.ench.com.locationtrace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiran0407 on 12/7/17.
 */

public class Locations implements Parcelable {
    String name;
    String loclat;
    String loclong;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoclat() {
        return loclat;
    }

    public void setLoclat(String loclat) {
        this.loclat = loclat;
    }

    public String getLoclong() {
        return loclong;
    }

    public void setLoclong(String loclong) {
        this.loclong = loclong;
    }



    protected Locations(Parcel in) {
        name = in.readString();
        loclat = in.readString();
        loclong = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(loclat);
        dest.writeString(loclong);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };
}

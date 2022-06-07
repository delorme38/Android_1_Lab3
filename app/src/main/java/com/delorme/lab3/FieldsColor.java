package com.delorme.lab3;

import android.os.Parcel;
import android.os.Parcelable;

public class FieldsColor implements Parcelable {
    public int firstName = R.color.black;
    public int lastName = R.color.black;
    public int email = R.color.black;
    public int phone = R.color.black;

    public FieldsColor(){}

    protected FieldsColor(Parcel in) {
        firstName = in.readInt();
        lastName = in.readInt();
        email = in.readInt();
        phone = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(firstName);
        dest.writeInt(lastName);
        dest.writeInt(email);
        dest.writeInt(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FieldsColor> CREATOR = new Creator<FieldsColor>() {
        @Override
        public FieldsColor createFromParcel(Parcel in) {
            return new FieldsColor(in);
        }

        @Override
        public FieldsColor[] newArray(int size) {
            return new FieldsColor[size];
        }
    };
}

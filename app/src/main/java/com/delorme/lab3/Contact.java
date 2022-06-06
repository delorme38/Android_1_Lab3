package com.delorme.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class Contact extends BaseObservable implements Parcelable {
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String email = "";
    private boolean male = false;
    private boolean female = false;

    public Contact() {};

    protected Contact(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        male = in.readByte() != 0;
        female = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeByte((byte) (male ? 1 : 0));
        dest.writeByte((byte) (female ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contact setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isMale() {
        return male;
    }

    public Contact setMale(boolean male) {
        this.male = male;
        return this;
    }

    public boolean isFemale() {
        return female;
    }

    public Contact setFemale(boolean female) {
        this.female = female;
        return this;
    }

    public static Contact giveMeBatman() {
        Contact batman = new Contact();
        batman.setFirstName("Bruce");
        batman.setLastName("Wayne");
        batman.setMale(true);
        batman.setPhoneNumber("(555) 555-5555");
        batman.setEmail("bruce_wayne@batman.com");

        return batman;
    }
}

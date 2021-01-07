package com.ioannisnicos.ethiomoviesstore.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    private int id;
    private String google_id;
    private String notif_token;

    private String first_name;
    private String last_name;
    private String display_name;
    private String email;

    private String language;
    private String description;

    private String prof_img;
    private String banner;

    private String address;
    private String postcode;
    private double geolng;
    private double geolat;

    private String reg_date;


    public Users(int id, String google_id, String notif_token, String first_name, String last_name, String display_name, String email, String language, String description, String prof_img, String banner, String address, String postcode, double geolng, double geolat, String reg_date) {
        this.id = id;
        this.google_id = google_id;
        this.notif_token = notif_token;
        this.first_name = first_name;
        this.last_name = last_name;
        this.display_name = display_name;
        this.email = email;
        this.language = language;
        this.description = description;
        this.prof_img = prof_img;
        this.banner = banner;
        this.address = address;
        this.postcode = postcode;
        this.geolng = geolng;
        this.geolat = geolat;
        this.reg_date = reg_date;
    }

    public Users() {
    }

    protected Users(Parcel in) {
        id = in.readInt();
        google_id = in.readString();
        notif_token = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        display_name = in.readString();
        email = in.readString();
        language = in.readString();
        description = in.readString();
        prof_img = in.readString();
        banner = in.readString();
        address = in.readString();
        postcode = in.readString();
        geolng = in.readDouble();
        geolat = in.readDouble();
        reg_date = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getNotif_token() {
        return notif_token;
    }

    public void setNotif_token(String notif_token) {
        this.notif_token = notif_token;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProf_img() {
        return prof_img;
    }

    public void setProf_img(String prof_img) {
        this.prof_img = prof_img;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public double getGeolng() {
        return geolng;
    }

    public void setGeolng(double geolng) {
        this.geolng = geolng;
    }

    public double getGeolat() {
        return geolat;
    }

    public void setGeolat(double geolat) {
        this.geolat = geolat;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(google_id);
        parcel.writeString(notif_token);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(display_name);
        parcel.writeString(email);
        parcel.writeString(language);
        parcel.writeString(description);
        parcel.writeString(prof_img);
        parcel.writeString(banner);
        parcel.writeString(address);
        parcel.writeString(postcode);
        parcel.writeDouble(geolng);
        parcel.writeDouble(geolat);
        parcel.writeString(reg_date);
    }
}

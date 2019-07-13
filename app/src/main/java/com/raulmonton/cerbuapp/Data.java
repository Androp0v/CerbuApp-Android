package com.raulmonton.cerbuapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable{
    private int id;
    private String name;
    private String surname_1;
    private String surname_2;
    private String career;
    private int promotion;
    private String room;
    private String beca;
    private int liked;
    private int floor;

    public Data(){

    }

    public Data(int id, String name, String surname_1, String surname_2, String career, int promotion, String room, String beca, int liked, int floor){
        this.id = id;
        this.name = name;
        this.surname_1 = surname_1;
        this.surname_2 = surname_2;
        this.career = career;
        this.promotion = promotion;
        this.room = room;
        this.beca = beca;
        this.liked = liked;
        this.floor = floor;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname_1() {
        return surname_1;
    }

    public void setSurname_1(String surname_1) {
        this.surname_1 = surname_1;
    }

    public String getSurname_2() {
        return surname_2;
    }

    public void setSurname_2(String surname_2) {
        this.surname_2 = surname_2;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBeca() {
        return beca;
    }

    public void setBeca(String beca) {
        this.beca = beca;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    // Parcelling part

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname_1);
        dest.writeString(surname_2);
        dest.writeString(career);
        dest.writeInt(promotion);
        dest.writeString(room);
        dest.writeString(beca);
        dest.writeInt(liked);
        dest.writeInt(floor);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data(Parcel source){
        id = source.readInt();
        name = source.readString();
        surname_1 = source.readString();
        surname_2 = source.readString();
        career = source.readString();
        promotion = source.readInt();
        room = source.readString();
        beca = source.readString();
        liked = source.readInt();
        floor = source.readInt();
    }
}
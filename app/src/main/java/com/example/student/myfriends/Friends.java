package com.example.student.myfriends;


public class Friends {
    public int _id;
    public String name = "";
    public String email = "";
    public String phone = "";



    public int getID() {
        return this._id;
    }

    public void setID(int id) {

        this._id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String ema) {
        this.email = ema;
    }


    public String getMobileNo() {
        return phone;
    }
    public void setMobileNo(String mobileNo) {
        this.phone = mobileNo;
    }
    // constructor
    public Friends(int id, String name,String em,String mobileNo){
        this._id = id;
        this.email = em;
        this.name = name;
        this.phone=mobileNo;
    }

    //***** Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }
    public Friends(){

    }
}



package com.maksof.linkapp.data;

public class DoctorsProfile {
    String name = "name";
    String phoneNumber = "0311111111";

    public String getName(){
        return this.name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public DoctorsProfile(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}

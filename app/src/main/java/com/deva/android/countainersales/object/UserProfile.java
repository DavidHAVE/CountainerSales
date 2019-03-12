package com.deva.android.countainersales.object;

/**
 * Created by David on 05/10/2017.
 */

public class UserProfile {

    private String uid;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean paidOff;

    public UserProfile(){
    }

    public UserProfile(String uid, String name, String email, String phoneNumber, String address,
                       boolean paidOff){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paidOff = paidOff;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaidOff() {
        return paidOff;
    }

    public void setPaidOff(boolean paidOff) {
        this.paidOff = paidOff;
    }
}

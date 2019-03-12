package com.deva.android.countainersales.object;

import static android.R.attr.key;
import static android.R.attr.theme;
import static android.R.attr.thickness;

/**
 * Created by David on 07/10/2017.
 */

//OrderItem :
//        String orderID; 2
//        String name;
//        String quanitty;
//        long price;
//
//OrderItem :
//        String orderID; 2
//        String name;
//        String quanitty;
//        long price;

public class Order {
    String uid;
    String orderID; // 2
    String name;
    String email;
    String phoneNumber;
    String address;
    String date;
//    String item;
//    long priceHighCube;
//    long priceFeet40;
//    long priceFeet20;
//    int countHighCube;
//    int countFeet40;
//    int countFeet20;
    long estimate;
    long designPrice;
    boolean isDesign;
    String urlDesign;

    public Order(){
    }

//    public Order(String uid, String date, long priceHighCube, long priceFeet40, long priceFeet20,
//                 int countHighCube, int countFeet40, int countFeet20, String urlDesign){
//        this.uid = uid;
//        this.date = date;
//        this.priceHighCube = priceHighCube;
//        this.priceFeet40 = priceFeet40;
//        this.priceFeet20 = priceFeet20;
//        this.countHighCube = countHighCube;
//        this.countFeet40 = countFeet40;
//        this.countFeet20 = countFeet20;
//        this.urlDesign = urlDesign;
//    }

    public Order(String uid, String orderID, String name, String email, String phoneNumber, String address,
                 String date, long estimate, long designPrice, boolean isDesign, String urlDesign){
        this.uid = uid;
        this.orderID = orderID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.date = date;
        this.estimate = estimate;
        this.designPrice = designPrice;
        this.isDesign = isDesign;
        this.urlDesign = urlDesign;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getEstimate() {
        return estimate;
    }

    public void setEstimate(long estimate) {
        this.estimate = estimate;
    }

    public long getDesignPrice() {
        return designPrice;
    }

    public void setDesignPrice(long designPrice) {
        this.designPrice = designPrice;
    }

    public boolean isDesign() {
        return isDesign;
    }

    public void setDesign(boolean design) {
        isDesign = design;
    }

    public String getUrlDesign() {
        return urlDesign;
    }

    public void setUrlDesign(String urlDesign) {
        this.urlDesign = urlDesign;
    }

}

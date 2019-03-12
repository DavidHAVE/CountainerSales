package com.deva.android.countainersales.object;

/**
 * Created by David on 14/10/2017.
 */

public class History {
    String uid;
    String orderID;
    String item;
    String quantity;
    String totalPrice;

    public History(){
    }

    public History(String uid, String orderID, String item, String quantity, String totalPrice){
        this.uid = uid;
        this.orderID = orderID;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


}

package com.deva.android.countainersales.object;

/**
 * Created by David on 08/10/2017.
 */

public class Cart {

    String uid;
    String orderNumber;
    int productID;
    int quantity;
    long totalPrice;
    String key;

    public Cart(){
    }

    public Cart(String uid, String orderNumber, int productID, int quantity, long totalPrice, String key){
        this.uid = uid;
        this.orderNumber = orderNumber;
        this.productID = productID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

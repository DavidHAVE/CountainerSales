package com.deva.android.countainersales.object;

/**
 * Created by David on 08/10/2017.
 */

public class OrderItem {
    String orderID;
    String name;
    int quantity;
    long price;
    String key;

    public OrderItem(){
    }

    public OrderItem(String orderID, String name, int quantity, long price, String key){
        this.orderID = orderID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.key = key;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

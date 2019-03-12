package com.deva.android.countainersales.object;

/**
 * Created by David on 08/10/2017.
 */

public class Item {
    String description;
    String name;
    long price;
    int productID;

    public Item(){
    }

    public Item(String description, String name, long price, int productID){
        this.description = description;
        this.name = name;
        this.price = price;
        this.productID = productID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}

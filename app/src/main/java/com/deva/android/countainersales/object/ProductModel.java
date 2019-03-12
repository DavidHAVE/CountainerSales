package com.deva.android.countainersales.object;

/**
 * Created by David on 06/10/2017.
 */

public class ProductModel {


    public String name,unitName, brandName, categoryName;

    public ProductModel(String product_name, String unit, String brand_name) {
        this.setName(product_name);
        this.setUnitName(unit);
        this.setBrandName(brand_name);
//        this.setCategoryName(category_name);
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
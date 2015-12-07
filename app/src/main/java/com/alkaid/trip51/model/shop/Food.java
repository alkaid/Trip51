package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/24.
 */
public class Food implements Serializable{
    private long foodid;
    private String foodname;
    private String foodimg;
    private float price;
    private long sales;
    private float promotionprice;
    private int foodnum;

    public long getFoodid() {
        return foodid;
    }

    public void setFoodid(long foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodimg() {
        return foodimg;
    }

    public void setFoodimg(String foodimg) {
        this.foodimg = foodimg;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }

    public float getPromotionprice() {
        return promotionprice;
    }

    public void setPromotionprice(float promotionprice) {
        this.promotionprice = promotionprice;
    }

    public int getFoodNum() {
        return foodnum;
    }

    public void setFoodNum(int foodNum) {
        this.foodnum = foodNum;
    }
}

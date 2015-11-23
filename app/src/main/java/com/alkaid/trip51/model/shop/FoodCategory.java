package com.alkaid.trip51.model.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class FoodCategory implements Serializable {
    private long categoryid;
    private String categoryname;
    private List<Food> foods;

    public long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(long categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}

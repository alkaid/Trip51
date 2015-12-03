package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/29.
 */
public class ShopCategory implements Serializable {
    private int categoryid;
    private String categoryname;
//    private int categoryshopnum;

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

//    public int getCategoryshopnum() {
//        return categoryshopnum;
//    }
//
//    public void setCategoryshopnum(int categoryshopnum) {
//        this.categoryshopnum = categoryshopnum;
//    }
}

package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/12/2.
 */
public class Cuisine implements Serializable{
    private int cuisineid;
    private String cuisinename;
    private int cuisineshopnum;

    public int getCuisineid() {
        return cuisineid;
    }

    public void setCuisineid(int cuisineid) {
        this.cuisineid = cuisineid;
    }

    public String getCuisinename() {
        return cuisinename;
    }

    public void setCuisinename(String cuisinename) {
        this.cuisinename = cuisinename;
    }

    public int getCuisineshopnum() {
        return cuisineshopnum;
    }

    public void setCuisineshopnum(int cuisineshopnum) {
        this.cuisineshopnum = cuisineshopnum;
    }
}

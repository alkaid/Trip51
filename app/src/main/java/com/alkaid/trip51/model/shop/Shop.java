package com.alkaid.trip51.model.shop;

import com.alkaid.trip51.model.config.TimeSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/29.
 */
public class Shop implements Serializable {
    private long shopid;
    private String shopname="";
    private String avgpay="";
    private String imageurl="";
    private String diningtypename="";
    private float totallevel;
    private String areaname="";
    private int privaterroomstatus;
    private int loungestatus;
    private int hallstatus;
    private int distance;
    private List<TimeSet> timesets=new ArrayList<>();

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAvgpay() {
        return avgpay;
    }

    public void setAvgpay(String avgpay) {
        this.avgpay = avgpay;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDiningtypename() {
        return diningtypename;
    }

    public void setDiningtypename(String diningtypename) {
        this.diningtypename = diningtypename;
    }

    public float getTotallevel() {
        return totallevel;
    }

    public void setTotallevel(float totallevel) {
        this.totallevel = totallevel;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public int getPrivaterroomstatus() {
        return privaterroomstatus;
    }

    public void setPrivaterroomstatus(int privaterroomstatus) {
        this.privaterroomstatus = privaterroomstatus;
    }

    public int getLoungestatus() {
        return loungestatus;
    }

    public void setLoungestatus(int loungestatus) {
        this.loungestatus = loungestatus;
    }

    public int getHallstatus() {
        return hallstatus;
    }

    public void setHallstatus(int hallstatus) {
        this.hallstatus = hallstatus;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<TimeSet> getTimesets() {
        return timesets;
    }

    public void setTimesets(List<TimeSet> timesets) {
        this.timesets = timesets;
    }
}

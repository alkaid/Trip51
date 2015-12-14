package com.alkaid.trip51.model.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class Baseinfo implements Serializable {
    private long shopid;
    private String shopname;
    private String avgpay;
    private String diningtypename;
    private String imgurl;
    private float starlevel;
    private String areanamel;
    private int privaterroomstatus;
    private int loungestatus;
    private int hallstatus;
    private int distance;
    private String address;
    private String envscore;
    private String tastescore;
    private String servicescore;
    private String tips;
    private int totallevel;
    private List<ShopImg> shopimgs=new ArrayList<>();

    public static class ShopImg{
        public String shopimgurl;

    }

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public float getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(float starlevel) {
        this.starlevel = starlevel;
    }

    public String getAreanamel() {
        return areanamel;
    }

    public void setAreanamel(String areanamel) {
        this.areanamel = areanamel;
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

    public List<ShopImg> getShopimgs() {
        return shopimgs;
    }

    public void setShopimgs(List<ShopImg> shopimgs) {
        this.shopimgs = shopimgs;
    }

    public String getDiningtypename() {
        return diningtypename;
    }

    public void setDiningtypename(String diningtypename) {
        this.diningtypename = diningtypename;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnvscore() {
        return envscore;
    }

    public void setEnvscore(String envscore) {
        this.envscore = envscore;
    }

    public String getTastescore() {
        return tastescore;
    }

    public void setTastescore(String tastescore) {
        this.tastescore = tastescore;
    }

    public String getServicescore() {
        return servicescore;
    }

    public void setServicescore(String servicescore) {
        this.servicescore = servicescore;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public int getTotallevel() {
        return totallevel;
    }

    public void setTotallevel(int totallevel) {
        this.totallevel = totallevel;
    }
}

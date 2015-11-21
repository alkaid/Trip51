package com.alkaid.trip51.model.response;

import java.util.List;

/**
 * Created by df on 2015/11/20.
 */
public class ResShopList extends  ResponseData {
    private int total;
    private List<Shop> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Shop> getData() {
        return data;
    }

    public void setData(List<Shop> data) {
        this.data = data;
    }

    public static class Shop{
        private int shopid;
        private String shopname;
        private String avgpay;
        private String imageurl;
        private String diningtypename;
        private float totallevel;
        private String areaname;
        private int privaterroomstatus;
        private int loungestatus;
        private int hallstatus;
        private int distance;

        public int getShopid() {
            return shopid;
        }

        public void setShopid(int shopid) {
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
    }
}

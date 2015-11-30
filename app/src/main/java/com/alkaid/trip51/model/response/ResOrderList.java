package com.alkaid.trip51.model.response;

import java.util.List;

/**
 * Created by df on 2015/11/30.
 */
public class ResOrderList extends ResponseData {
    private List<Order> data;

    public static class Order{
        private String orderno;
        private long orderid;
        private long shopid;
        private String shopname;
        private String shopimgurl;
        private int personnum;
        private int roomtype;
        private String dinnerdate;
        private String orderamount;
        private int orderstatus;

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public long getOrderid() {
            return orderid;
        }

        public void setOrderid(long orderid) {
            this.orderid = orderid;
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

        public String getShopimgurl() {
            return shopimgurl;
        }

        public void setShopimgurl(String shopimgurl) {
            this.shopimgurl = shopimgurl;
        }

        public int getPersonnum() {
            return personnum;
        }

        public void setPersonnum(int personnum) {
            this.personnum = personnum;
        }

        public int getRoomtype() {
            return roomtype;
        }

        public void setRoomtype(int roomtype) {
            this.roomtype = roomtype;
        }

        public String getDinnerdate() {
            return dinnerdate;
        }

        public void setDinnerdate(String dinnerdate) {
            this.dinnerdate = dinnerdate;
        }

        public String getOrderamount() {
            return orderamount;
        }

        public void setOrderamount(String orderamount) {
            this.orderamount = orderamount;
        }

        public int getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(int orderstatus) {
            this.orderstatus = orderstatus;
        }
    }


    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}

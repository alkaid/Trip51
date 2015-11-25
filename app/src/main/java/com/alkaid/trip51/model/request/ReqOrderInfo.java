package com.alkaid.trip51.model.request;

import com.alkaid.trip51.model.shop.Food;

import java.io.Serializable;
import java.util.List;

/**
 * Created by df on 2015/11/25.
 */
public class ReqOrderInfo implements Serializable {
//    private float orderamount;
//    private float foodamount;
    private long shopid;
    private int personnum;
    private String dinnertime;
    private int roomtype;
    private int roomnum;
    private int sex;
    private String mobile;
    private int isreplaceother;
    private int othersex;
    private String otherusername;
    private String othermobile;
    private int iscontainfood;
    private List<ReqFood> foods;

    public static class ReqFood implements Serializable{
        private long foodid;
        private String foodname;
        private float foodprice;
        private int foodnum;
        public ReqFood(Food food,int foodnum){
            this.foodid=food.getFoodid();
            this.foodname=food.getFoodname();
            this.foodprice=food.getPrice();
            this.foodnum=foodnum;
        }

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

        public float getFoodprice() {
            return foodprice;
        }

        public void setFoodprice(float foodprice) {
            this.foodprice = foodprice;
        }

        public int getFoodnum() {
            return foodnum;
        }

        public void setFoodnum(int foodnum) {
            this.foodnum = foodnum;
        }
    }

//    public String getOrderno() {
//        return orderno;
//    }
//
//    public void setOrderno(String orderno) {
//        this.orderno = orderno;
//    }

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public int getPersonnum() {
        return personnum;
    }

    public void setPersonnum(int personnum) {
        this.personnum = personnum;
    }

    public String getDinnertime() {
        return dinnertime;
    }

    public void setDinnertime(String dinnertime) {
        this.dinnertime = dinnertime;
    }

    public int getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(int roomtype) {
        this.roomtype = roomtype;
    }

    public int getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(int roomnum) {
        this.roomnum = roomnum;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIsreplaceother() {
        return isreplaceother;
    }

    public void setIsreplaceother(int isreplaceother) {
        this.isreplaceother = isreplaceother;
    }

    public int getOthersex() {
        return othersex;
    }

    public void setOthersex(int othersex) {
        this.othersex = othersex;
    }

    public String getOtherusername() {
        return otherusername;
    }

    public void setOtherusername(String otherusername) {
        this.otherusername = otherusername;
    }

    public String getOthermobile() {
        return othermobile;
    }

    public void setOthermobile(String othermobile) {
        this.othermobile = othermobile;
    }

    public int getIscontainfood() {
        return iscontainfood;
    }

    public void setIscontainfood(int iscontainfood) {
        this.iscontainfood = iscontainfood;
    }

    public List<ReqFood> getFoods() {
        return foods;
    }

    public void setFoods(List<ReqFood> foods) {
        this.foods = foods;
    }
}

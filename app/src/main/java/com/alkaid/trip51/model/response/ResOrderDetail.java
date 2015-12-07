package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class ResOrderDetail extends ResponseData {
    public String orderno;
    public long shopid;
    public String shopname;
    public String shopimgurl;
    public int personnum;
    public int roomtype;
    public String ordertime;
    public String dinnerdate;
    public float orderamount;
    public float foodamount;
    public int sex;
    public String mobile;
    public int isinstead;
    public int othersex;
    public String otherusername;
    public String othermobile;
    public List<Food> foods=new ArrayList<>();
    public int orderstatus;
}

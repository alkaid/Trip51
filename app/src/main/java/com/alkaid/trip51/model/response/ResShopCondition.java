package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.config.TimeSet;
import com.alkaid.trip51.model.shop.Area;
import com.alkaid.trip51.model.shop.Cuisine;
import com.alkaid.trip51.model.shop.ShopCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/12/1.
 */
public class ResShopCondition extends ResponseData{
    private List<TimeSet> timesets=new ArrayList<>();
    private List<ShopCategory> shopcategorylist=new ArrayList<>();
    private List<Area> arealist=new ArrayList<>();
    private List<Cuisine> shopcuisinelist=new ArrayList<>();

    public List<TimeSet> getTimesets() {
        return timesets;
    }

    public void setTimesets(List<TimeSet> timesets) {
        this.timesets = timesets;
    }

    public List<ShopCategory> getShopcategorylist() {
        return shopcategorylist;
    }

    public void setShopcategorylist(List<ShopCategory> shopcategorylist) {
        this.shopcategorylist = shopcategorylist;
    }

    public List<Area> getArealist() {
        return arealist;
    }

    public void setArealist(List<Area> arealist) {
        this.arealist = arealist;
    }

    public List<Cuisine> getShopcuisinelist() {
        return shopcuisinelist;
    }

    public void setShopcuisinelist(List<Cuisine> shopcuisinelist) {
        this.shopcuisinelist = shopcuisinelist;
    }
}

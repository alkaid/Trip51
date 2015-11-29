package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Shop;

import java.util.List;

/**
 * Created by df on 2015/11/20.
 */
public class ResShopList extends  ResponseData {
    protected int total;
    protected List<Shop> data;

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

}

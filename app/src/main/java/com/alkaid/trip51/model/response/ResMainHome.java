package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Area;
import com.alkaid.trip51.model.shop.ShopCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by df on 2015/11/20.
 */
public class ResMainHome extends  ResShopList {
    private String bannerimgurl;
    private List<ShopCategory> shopcategorylist=new ArrayList<>();
    private List<Area> arealist;
}

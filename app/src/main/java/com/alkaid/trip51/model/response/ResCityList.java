package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.SimpleCity;

import java.util.List;

/**
 * Created by df on 2015/11/24.
 */
public class ResCityList extends ResponseData {
    List<SimpleCity> citylist;

    public List<SimpleCity> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<SimpleCity> citylist) {
        this.citylist = citylist;
    }
}

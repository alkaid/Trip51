package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.SimpleCity;

import java.util.List;

/**
 * Created by df on 2015/11/24.
 */
public class ResCityId extends ResponseData {
    long cityid;

    public long getCityid() {
        return cityid;
    }

    public void setCityid(long cityid) {
        this.cityid = cityid;
    }
}

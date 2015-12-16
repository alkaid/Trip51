package com.alkaid.trip51.model;

import java.io.Serializable;

/**
 * Created by df on 2015/11/24.
 */
public class SimpleCity implements Serializable{
    private long cityid;//城市id
    private String cityname;//城市名字
    private String firstLetter;//首字母

    public long getCityid() {
        return cityid;
    }

    public void setCityid(long cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
}

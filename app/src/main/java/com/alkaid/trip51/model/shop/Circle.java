package com.alkaid.trip51.model.shop;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/29.
 */
public class Circle implements Serializable {
//    @Expose(serialize = false)
//    public long areaid;
    private long circleid;
    private String circlename;
    private int circleshopnum;
    /**距离，用于搜索条件，并非服务端接口下发的数据*/
    @Expose(serialize = false)
    public int distance;

    public long getCircleid() {
        return circleid;
    }

    public void setCircleid(long circleid) {
        this.circleid = circleid;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public int getCircleshopnum() {
        return circleshopnum;
    }

    public void setCircleshopnum(int circleshopnum) {
        this.circleshopnum = circleshopnum;
    }
}

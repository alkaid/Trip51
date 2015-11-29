package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/29.
 */
public class Circle implements Serializable {
    private long circleid;
    private String circlename;
    private int circleshopnum;

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

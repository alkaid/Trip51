package com.alkaid.trip51.model.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/29.
 */
public class Area implements Serializable{
    private long areaid;
    private String araename;
    private List<Circle> circlelist=new ArrayList<>();

    public long getAreaid() {
        return areaid;
    }

    public void setAreaid(long areaid) {
        this.areaid = areaid;
    }

    public String getAraename() {
        return araename;
    }

    public void setAraename(String araename) {
        this.araename = araename;
    }

    public List<Circle> getCirclelist() {
        return circlelist;
    }

    public void setCirclelist(List<Circle> circlelist) {
        this.circlelist = circlelist;
    }
}

package com.alkaid.trip51.model.config;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/12/1.
 */
public class TimeSet implements Serializable {
    private int timeid;
    private String timepart;

    public int getTimeid() {
        return timeid;
    }

    public void setTimeid(int timeid) {
        this.timeid = timeid;
    }

    public String getTimepart() {
        return timepart;
    }

    public void setTimepart(String timepart) {
        this.timepart = timepart;
    }
}

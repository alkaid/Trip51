package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Baseinfo;
import com.alkaid.trip51.model.shop.Comment;

import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class ResOrder extends ResponseData {
    private String outtradeno;

    public String getOuttradeno() {
        return outtradeno;
    }

    public void setOuttradeno(String outtradeno) {
        this.outtradeno = outtradeno;
    }
}

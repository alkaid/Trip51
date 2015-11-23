package com.alkaid.trip51.model.account;

import java.io.Serializable;

/**
 * Created by df on 2015/11/23.
 */
public class OpenInfo implements Serializable {
    private String openid;
    private String accesstoken;
    private Long expiresin;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public Long getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(Long expiresin) {
        this.expiresin = expiresin;
    }
}

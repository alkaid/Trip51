package com.alkaid.trip51.model.response;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/19.
 */
public class ResponseData implements Serializable {
    protected boolean success;
    protected int errcode;
    protected String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

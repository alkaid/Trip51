package com.alkaid.trip51.model;

/**
 * Created by df on 2015/11/19.
 */
public class ResponseModel<T> {

    private Status status;
    private T data;

    public static class Status{
        boolean success;
        int errcode;
        String msg;
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
}


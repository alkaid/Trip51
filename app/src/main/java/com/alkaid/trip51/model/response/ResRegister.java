package com.alkaid.trip51.model.response;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/23.
 */
public class ResRegister extends ResponseData{
    private OpenInfo openinfo;
    private Account account;
    public static class OpenInfo implements Serializable{
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
    public static class Account implements Serializable{
        private String memberid;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMemberid() {
            return memberid;
        }

        public void setMemberid(String memberid) {
            this.memberid = memberid;
        }

        private String mobile;
    }

    public OpenInfo getOpeninfo() {
        return openinfo;
    }

    public void setOpeninfo(OpenInfo openinfo) {
        this.openinfo = openinfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

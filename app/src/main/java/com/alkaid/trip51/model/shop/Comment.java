package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/24.
 */
public class Comment implements Serializable {
    private long memberid;
    private String content;
    private String nickname;
    private String commenttime; //评价时间 yyyy-MM-dd hh24:mi:si
    private String imgurl;  //评价图片url，以;间隔区分
    private float commentlevel; //综合评分
    private float tastelevel;   //口味
    private float envlevel;     //环境
    private float servicelevel; //服务
    private float avgFee;//人均消费

    //以下字段是”我的评价“的字段
    private long commentid;
    private long shopid;
    private String shopname;

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public float getCommentlevel() {
        return commentlevel;
    }

    public void setCommentlevel(float commentlevel) {
        this.commentlevel = commentlevel;
    }

    public float getTastelevel() {
        return tastelevel;
    }

    public void setTastelevel(float tastelevel) {
        this.tastelevel = tastelevel;
    }

    public float getEnvlevel() {
        return envlevel;
    }

    public void setEnvlevel(float envlevel) {
        this.envlevel = envlevel;
    }

    public float getServicelevel() {
        return servicelevel;
    }

    public void setServicelevel(float servicelevel) {
        this.servicelevel = servicelevel;
    }

    public long getCommentid() {
        return commentid;
    }

    public void setCommentid(long commentid) {
        this.commentid = commentid;
    }

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public float getAvgFee() {
        return avgFee;
    }

    public void setAvgFee(float avgFee) {
        this.avgFee = avgFee;
    }
}

package com.alkaid.trip51.model.shop;

import java.io.Serializable;

/**
 * Created by alkaid on 2015/11/24.
 */
public class Comment implements Serializable {
    private long memberid;
    private String contnet;
    private String nickname;
    private String commenttime; //评价时间 yyyy-MM-dd hh24:mi:si
    private String imgurl;  //评价图片url，以;间隔区分
    private float commentlevel; //综合评分
    private float tastelevel;   //口味
    private float envlevel;     //环境
    private float servicelevel; //服务

}

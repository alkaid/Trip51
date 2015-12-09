package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Baseinfo;
import com.alkaid.trip51.model.shop.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class ResBaseInfo extends ResponseData {
    private Baseinfo baseinfo;
    private List<Comment> comments=new ArrayList<>();

    public Baseinfo getBaseinfo() {
        return baseinfo;
    }

    public void setBaseinfo(Baseinfo baseinfo) {
        this.baseinfo = baseinfo;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Comment;

import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class ResComments extends ResponseData {
    private List<Comment> data;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}

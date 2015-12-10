package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.shop.Baseinfo;
import com.alkaid.trip51.model.shop.Comment;
import com.alkaid.trip51.model.shop.FoodCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class ResFoodList extends ResponseData {
    private List<FoodCategory> foodcategory=new ArrayList<>();
    private List<Comment> comments=new ArrayList<>();
    private Baseinfo baseinfo;

    public List<FoodCategory> getFoodcategory() {
        return foodcategory;
    }
    public void setFoodcategory(List<FoodCategory> foodcategory) {
        this.foodcategory = foodcategory;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Baseinfo getBaseinfo() {
        return baseinfo;
    }

    public void setBaseinfo(Baseinfo baseinfo) {
        this.baseinfo = baseinfo;
    }
}

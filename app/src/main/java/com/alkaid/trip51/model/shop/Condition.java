package com.alkaid.trip51.model.shop;

/**
 * Created by alkaid on 2015/12/4.
 */
public class Condition {
    public static enum CondType{
        ShopCategory,Sort,Location
    }
    public static class Result{
        public Result(CondType condType,Object data){
            this.condType=condType;
            this.data=data;
        }
        public CondType condType;
        public Object data;
    }
}

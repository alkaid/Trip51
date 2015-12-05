package com.alkaid.trip51.model.shop;

import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.enums.SortType;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by alkaid on 2015/12/4.
 */
public class SearchCondition implements Serializable{

    public Base base;
    public Advance advance;

    public static void putUpHttpSearchRequestParams(Map<String,String> params,SearchCondition searchCondition){
        if(null==searchCondition){
        }else{
            if(null!=searchCondition.base){
                if(null!=searchCondition.base.sortType){
                    params.put("sortid",searchCondition.base.sortType.code);
                }
                if(null!=searchCondition.base.area && searchCondition.base.area.getAreaid()>0){
                    params.put("areaid",searchCondition.base.area.getAreaid()+"");
                }
                if(null!=searchCondition.base.circle){
                    params.put("circleid",searchCondition.base.circle.getCircleid()+"");
                }
                if(null!=searchCondition.base.nearBy){
                    params.put("location",searchCondition.base.nearBy.distance+"");
                }
                if(null!=searchCondition.base.shopType&&searchCondition.base.shopType!=ShopType.ALL){
                    params.put("shoptype",searchCondition.base.shopType.code+"");
                }
                if(null!=searchCondition.base.shopCategory&&searchCondition.base.shopCategory.getCategoryid()>0){
                    params.put("diningtype",searchCondition.base.shopCategory.getCategoryid()+"");
                }
            }
            if(null!=searchCondition.advance){

            }
        }
    }

    /**
     * 基本搜索条件
     */
    public static class Base implements Serializable{
        public SortType sortType;
        public Area area;
        //商圈
        public Circle circle;
        /**附近距离*/
        public NearBy nearBy;
        /**商店大类*/
        public ShopType shopType;
        /**商店细分类型*/
        public ShopCategory shopCategory;
    }

    /**
     * 高级搜索条件
     */
    public static class Advance implements Serializable{
        public String keyword;
        public String orderdate;
        public int timepartid=-1;
        public int seatnum;
        public int cuisineid;
        public int desktype;
    }








    /**搜索数据类型*/
    public static enum CondType{
        ShopCategory,Sort,Location
    }

    /**
     * 用于FilterBar返回用户点击的搜索条件数据
     */
    public static class Result implements Serializable{
        public Result(CondType condType,Object parentData,Object subData){
            this.condType=condType;
            this.parentData=parentData;
            this.subData=subData;
        }
        public CondType condType;
        public Object parentData;
        public Object subData;
    }

    public static class NearBy{
        public int id;
        public int distance;
    }

}

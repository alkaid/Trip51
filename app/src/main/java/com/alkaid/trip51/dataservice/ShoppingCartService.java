package com.alkaid.trip51.dataservice;

import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.alkaid.trip51.model.shop.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jyz on 2015/12/05.
 */
public class ShoppingCartService {
    private static ShoppingCartService instance;
    private Context context;
    /**
     * 购物车显示的食物
     */
    private LongSparseArray<List<Food>> cartFoods;

    private ShoppingCartService(Context context) {
        this.context = context;
    }

    public static ShoppingCartService create(Context context) {
        instance = new ShoppingCartService(context);
        instance.init();
        return instance;
    }

    private void init() {
        cartFoods = new LongSparseArray<>();
    }

    public LongSparseArray<List<Food>> getCart() {
        return cartFoods;
    }

    /**
     * 更新购物车
     *
     * @param shopId
     * @param food
     */
    public void updateFoodToCart(long shopId, Food food) {
        List<Food> foods = cartFoods.get(shopId);
        if (cartFoods != null) {
            if (foods == null) {
                foods = new ArrayList<Food>();
                foods.add(food);
                cartFoods.put(shopId, foods);
            } else {
                boolean isContainFood = false;
                for (Food f : foods) {
                    if (f.getFoodid() == food.getFoodid()) {
                        f = food;
                        isContainFood = true;
                        break;
                    }
                }
                if (!isContainFood) {
                    foods.add(food);
                }
            }
        }
    }

    //TODO 金额和总数需要改成update时就计算好缓存起来
    /**
     * 获取单个店铺购物车总价
     * @param shopId 店铺id
     * @return
     */
    public float getCartTotalPrice(long shopId){
        float total = 0;
        List<Food> foods = cartFoods.get(shopId);
        if(null==foods)
            return total;
        for(Food f:foods){
            total+=(f.getPrice()*f.getFoodNum());
        }
        return total;
    }

    /**
     * 获取购物车数量
     * @param shopId 店铺id
     * @return
     */
    public int getCartFoodNum(long shopId){
        int total=0;
        if(cartFoods!=null){
            List<Food> foods = cartFoods.get(shopId);
            if(foods!=null){
                for(Food f:foods){
                    total+=f.getFoodNum();
                }
            }
        }
        return total;
    }

    /**
     * 移除购物车根据店铺id
     * @param shopId
     */
    public void clearCartFood(long shopId){
        if(cartFoods!=null){
            cartFoods.remove(shopId);
        }
    }

}

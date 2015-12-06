package com.alkaid.trip51.dataservice;

import android.content.Context;

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
    private Map<Long,ArrayList<Food>> cartFoods;

    private ShoppingCartService(Context context) {
        this.context = context;
    }

    public static ShoppingCartService create(Context context) {
        instance = new ShoppingCartService(context);
        instance.init();
        return instance;
    }

    private void init() {
        cartFoods =new HashMap<Long,ArrayList<Food>>();
    }

    public  Map<Long,ArrayList<Food>> getCart() {
        return cartFoods;
    }

    /**
     * 更新购物车
     * @param shopId
     * @param food
     */
    public void updateFoodToCart(long shopId,Food food){
        if(cartFoods!=null){
            if(cartFoods.get(shopId)==null){
                List<Food> foods = new ArrayList<Food>();
                foods.add(food);
                cartFoods.put(shopId, (ArrayList<Food>) foods);
            }else{
                for(Food f:cartFoods.get(shopId)){
                    if(f.getFoodid() == food.getFoodid()){
                        f = food;
                    }
                }
            }
        }
    }

}

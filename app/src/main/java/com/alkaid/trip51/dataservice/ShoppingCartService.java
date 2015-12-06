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
    private Map<Long,List<Food>> cartFoods;

    private ShoppingCartService(Context context) {
        this.context = context;
    }

    public static ShoppingCartService create(Context context) {
        instance = new ShoppingCartService(context);
        instance.init();
        return instance;
    }

    private void init() {
        cartFoods =new HashMap<Long,List<Food>>();
    }

    public  Map<Long,List<Food>> getCart() {
        return cartFoods;
    }

    /**
     * 更新购物车
     * @param shopId
     * @param food
     */
    public void updateFoodToCart(long shopId,Food food){
        List<Food> foods=cartFoods.get(shopId);
        if(cartFoods!=null){
            if(foods==null){
                foods = new ArrayList<Food>();
                foods.add(food);
                cartFoods.put(shopId, foods);
            }else{
                boolean isContainFood=false;
                for(Food f:foods){
                    if(f.getFoodid() == food.getFoodid()){
                        f = food;
                        isContainFood=true;
                        break;
                    }
                }
                foods.add(food);
            }
        }
    }

}

package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.FoodCategory;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.widget.Operator;

import java.util.List;

public class ShoppingCartListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;

    private List<Food> foods;//当前店铺的菜单列表
    private List<FoodCategory> foodCategories;//当前店铺的菜单列表
    private Shop currShop;


    public ShoppingCartListAdapter(Context context, Shop currShop) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.currShop = currShop;
    }

    /**
     * 菜单的数据在购物车
     *
     * @param foodCategories
     */
    public void setCartData(List<FoodCategory> foodCategories) {
        this.foodCategories = foodCategories;
        foods = App.shoppingCartService().getCart().get(currShop.getShopid());
    }

    @Override
    public int getCount() {
        if (foods == null) {
            return 0;
        } else {
            return foods.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(foods!=null){
            return foods.get(position);
        }else{
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shopping_cart_list, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tvFoodName = (TextView) convertView.findViewById(R.id.tv_food_name);
            holder.tvFoodPrice = (TextView) convertView.findViewById(R.id.tv_food_price);
            holder.opFoodNum = (Operator) convertView.findViewById(R.id.op_food_num);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        if (foods != null) {
            holder.tvFoodName.setText(foods.get(position).getFoodname());
            holder.tvFoodPrice.setText("￥" + foods.get(position).getPrice());
            holder.opFoodNum.setOperationCallback(new Operator.OperationListener() {
                @Override
                public void onAddClick(int i) {
                    updateFoodCategories(foods.get(position), i);
                }

                @Override
                public void onSubClick(int i) {
                    updateFoodCategories(foods.get(position), i);
                }

                @Override
                public void onTextChange(int i) {

                }
            });
        }
        return convertView;
    }

    /**
     * 更新菜单的数据和购物车的数据，根据食物id
     *
     * @param food    需要改变的食物的id
     * @param foodNum 需要改变的食物的数量
     */
    public void updateFoodCategories(Food food, int foodNum) {
        if (foodCategories != null) {
            for (int i = 0; i < foodCategories.size(); i++) {
                List<Food> foods = foodCategories.get(i).getFoods();
                for (int j = 0; j < foods.size(); j++) {
                    if (foods.get(j).getFoodid() == food.getFoodid()) {
                        foodCategories.get(i).getFoods().get(j).setFoodNum(foodNum);
                    }
                }
            }
            App.shoppingCartService().updateFoodToCart(currShop.getShopid(), food);
        }
    }

    private class ViewHolder {
        public TextView tvFoodName, tvFoodPrice;
        public Operator opFoodNum;
    }

}

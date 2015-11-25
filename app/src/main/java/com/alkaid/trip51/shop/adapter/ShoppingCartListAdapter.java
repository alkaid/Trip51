package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.widget.Operator;

import java.util.ArrayList;

public class ShoppingCartListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public ShoppingCartListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shopping_cart_list, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tvFoodName = (TextView) convertView.findViewById(R.id.tv_food_name);
            holder.tvFoodPrice = (TextView) convertView.findViewById(R.id.tv_food_price);
            holder.opFoodNum=(Operator)convertView.findViewById(R.id.op_food_num);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        holder.tvFoodName.setText("清蒸鲈鱼");
        holder.tvFoodPrice.setText("￥32.8");
        return convertView;
    }

    private class ViewHolder{
        public TextView tvFoodName,tvFoodPrice;
        public Operator opFoodNum;
    }
}

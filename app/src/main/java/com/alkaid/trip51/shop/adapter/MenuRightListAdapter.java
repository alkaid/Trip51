package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.FoodCategory;
import com.alkaid.trip51.util.BitmapUtil;
import com.alkaid.trip51.widget.pinnedheaderlistview.SectionedBaseAdapter;

import java.util.List;

public class MenuRightListAdapter extends SectionedBaseAdapter {

    private Context mContext;

    private String TAG = getClass().getName();

    private List<FoodCategory> foodCategories;

    public MenuRightListAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 赋数据
     *
     * @param foodCategories 设置的数据
     */
    public void setMenuListData(List<FoodCategory> foodCategories) {
        this.foodCategories = foodCategories;
    }

    @Override
    public Object getItem(int section, int position) {
        if (foodCategories != null) {
            return foodCategories.get(section).getFoods().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        if (foodCategories == null) {
            return 0;
        }
        return foodCategories.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (foodCategories != null) {
            return foodCategories.get(section).getFoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Food food = (Food) getItem(section, position);
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.menu_list_content_item, null);
            holder = new ViewHolder();
            holder.ivFood = (ImageView) convertView.findViewById(R.id.iv_menu_food);
            holder.tvNameFood = (TextView) convertView.findViewById(R.id.tv_menu_food_name);
            holder.tvCountFood = (TextView) convertView.findViewById(R.id.tv_menu_food_count);
            holder.tvPriceFood = (TextView) convertView.findViewById(R.id.tv_menu_food_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (food != null) {
            if (food.getFoodimg() != null) {
                holder.ivFood.setImageBitmap(BitmapUtil.getHttpBitmap(food.getFoodimg()));
            } else {
                holder.ivFood.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.temp_shop_thumb));
            }
            holder.tvNameFood.setText(food.getFoodname());
            holder.tvCountFood.setText("已售:" + food.getSales() + "份");
            holder.tvPriceFood.setText(food.getPrice() + "元");
            LogUtil.d(TAG, "Section " + section + " Item " + position);
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.menu_list_section_head, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        TextView tv = (TextView) layout.findViewById(R.id.textItem);
        if (foodCategories != null && foodCategories.get(section) != null) {
            tv.setText(foodCategories.get(section).getCategoryname());
        }
        layout.setClickable(false);
//        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        LogUtil.d(TAG, "Header for section " + section);
        return layout;
    }


    private class ViewHolder {
        ImageView ivFood;
        TextView tvNameFood;
        TextView tvCountFood;
        TextView tvPriceFood;
    }

}

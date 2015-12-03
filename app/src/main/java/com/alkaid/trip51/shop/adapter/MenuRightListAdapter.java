package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.shop.model.MenuItemModel;
import com.alkaid.trip51.widget.pinnedheaderlistview.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MenuRightListAdapter extends SectionedBaseAdapter {

	private Context mContext;

    private String TAG = getClass().getName();

    private ArrayList<MenuItemModel> menus;
    private String[] leftNavigations;

	public MenuRightListAdapter(Context context){
		this.mContext = context;
	}

    /**
     * 根据section和position获得菜单项
     * @param section
     * @param position
     * @return
     */
    private MenuItemModel getMenuItemModelBySectionPosition(int section,int position){
        if(menus == null){
            return null;
        }
        for(MenuItemModel menuItemModel:menus){
            if(menuItemModel.getSectionNum() == section&&menuItemModel.getPositionNum() == position){
                return menuItemModel;
            }
        }
        return null;
    }

    public int getCountBySection(int section) {
        int count = 0;
        for(MenuItemModel model:menus){
            if(model.getSectionNum() == section){
                count++;
            }
        }
        return count;
    }

    public void setMenuListData(String[] leftNavigations,ArrayList<MenuItemModel> menus){
        this.menus = menus;
        this.leftNavigations = leftNavigations;
    }

    @Override
    public Object getItem(int section, int position) {
        return getMenuItemModelBySectionPosition(section,position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        if(leftNavigations == null){
            return 0;
        }
        return leftNavigations.length;
    }

    @Override
    public int getCountForSection(int section) {
        return getCountBySection(section);
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MenuItemModel item = (MenuItemModel) getItem(section,position);
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.menu_list_content_item,null);
            holder = new ViewHolder();
            holder.ivFood = (ImageView) convertView.findViewById(R.id.iv_menu_food);
            holder.tvNameFood = (TextView) convertView.findViewById(R.id.tv_menu_food_name);
            holder.tvCountFood = (TextView) convertView.findViewById(R.id.tv_menu_food_count);
            holder.tvPriceFood = (TextView) convertView.findViewById(R.id.tv_menu_food_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivFood.setImageBitmap(item.getIcon());
        holder.tvNameFood.setText(item.getName());
        holder.tvCountFood.setText("已售:"+item.getSold_num()+"份");
        holder.tvPriceFood.setText(item.getPrice()+"元");
        LogUtil.d(TAG, "Section " + section + " Item " + position);
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
        if(leftNavigations!=null) {
            tv.setText(leftNavigations[section]);
        }
        layout.setClickable(false);
//        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        LogUtil.d(TAG,"Header for section " + section);
        return layout;
    }


    private class ViewHolder {
        ImageView ivFood;
        TextView tvNameFood;
        TextView tvCountFood;
        TextView tvPriceFood;
    }

}

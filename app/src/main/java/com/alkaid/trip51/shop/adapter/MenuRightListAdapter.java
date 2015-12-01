package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alkaid.trip51.R;
import com.alkaid.trip51.shop.model.MenuItemModel;
import com.alkaid.trip51.widget.pinnedheaderlistview.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MenuRightListAdapter extends SectionedBaseAdapter {

	private Context mContext;

    private ArrayList<MenuItemModel> menus;
    private ArrayList<String> leftNavigations;

	public MenuRightListAdapter(Context context){
		this.mContext = context;
	}

    /**
     * 根据section和position获得菜单项
     * @param section
     * @param position
     * @return
     */
    public MenuItemModel getMenuItemModelBySectionPosition(int section,int position){
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


    public void setMenuListData(ArrayList<String> leftNavigations,ArrayList<MenuItemModel> menus){
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
        return leftNavigations.size();
    }

    @Override
    public int getCountForSection(int section) {
        return 0;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.linked_list_right_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
//        ((TextView) layout.findViewById(R.id.textItem)).setText(rightStr[section][position]);
//        layout.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(mContext, rightStr[section][position], Toast.LENGTH_SHORT).show();
//			}
//		});
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.linked_list_section_head, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
//        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        return layout;
    }

}

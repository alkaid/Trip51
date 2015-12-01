package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alipay.sdk.app.m;
import com.alkaid.trip51.R;
import com.alkaid.trip51.model.shop.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jyz on 2015/12/1.
 */
public class MenuLeftListAdapter extends BaseAdapter {

    private String[] mNavigationName;

    private Context mContext;
    private LayoutInflater mInflater;

    public MenuLeftListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setNavigationData(String[] mNavigationName){
        this.mNavigationName = mNavigationName;
    }

    @Override
    public int getCount() {
        if (mNavigationName != null) {
            return mNavigationName.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mNavigationName != null) {
            return mNavigationName[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvNavigation = (TextView) mInflater.inflate(R.layout.menu_list_navagation_item, null);
        tvNavigation.setText(mNavigationName[position]);
        if (position == 0) {
            tvNavigation.setBackgroundColor(Color.WHITE);
        } else {
            tvNavigation.setBackgroundResource(R.color.menu_navigator_background_color);
        }
        return tvNavigation;
    }
}

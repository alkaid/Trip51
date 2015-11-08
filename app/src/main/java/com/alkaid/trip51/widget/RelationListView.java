package com.alkaid.trip51.widget;

/**
 * 自定义控件，联动listview，左边listview导航右边listview
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jyz on 2015/11/8.
 */
public class RelationListView extends LinearLayout {

    //左侧导航列表
    private ListView navigationListView;
    //右侧内容列表
    private ListView contentListView;

    private TextView tv_content_titile;

    private List<String> mNavigationData;

    //选中的导航项
    private int index;

    //内容列表，传递显示的ui
    private HashMap<Integer, ArrayList<String>> mContenttData;

    private NavigationListAdapter navigationAdapter;

    private ContentListAdapter contentAdapter;

    private Context mContext;

    public RelationListView(Context context) {
        this(context, null);
    }

    public RelationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.menu_relation_listview, this, true);
        tv_content_titile = (TextView) findViewById(R.id.tv_content_title);
        navigationListView = (ListView) view.findViewById(R.id.navigation_list_id);
        contentListView = (ListView) view.findViewById(R.id.content_list_id);
        navigationAdapter = new NavigationListAdapter();
        navigationListView.setAdapter(navigationAdapter);
        contentAdapter = new ContentListAdapter();
        contentListView.setAdapter(contentAdapter);

        navigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                tv_content_titile.setText(mNavigationData.get(position));
                navigationAdapter.notifyDataSetChanged();
            }
        });

        //制造假数据
        ArrayList<String> navigas = new ArrayList<>();
        navigas.add("炒菜类");
        navigas.add("凉菜类");
        navigas.add("海鲜类");
        navigas.add("小吃类");

        HashMap<Integer, ArrayList<String>> content = new HashMap<>();
        ArrayList<String> data1 = new ArrayList<>();
        data1.add(1 + "");
        data1.add(1 + "");
        data1.add(1 + "");
        ArrayList<String> data2 = new ArrayList<>();
        data2.add(1 + "");
        data2.add(1 + "");
        data2.add(1 + "");
        ArrayList<String> data3 = new ArrayList<>();
        data3.add(1 + "");
        data3.add(1 + "");
        data3.add(1 + "");
        content.put(0, data1);
        content.put(1, data2);
        content.put(2, data3);

        setListViewData(navigas, content);
    }

    /**
     * 设置自定义控件左侧listview数据
     *
     * @param navigatonData
     */
    public void setListViewData(List<String> navigatonData, HashMap<Integer, ArrayList<String>> contentData) {
        mNavigationData = navigatonData;
        mContenttData = contentData;
        if (navigationListView != null && navigatonData != null) {
            tv_content_titile.setText(mNavigationData.get(index));
            navigationAdapter.notifyDataSetChanged();
        }
        if (contentListView != null && contentData != null) {
            contentAdapter.notifyDataSetChanged();
        }
    }

    private class NavigationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            ;
            if (mNavigationData != null) {
                return mNavigationData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mNavigationData != null) {
                return mNavigationData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) inflate(mContext, R.layout.menu_list_navagation_item, null);
            tv.setText(mNavigationData.get(position));
            if (position == index) {
                tv.setBackgroundColor(Color.WHITE);
            } else {
                tv.setBackgroundResource(R.color.menu_navigator_background_color);
            }
            return tv;
        }
    }

    private class ContentListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mContenttData != null && mContenttData.size() > 0) {
                return mContenttData.get(index).size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mContenttData != null && mContenttData.size() > 0) {
                return mContenttData.get(index);
            }
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
                convertView = inflate(mContext, R.layout.menu_list_content_item, null);
                holder = new ViewHolder();
                holder.ivFood = (ImageView) convertView.findViewById(R.id.iv_menu_food);
                holder.tvNameFood = (TextView) convertView.findViewById(R.id.tv_menu_food_name);
                holder.tvCountFood = (TextView) convertView.findViewById(R.id.tv_menu_food_count);
                holder.tvPriceFood = (TextView) convertView.findViewById(R.id.tv_menu_food_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //测试数据
            holder.ivFood.setImageResource(R.drawable.temp_shop_thumb);
            holder.tvNameFood.setText("清蒸鲈鱼");
            holder.tvCountFood.setText("已售:200");
            holder.tvPriceFood.setText("32.8元");

            return convertView;
        }
    }

    private class ViewHolder {
        ImageView ivFood;
        TextView tvNameFood;
        TextView tvCountFood;
        TextView tvPriceFood;
    }
}

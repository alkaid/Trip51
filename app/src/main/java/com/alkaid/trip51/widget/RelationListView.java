package com.alkaid.trip51.widget;

/**
 * 自定义控件，联动listview，左边listview导航右边listview
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jyz on 2015/11/8.
 */
public class RelationListView extends LinearLayout {

    //左侧导航列表
    private ListView navigationListView;
    //右侧内容列表
    private ListView contentListView;

    private List<String> mNavigationData;

    //内容列表，传递显示的ui
    private List<View> mContenttData;

    //选中项
    private int index;

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
        navigationListView = (ListView) view.findViewById(R.id.navigation_list_id);
        contentListView = (ListView) view.findViewById(R.id.content_list_id);
        navigationAdapter = new NavigationListAdapter();
        navigationListView.setAdapter(navigationAdapter);
        contentAdapter = new ContentListAdapter();
        contentListView.setAdapter(contentAdapter);


        //制造假数据
        ArrayList<String> navigas = new ArrayList<>();
        navigas.add("炒菜类");
        navigas.add("凉菜类");
        navigas.add("海鲜类");
        navigas.add("炒菜类");

        setListViewData(navigas, 1, null);
    }

    /**
     * 设置自定义控件左侧listview数据
     *
     * @param navigatonData
     */
    public void setListViewData(List<String> navigatonData, int index, List<View> contentData) {
        mNavigationData = navigatonData;
        mContenttData = contentData;
        index = index;
        if (navigationListView != null && navigatonData != null) {
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

            return tv;
        }
    }

    private class ContentListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mContenttData != null) {
                return mContenttData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mContenttData != null) {
                return mContenttData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mContenttData != null) {
                return mContenttData.get(position);
            }
            return null;
        }
    }
}

package com.alkaid.trip51.widget.linkedlistview;

/**
 * 自定义控件，联动listview，左边listview导航右边listview
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.m;
import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.widget.pinnedheaderlistview.PinnedHeaderListView;
import com.alkaid.trip51.widget.pinnedheaderlistview.SectionedBaseAdapter;

import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jyz on 2015/11/8.
 */
public class LinkedListView extends LinearLayout {

    private String TAG = getClass().getName();

    private Context mContext;
    private PinnedHeaderListView rightListView;
    private ListView leftListView;

    private BaseAdapter leftListAdapter;

    private SectionedBaseAdapter rightListAdapter;

    private boolean isScroll = true;

    public LinkedListView(Context context) {
        this(context, null);
    }

    public LinkedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.linked_list_view, this, true);
        rightListView = (PinnedHeaderListView) view.findViewById(R.id.pinnedListView);
        leftListView = (ListView) view.findViewById(R.id.left_listview);
        leftListView.setAdapter(leftListAdapter);
        rightListView.setAdapter(rightListAdapter);

        leftListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                isScroll = false;
                //设置左边点击背景色已经右边section对应
                for (int i = 0; i < leftListView.getChildCount(); i++) {
                    if (i == position) {
                        leftListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
                    } else {
                        leftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += rightListAdapter.getCountForSection(i) + 1;
                }
                rightListView.setSelection(rightSection);
            }

        });

        rightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //设置左边点击背景色已经右边section对应
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < leftListView.getChildCount(); i++) {

                        if (i == rightListAdapter.getSectionForPosition(firstVisibleItem)) {
                            leftListView.getChildAt(i).setBackgroundColor(
                                    Color.rgb(255, 255, 255));
                        } else {
                            leftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                        }
                    }

                } else {
                    isScroll = true;
                }
            }
        });
    }

    /**
     * 设置adapter
     *
     * @param leftListAdapter
     * @param rightListAdapter
     */
    public void setAdapter(BaseAdapter leftListAdapter, SectionedBaseAdapter rightListAdapter) {
        this.leftListAdapter = leftListAdapter;
        this.rightListAdapter = rightListAdapter;
        leftListView.setAdapter(leftListAdapter);
        rightListView.setAdapter(rightListAdapter);
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (leftListAdapter == null || rightListAdapter == null) {
            LogUtil.d(TAG, "adapter为null");
        } else {
            leftListAdapter.notifyDataSetChanged();
            rightListAdapter.notifyDataSetChanged();
        }
    }

}

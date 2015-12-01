package com.alkaid.trip51.widget.linkedlistview;

/**
 * 自定义控件，联动listview，左边listview导航右边listview
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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
import com.alkaid.trip51.R;
import com.alkaid.trip51.widget.pinnedheaderlistview.PinnedHeaderListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jyz on 2015/11/8.
 */
public class LinkedListView extends LinearLayout {


    private String[] leftStr = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private String[][] rightStr = new String[][]{
            {"星期一  早餐","星期一  午餐","星期一  晚餐"}, {"星期二  早餐","星期二  午餐","星期二  晚餐"},
            {"星期三  早餐","星期三  午餐","星期三  晚餐"}, {"星期四  早餐","星期四  午餐","星期四  晚餐"},
            {"星期五  早餐","星期五  午餐","星期五  晚餐"}, {"星期六  早餐","星期六  午餐","星期六  晚餐"},
            {"星期日  早餐","星期日  午餐","星期日  晚餐"}};

    private Context mContext;

    private boolean isScroll = true;

    public LinkedListView(Context context) {
        this(context, null);
    }

    public LinkedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.linked_list_view, this, true);
        final PinnedHeaderListView rightListView = (PinnedHeaderListView) view.findViewById(R.id.pinnedListView);
        final ListView leftListView = (ListView) view.findViewById(R.id.left_listview);

        final SectionedAdapter sectionedAdapter = new SectionedAdapter(mContext, leftStr, rightStr);
        rightListView.setAdapter(sectionedAdapter);

        leftListView.setAdapter(new ArrayAdapter<String>(mContext,
                android.R.layout.simple_expandable_list_item_1, leftStr));

        leftListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                isScroll = false;

                for (int i = 0; i < leftListView.getChildCount(); i++)
                {
                    if (i == position)
                    {
                        leftListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
                    } else
                    {
                        leftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                int rightSection = 0;
                for(int i=0;i<position;i++){
                    rightSection += sectionedAdapter.getCountForSection(i)+1;
                }
                rightListView.setSelection(rightSection);

            }

        });

        rightListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(isScroll){
                    for (int i = 0; i < leftListView.getChildCount(); i++)
                    {

                        if (i == sectionedAdapter.getSectionForPosition(firstVisibleItem))
                        {
                            leftListView.getChildAt(i).setBackgroundColor(
                                    Color.rgb(255, 255, 255));
                        } else
                        {
                            leftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                        }
                    }

                }else{
                    isScroll = true;
                }
            }
        });

    }


}

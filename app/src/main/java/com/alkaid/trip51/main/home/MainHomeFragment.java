package com.alkaid.trip51.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import pw.h57.popupbuttonlibrary.PopupButton;
import pw.h57.popupbuttonlibrary.adapter.PopupAdapter;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MainHomeFragment extends BaseFragment {
    private View slideshowView,layMainMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.main_home_fragment,container,false);
        ViewGroup layBar = (ViewGroup) v.findViewById(R.id.title_bar_layout);
        View titleBar=inflater.inflate(R.layout.main_home_title_bar,layBar,true);

        slideshowView=v.findViewById(R.id.slideshowView);
        layMainMenu=v.findViewById(R.id.layMainMenu);

        ListView shopListView= (ListView) v.findViewById(android.R.id.list);
        shopListView.setAdapter(new ShopListAdapter());
        shopListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mListViewFirstItem = 0;
            //listView中第一项的在屏幕中的位置
            private int mScreenY = 0;
            //是否向上滚动
            private boolean mIsScrollToUp = false;
            @Override
            public void onScrollStateChanged(AbsListView v, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if(v.getFirstVisiblePosition() == 0){
                            slideshowView.setVisibility(View.VISIBLE);
                            layMainMenu.setVisibility(View.VISIBLE);
                        }else{
                            slideshowView.setVisibility(View.GONE);
                            layMainMenu.setVisibility(View.GONE);
                        }
                }
            }

            @Override
            public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //TODO 以下是测试用的筛选 要删除
        View view2 = inflater.inflate(R.layout.popup2,null);
        ListView pLv = (ListView) view2.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) view2.findViewById(R.id.child_lv);
        List<String> pList = new ArrayList<>();
        final List<List<String>> cList = new ArrayList<>();
        for(int i = 0; i < 10; i ++) {
            pList.add("p" + i);
            List<String> t = new ArrayList<>();
            for(int j = 0; j < 15; j++) {
                t.add(pList.get(i) + "-c" + j);
            }
            cList.add(t);
        }
        final List cValues = new ArrayList<>();
        cValues.addAll(cList.get(0));
        final PopupAdapter pAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,pList,R.color.base_start_color_default,R.color.base_start_color_pressed);
        final PopupAdapter cAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,pList,R.color.base_start_color_default,R.color.base_start_color_pressed);
        pAdapter.setPressPostion(0);
        pLv.setAdapter(pAdapter);
        cLv.setAdapter(cAdapter);
        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pAdapter.setPressPostion(position);
                pAdapter.notifyDataSetChanged();
                cValues.clear();
                cValues.addAll(cList.get(position));
                cAdapter.notifyDataSetChanged();
                cAdapter.setPressPostion(-1);
                cLv.setSelection(0);
            }
        });
        final PopupButton btn1 = (PopupButton) v.findViewById(R.id.btn);
        final PopupButton btn2 = (PopupButton) v.findViewById(R.id.btn2);
        final PopupButton btn3 = (PopupButton) v.findViewById(R.id.btn3);
        btn1.setPopupView(view2);
//        btn2.setPopupView(view2);
//        btn3.setPopupView(view2);
        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPostion(position);
                cAdapter.notifyDataSetChanged();
                btn2.setText(cValues.get(position) + "");
                btn2.hidePopup();
            }
        });

        return v;
    }

    private class ShopListAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        public ShopListAdapter(){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.main_home_list_item,null);
//                holder = new ViewHolder();
//                /**得到各个控件的对象*/
//                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
//                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
//                holder.bt = (Button) convertView.findViewById(R.id.ItemButton);
//                convertView.setTag(holder);//绑定ViewHolder对象
            } else{
//                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            return convertView;
        }
    }
}

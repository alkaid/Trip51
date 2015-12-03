package com.alkaid.trip51.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.enums.SortType;
import com.alkaid.trip51.model.response.ResShopCondition;
import com.alkaid.trip51.model.shop.Area;
import com.alkaid.trip51.model.shop.Circle;
import com.alkaid.trip51.model.shop.Condition;
import com.alkaid.trip51.model.shop.ShopCategory;

import java.util.ArrayList;
import java.util.List;

import pw.h57.popupbuttonlibrary.PopupButton;
import pw.h57.popupbuttonlibrary.adapter.PopupAdapter;

/**
 * Created by df on 2015/12/3.
 */
public class DefaultFilter {
    private PopupButton btn1,btn2,btn3;
    private OnItemClickListener onItemClickListener;
    public DefaultFilter(OnItemClickListener onItemClickListener,LayoutInflater inflater,Context context,View v){
        this.onItemClickListener=onItemClickListener;
        init(inflater,context,v);
    }
    public void init(LayoutInflater inflater,Context context,View v){
        btn1 = (PopupButton) v.findViewById(R.id.popbtn1);
        btn2 = (PopupButton) v.findViewById(R.id.popbtn2);
        btn3 = (PopupButton) v.findViewById(R.id.popbtn3);
        btn1.setText("商家分类");
        btn2.setText("排序");
        btn3.setText("附近(位置)");

    }

    private void initBtn1(LayoutInflater inflater,Context context){
        ResShopCondition condition = App.instance().locationService().getCondition();
        View popup = inflater.inflate(R.layout.popup2, null);
        ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) popup.findViewById(R.id.child_lv);

        ShopType[] shopTypes=ShopType.values();
        final List<List<ShopCategory>> cList=new ArrayList<>();
        List<ShopCategory> foodShops=new ArrayList<>();
        ShopCategory category=new ShopCategory();
        category.setCategoryid(0);
        category.setCategoryname("全部餐厅");
        foodShops.add(category);
        foodShops.addAll(condition.getShopcategorylist());
        cList.add(foodShops);
        cList.add(new ArrayList<ShopCategory>());
        cList.add(new ArrayList<ShopCategory>());

        final List<ShopCategory> cValues = new ArrayList<>();
        cValues.addAll(cList.get(0));
        final PopupAdapter pAdapter = new PopupAdapter<ShopType>(context, R.layout.popup_item, shopTypes, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected String getItemLabel(int position) {
                return getItem(position).desc;
            }
        };
        final PopupAdapter cAdapter = new PopupAdapter<ShopCategory>(context, R.layout.popup_item, cValues, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected String getItemLabel(int position) {
                return getItem(position).getCategoryname();
            }
        };
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

        btn1.setPopupView(popup);
//        btn2.setPopupView(view2);
//        btn3.setPopupView(view2);
        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPostion(position);
                cAdapter.notifyDataSetChanged();
                btn1.setText(cValues.get(position).getCategoryname());
                btn1.hidePopup();
                onItemClickListener.onClick(new Condition.Result(Condition.CondType.Location, cValues.get(position)));
            }
        });
    }

    private void initBtn2(LayoutInflater inflater,Context context){
        View popup = inflater.inflate(R.layout.popup1, null);
        ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);

        final SortType[] sortTypes=SortType.values();

        final PopupAdapter pAdapter = new PopupAdapter<SortType>(context, R.layout.popup_item, sortTypes, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected String getItemLabel(int position) {
                return getItem(position).desc;
            }
        };
        pAdapter.setPressPostion(0);
        pLv.setAdapter(pAdapter);
        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pAdapter.setPressPostion(position);
                pAdapter.notifyDataSetChanged();
                btn2.setText(sortTypes[position].desc);
                btn2.hidePopup();
                onItemClickListener.onClick(new Condition.Result(Condition.CondType.Location, sortTypes[position]));
            }
        });

        btn2.setPopupView(popup);
    }

    private void initBtn3(LayoutInflater inflater,Context context){
        ResShopCondition condition = App.instance().locationService().getCondition();
        View popup = inflater.inflate(R.layout.popup2, null);
        ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) popup.findViewById(R.id.child_lv);

        Area area=new Area();
        area.setAreaid(0);
        area.setAraename("附近");
        Circle distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("附近（智能范围）");
        distance.distance=2000;
        area.setCirclelist(new ArrayList<Circle>());
        area.getCirclelist().add(distance);
        distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("1000m");
        distance.distance=2000;
        area.setCirclelist(new ArrayList<Circle>());
        area.getCirclelist().add(distance);

        ShopType[] shopTypes=ShopType.values();
        final List<List<ShopCategory>> cList=new ArrayList<>();
        cList.add(condition.getShopcategorylist());
        cList.add(new ArrayList<ShopCategory>());
        cList.add(new ArrayList<ShopCategory>());

//        final List<Circle> cValues = new ArrayList<>();
//        cValues.addAll(cList.get(0));
//        final PopupAdapter pAdapter = new PopupAdapter<ShopType>(context, R.layout.popup_item, shopTypes, R.color.base_start_color_default, R.color.base_start_color_pressed) {
//            @Override
//            protected String getItemLabel(int position) {
//                return getItem(position).desc;
//            }
//        };
//        final PopupAdapter cAdapter = new PopupAdapter<ShopCategory>(context, R.layout.popup_item, cValues, R.color.base_start_color_default, R.color.base_start_color_pressed) {
//            @Override
//            protected String getItemLabel(int position) {
//                return getItem(position).getCategoryname();
//            }
//        };
//        pAdapter.setPressPostion(0);
//        pLv.setAdapter(pAdapter);
//        cLv.setAdapter(cAdapter);
//        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pAdapter.setPressPostion(position);
//                pAdapter.notifyDataSetChanged();
//                cValues.clear();
//                cValues.addAll(cList.get(position));
//                cAdapter.notifyDataSetChanged();
//                cAdapter.setPressPostion(-1);
//                cLv.setSelection(0);
//            }
//        });
//
//        btn1.setPopupView(popup);
//        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                cAdapter.setPressPostion(position);
//                cAdapter.notifyDataSetChanged();
//                btn1.setText(cValues.get(position).getCategoryname());
//                btn1.hidePopup();
//                onItemClickListener.onClick(new Condition.Result(Condition.CondType.Location, cValues.get(position)));
//            }
//        });
    }

    public static interface OnItemClickListener{
        public void onClick(Condition.Result result);
    }
}

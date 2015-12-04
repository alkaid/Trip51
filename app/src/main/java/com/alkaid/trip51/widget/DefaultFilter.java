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
import com.alkaid.trip51.model.shop.SearchCondition;
import com.alkaid.trip51.model.shop.ShopCategory;

import java.util.ArrayList;
import java.util.List;

import pw.h57.popupbuttonlibrary.PopupButton;
import pw.h57.popupbuttonlibrary.adapter.PopupAdapter;

/**
 * Created by df on 2015/12/3.
 */
public class DefaultFilter {
    private PopupButton btnType, btnSort, btnLocation;
    private OnItemClickListener onItemClickListener;
    public DefaultFilter(LayoutInflater inflater,Context context,View v,OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
        init(inflater, context, v);
    }
    public void init(LayoutInflater inflater,Context context,View v){
        btnType = (PopupButton) v.findViewById(R.id.popbtn1);
        btnSort = (PopupButton) v.findViewById(R.id.popbtn2);
        btnLocation = (PopupButton) v.findViewById(R.id.popbtn3);
        btnType.setText("商家分类");
        btnSort.setText("排序");
        btnLocation.setText("附近(位置)");
        initBtnType(inflater, context);
        initBtnSort(inflater, context);
        initBtnLocation(inflater, context);
    }

    private void initBtnType(LayoutInflater inflater, Context context){
        ResShopCondition condition = App.instance().locationService().getCondition();
        View popup = inflater.inflate(R.layout.popup2, null);
        final ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) popup.findViewById(R.id.child_lv);

        final ShopType[] shopTypes=ShopType.values();
        final List<List<ShopCategory>> cList=new ArrayList<>();
        List<ShopCategory> foodShops=new ArrayList<>();
        ShopCategory category=new ShopCategory();
        category.setCategoryid(0);
        category.setCategoryname("全部餐厅");
        foodShops.add(category);
        foodShops.addAll(condition.getShopcategorylist());
        cList.add(new ArrayList<ShopCategory>());
        cList.add(foodShops);
        cList.add(new ArrayList<ShopCategory>());
        cList.add(new ArrayList<ShopCategory>());

        final List<ShopCategory> cValues = new ArrayList<>();
        cValues.addAll(cList.get(0));
        final PopupAdapter pAdapter = new PopupAdapter<ShopType>(context, R.layout.popup_item, shopTypes, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected void setView(int position, ViewHolder holder) {
                holder.tv.setText(getItem(position).desc);
            }
        };
        final PopupAdapter cAdapter = new PopupAdapter<ShopCategory>(context, R.layout.popup_item2, cValues, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected void setView(int position, ViewHolder holder) {
                holder.tv.setText(getItem(position).getCategoryname());
                holder.tv2.setText(position==0?"":getItem(position).getCategoryshopnum()+"");
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
                if(shopTypes[position]==ShopType.ALL){
                    btnType.setText(shopTypes[position].desc);
                    btnType.hidePopup();
                    onItemClickListener.onClick(new SearchCondition.Result(SearchCondition.CondType.ShopCategory, ShopType.ALL,null));
                }
            }
        });

        btnType.setPopupView(popup);
        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPostion(position);
                cAdapter.notifyDataSetChanged();
                btnType.setText(cValues.get(position).getCategoryname());
                btnType.hidePopup();
                onItemClickListener.onClick(new SearchCondition.Result(SearchCondition.CondType.ShopCategory, pLv.getSelectedItem(), cValues.get(position)));
            }
        });
    }

    private void initBtnSort(LayoutInflater inflater, Context context){
        View popup = inflater.inflate(R.layout.popup1, null);
        ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);

        final SortType[] sortTypes=SortType.values();

        final PopupAdapter pAdapter = new PopupAdapter<SortType>(context, R.layout.popup_item, sortTypes, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected void setView(int position, ViewHolder holder) {
                holder.tv.setText(getItem(position).desc);
            }
        };
        pAdapter.setPressPostion(0);
        pLv.setAdapter(pAdapter);
        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pAdapter.setPressPostion(position);
                pAdapter.notifyDataSetChanged();
                btnSort.setText(sortTypes[position].desc);
                btnSort.hidePopup();
                onItemClickListener.onClick(new SearchCondition.Result(SearchCondition.CondType.Sort, sortTypes[position],null));
            }
        });

        btnSort.setPopupView(popup);
    }

    private void initBtnLocation(LayoutInflater inflater, Context context){
        ResShopCondition condition = App.instance().locationService().getCondition();
        View popup = inflater.inflate(R.layout.popup2, null);
        final ListView pLv = (ListView) popup.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) popup.findViewById(R.id.child_lv);

        Area area=new Area();
        area.setAreaid(0);
        area.setAraename("附近");
        area.setCirclelist(new ArrayList<Circle>());
        Circle distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("附近（智能范围）");
        distance.distance=2000;
        area.getCirclelist().add(distance);
        distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("1000m");
        distance.distance=1000;
        area.getCirclelist().add(distance);
        distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("3000m");
        distance.distance=3000;
        area.getCirclelist().add(distance);
        distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("5000m");
        distance.distance=5000;
        area.getCirclelist().add(distance);
        distance=new Circle();
        distance.setCircleid(0);
        distance.setCirclename("10000m");
        distance.distance=10000;
        area.getCirclelist().add(distance);
        final List<Area> areas=new ArrayList<>();
        areas.add(area);
        areas.addAll(condition.getArealist());

        final List<List<Circle>> cList=new ArrayList<>();
        for(Area a:areas){
            cList.add(a.getCirclelist());
        }
        final List<Circle> cValues = new ArrayList<>();
        cValues.addAll(cList.get(0));
        final PopupAdapter pAdapter = new PopupAdapter<Area>(context, R.layout.popup_item, areas, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected void setView(int position, ViewHolder holder) {
                holder.tv.setText(getItem(position).getAraename());
            }
        };
        final PopupAdapter cAdapter = new PopupAdapter<Circle>(context, R.layout.popup_item2, cValues, R.color.base_start_color_default, R.color.base_start_color_pressed) {
            @Override
            protected void setView(int position, ViewHolder holder) {
                holder.tv.setText(getItem(position).getCirclename());
                holder.tv2.setText(position==0?"":getItem(position).getCircleshopnum()+"");
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

        btnLocation.setPopupView(popup);
        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPostion(position);
                cAdapter.notifyDataSetChanged();
                btnLocation.setText(cValues.get(position).getCirclename());
                btnLocation.hidePopup();
                Circle c=cValues.get(position);
                if(c.getCircleid()<=0){
                    SearchCondition.NearBy nearBy=new SearchCondition.NearBy();
                    nearBy.id=position;
                    nearBy.distance=c.distance;
                    onItemClickListener.onClick(new SearchCondition.Result(SearchCondition.CondType.Location, null,nearBy));
                }else {
                    onItemClickListener.onClick(new SearchCondition.Result(SearchCondition.CondType.Location,pLv.getSelectedItem(), cValues.get(position)));
                }
            }
        });

    }

    public static interface OnItemClickListener{
        public void onClick(SearchCondition.Result result);
    }
}

package com.alkaid.trip51.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.trip51.R;

import java.util.ArrayList;
import java.util.List;

import pw.h57.popupbuttonlibrary.PopupButton;
import pw.h57.popupbuttonlibrary.adapter.PopupAdapter;

/**
 * Created by df on 2015/12/3.
 */
public class DefaultFilter {
    public void init(LayoutInflater inflater,Context context,View v){
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
        final PopupAdapter pAdapter = new PopupAdapter(context,R.layout.popup_item,pList,R.color.base_start_color_default,R.color.base_start_color_pressed);
        final PopupAdapter cAdapter = new PopupAdapter(context,R.layout.popup_item,cValues,R.color.base_start_color_default,R.color.base_start_color_pressed);
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
        final PopupButton btn1 = (PopupButton) v.findViewById(R.id.popbtn1);
        final PopupButton btn2 = (PopupButton) v.findViewById(R.id.popbtn2);
        final PopupButton btn3 = (PopupButton) v.findViewById(R.id.popbtn3);
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
    }
}

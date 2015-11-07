package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alkaid.trip51.R;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public ShopListAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_home_list_item, null);
//                holder = new ViewHolder();
//                /**得到各个控件的对象*/
//                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
//                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
//                holder.bt = (Button) convertView.findViewById(R.id.ItemButton);
//                convertView.setTag(holder);//绑定ViewHolder对象
        } else {
//                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        return convertView;
    }
}

package com.alkaid.trip51.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.SimpleCity;

import java.util.List;

//城市列表adapter

/**
 * Created by jyz on 2015/12/10.
 */
public class CityGridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;

    private List<SimpleCity> cities;

    public CityGridViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setData(List<SimpleCity> cities) {
        this.cities = cities;
    }

    @Override
    public int getCount() {
        if (cities != null) {
            return cities.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (cities != null) {
            return cities.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn = new Button(mContext);
        btn.setBackgroundResource(R.color.white);
        btn.setText(cities.get(position).getCityname());
        return btn;
    }
}

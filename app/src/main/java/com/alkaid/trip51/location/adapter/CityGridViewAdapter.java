package com.alkaid.trip51.location.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.SimpleCity;

import org.w3c.dom.Text;

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
        TextView tv = (TextView) mInflater.inflate(R.layout.tv_city_grid_item, null);
        SimpleCity city= (SimpleCity) getItem(position);
        if(null!=city)
            tv.setText(city.getCityname());
        return tv;
    }
}

package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.response.ResShopList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ResShopList.Shop> shops=new ArrayList<ResShopList.Shop>();

    public ShopListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<ResShopList.Shop> shops){
        this.shops=shops;
    }
    public List<ResShopList.Shop> getData(){
        return shops;
    }

    @Override
    public int getCount() {
        return shops.size()==0?20:shops.size();
    }

    @Override
    public Object getItem(int position) {
        return shops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_home_list_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvAvgPay = (TextView) convertView.findViewById(R.id.tvAvgPay);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.tvAreaAndType = (TextView) convertView.findViewById(R.id.tvAreaAndType);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.rtbProductRating=(RatingBar)convertView.findViewById(R.id.rtbProductRating);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        if(shops.isEmpty()){
            return convertView;
        }
        ResShopList.Shop shop=shops.get(position);
        holder.rtbProductRating.setRating(shop.getTotallevel());
        holder.tvTitle.setText(shop.getShopname());
        holder.tvAvgPay.setText(shop.getAvgpay());
        String status="";
        if(shop.getPrivaterroomstatus()==1){
            status+="包房（<font color='green'>空闲</font>）       ";
        }else if(shop.getPrivaterroomstatus()==2){
            status+="包房（<font color='red'>紧张</font>）       ";
        }
        if(shop.getHallstatus()==1){
            status+="大厅（<font color='green'>空闲</font>）       ";
        }else if(shop.getHallstatus()==2){
            status+="大厅（<font color='red'>紧张</font>）       ";
        }
        holder.tvStatus.setText(Html.fromHtml(status));
        holder.tvAreaAndType.setText((shop.getAreaname() == null ? "" : shop.getAreaname() + " ") + (shop.getDiningtypename() == null ? "" : shop.getDiningtypename()));
        holder.tvDistance.setText(shop.getDistance()+"m");
        return convertView;
    }

    private class ViewHolder{
        public TextView tvTitle,tvAvgPay,tvStatus,tvAreaAndType,tvDistance;
        public RatingBar rtbProductRating;
    }
}

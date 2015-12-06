package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.enums.OrderStatus;
import com.alkaid.trip51.model.enums.SeatType;
import com.alkaid.trip51.model.response.ResOrderList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/7.
 */
public class OrderListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ResOrderList.Order> data=new ArrayList<>();

    public OrderListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public List<ResOrderList.Order> getData() {
        return data;
    }

    public void setData(List<ResOrderList.Order> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getOrderid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.myorder_list_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tvShopName = (TextView) convertView.findViewById(R.id.tvShopName);
            holder.tvPersonNum = (TextView) convertView.findViewById(R.id.tvPersonNum);
            holder.tvSeatType = (TextView) convertView.findViewById(R.id.tvSeatType);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvOrderNo = (TextView) convertView.findViewById(R.id.tvOrderNo);
            holder.tvAmount= (TextView) convertView.findViewById(R.id.tvAmount);
            holder.tvOrderStatus= (TextView) convertView.findViewById(R.id.tvOrderStatus);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        ResOrderList.Order obj=data.get(position);
        holder.tvShopName.setText(obj.getShopname());
        holder.tvPersonNum.setText(obj.getPersonnum()+"人");
        holder.tvSeatType.setText(SeatType.getByCode(obj.getRoomtype()).desc);
        holder.tvTime.setText(obj.getDinnerdate());
        holder.tvOrderNo.setText(obj.getOrderno());
        if(!TextUtils.isEmpty(obj.getOrderamount())) {
            holder.tvAmount.setText("￥" + obj.getOrderamount());
        }
        if(obj.getOrderstatus()>0){
            holder.tvOrderStatus.setText(OrderStatus.getByCode(obj.getOrderstatus()).desc);
        }else{
            holder.tvOrderStatus.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder{
        public TextView tvShopName,tvPersonNum,tvSeatType,tvTime,tvOrderNo,tvAmount,tvOrderStatus;
    }
}

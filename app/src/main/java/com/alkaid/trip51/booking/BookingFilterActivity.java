package com.alkaid.trip51.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.shop.ShopListActivity;
import com.alkaid.trip51.widget.Operator;

/**
 * Created by alkaid on 2015/11/7.
 */
public class BookingFilterActivity extends BaseActivity {
    private ViewGroup layContent,laySeat;
    private Operator opNums;
    private LayoutInflater inflater;
    private View btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_booking);
        btnSearch=findViewById(R.id.btnSearch);
        layContent= (ViewGroup) findViewById(R.id.content);
        inflater= LayoutInflater.from(context);
        initItem(R.drawable.booking_ic_time,"时间","08月26日 今天 18：:00");
        initItem(R.drawable.booking_ic_person,"人数","选择人数");
        laySeat= (ViewGroup) inflater.inflate(R.layout.item_booking_filter_seat,layContent,false);
        layContent.addView(laySeat);
        initItem(R.drawable.booking_ic_location, "商圈", "大雁塔");
        initItem(R.drawable.booking_ic_cuisine, "菜系", "粤菜");
        initItem(R.drawable.booking_ic_shoptype,"就餐类型","主题餐厅");

        opNums= (Operator) laySeat.findViewById(R.id.opNums);
        opNums.setOperationCallback(new Operator.OperationListener() {
            @Override
            public void onAddClick(int i) {

            }

            @Override
            public void onSubClick(int i) {

            }

            @Override
            public void onTextChange(int i) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ShopListActivity.class));
                finish();
            }
        });
    }

    private void initItem(int resIconId,String name,String value){
        ViewHolder holder=new ViewHolder();
        holder.lay = (ViewGroup) inflater.inflate(R.layout.item_booking_filter,layContent,false);
        holder.icon= (ImageView) holder.lay.findViewById(R.id.ivItemIcon);
        holder.itemName= (TextView) holder.lay.findViewById(R.id.tvItemName);
        holder.itemValue= (TextView) holder.lay.findViewById(R.id.tvItemValue);
        holder.icon.setImageResource(resIconId);
        holder.itemName.setText(name);
        holder.itemValue.setText(value);
        layContent.addView(holder.lay);
    }

    private static class ViewHolder{
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
        private TextView itemValue;
    }
}

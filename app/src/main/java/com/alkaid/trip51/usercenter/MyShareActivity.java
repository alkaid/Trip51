package com.alkaid.trip51.usercenter;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

import java.util.ArrayList;

/**
 * 我的余额模块
 */

/**
 * Created by alkaid on 2015/11/9.
 */
public class MyShareActivity extends BaseActivity {

    private GridView mGrridView;

    private ArrayList<Integer> shareTypes;

    LayoutInflater mInflate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
        initTitleBar();
        initView();
        mInflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        shareTypes = new ArrayList<>();
        shareTypes.add(R.drawable.temp_weixin_big);
        shareTypes.add(R.drawable.temp_pyq_big);
        shareTypes.add(R.drawable.temp_qq_big);
        shareTypes.add(R.drawable.temp_weibo_big);
        shareTypes.add(R.drawable.temp_qqkj_big);
        shareTypes.add(R.drawable.temp_so_on_big);
        mGrridView.setAdapter(new ShareTypeAdapter());

    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("我的分享");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        mGrridView = (GridView) findViewById(R.id.gv_share_type);
    }

    private class ShareTypeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return shareTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return shareTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflate.inflate(R.layout.item_share_type, null);
            }
            ((ImageView)convertView).setImageResource(shareTypes.get(position));
            return convertView;
        }
    }

    private class ViewHolder{

    }
}

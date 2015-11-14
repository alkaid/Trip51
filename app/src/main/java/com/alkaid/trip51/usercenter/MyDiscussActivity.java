package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.shop.adapter.MyDiscussAdapter;
import com.alkaid.trip51.shop.adapter.MyFavoriteAdapter;

/**
 * Created by alkaid on 2015/11/9.
 */
public class MyDiscussActivity extends BaseActivity {

    private ListView myFavoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discuss);
        initTitleBar();
        initView();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("我的评论");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        myFavoriteList = (ListView)findViewById(R.id.lv_my_discuss);
        myFavoriteList.setAdapter(new MyDiscussAdapter(this));
    }
}

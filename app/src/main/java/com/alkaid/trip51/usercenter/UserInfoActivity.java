package com.alkaid.trip51.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by alkaid on 2015/11/9.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener
{
    private RelativeLayout relTelphone;//电话绑定
    private RelativeLayout relHeadSetting;//头像设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initTitleBar();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("个人信息");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        relTelphone = (RelativeLayout) findViewById(R.id.rl_tel);
        relTelphone.setOnClickListener(this);
        relHeadSetting = (RelativeLayout) findViewById(R.id.rl_head_setting);
        relHeadSetting.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_tel:
                startActivity(new Intent(this,ModifyTelBindActivity.class));
                break;
            case R.id.rl_head_setting:
                startActivity(new Intent(this,TakePhoneActivity.class));
                break;
        }
    }
}

package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by df on 2015/11/19.
 */
public class SmsValcodeActivity extends BaseActivity{
//    private static final String action_register="com.alkaid.smsvalcode.register";
//    private static final String action_login=""
    public static final String BUNDLE_KEY_PHONE="BUNDLE_KEY_PHONE";
    public static final String BUNDLE_KEY_SMSVALCODE="BUNDLE_KEY_SMSVALCODE";
    private Button btnSure;
    private EditText etValCode;
    private TextView tvPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_valcode);
        initTitleBar();
        tvPhone= (TextView) findViewById(R.id.tvPhone);
        etValCode= (EditText) findViewById(R.id.etValCode);
        btnSure= (Button) findViewById(R.id.btnSure);
        tvPhone.setText(getIntent().getStringExtra(BUNDLE_KEY_PHONE));
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valcode=etValCode.getText().toString().trim();
                if(TextUtils.isEmpty(valcode)){
                    //TODO 更新UI提示错误 暂时toast要替换
                    toastShort("输入有误");
                }else{
                    Intent intent=new Intent();
                    intent.putExtra(BUNDLE_KEY_SMSVALCODE,valcode);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

    }
    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        TextView tvBarRight= (TextView) findViewById(R.id.tvRight);
        tvTitle.setText("验证码");
        tvBarRight.setText("编辑");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by alkaid on 2015/12/6.
 */
public class SimpleEditorActivity extends BaseActivity{
    public static final int FIELD_NICKNAME=1;
    public static final int FIELD_NAME=2;
    public static final String BUNDLE_KEY_FIELD="BUNDLE_KEY_FIELD";
    private int field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_editor);
        field=getIntent().getIntExtra(BUNDLE_KEY_FIELD,field);
        initTitleBar();
    }
    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        TextView tvBarRight= (TextView) findViewById(R.id.tvRight);
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (field){
            case FIELD_NAME:
                tvTitle.setText("修改姓名");
                tvBarRight.setText("编辑");
                break;
            case FIELD_NICKNAME:
                tvTitle.setText("验证码");
                tvBarRight.setText("编辑");
                break;
        }
    }
}

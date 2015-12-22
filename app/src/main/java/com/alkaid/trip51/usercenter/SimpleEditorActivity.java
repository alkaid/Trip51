package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResponseData;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/12/6.
 */
public class SimpleEditorActivity extends BaseActivity{
    public static final int FIELD_NICKNAME=1;
    public static final int FIELD_NAME=2;
    public static final String BUNDLE_KEY_FIELD="BUNDLE_KEY_FIELD";
    private int field;
    private TextView tvName;
    private EditText etValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_editor);
        field=getIntent().getIntExtra(BUNDLE_KEY_FIELD,field);
        tvName= (TextView) findViewById(R.id.tvName);
        etValue= (EditText) findViewById(R.id.etValue);
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
        tvBarRight.setText("完成");
        tvBarRight.setVisibility(View.VISIBLE);
        switch (field){
            case FIELD_NAME:
                tvTitle.setText("修改姓名");
                tvName.setText("姓名");
                etValue.setText(App.accountService().getAccount().getRealname());
                break;
            case FIELD_NICKNAME:
                tvTitle.setText("修改昵称");
                tvName.setText("昵称");
                etValue.setText(App.accountService().getAccount().getNickname());
                break;
        }
        tvBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value=etValue.getText().toString().trim();
                if(TextUtils.isEmpty(value))
                    return;
                Map<String,String> beSignForm=new HashMap<String, String>();
                Map<String,String> unBeSignform=new HashMap<String, String>();
                beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
                unBeSignform.put("nickname", value);
                final String tag="modifyuserinfo"+(int)(Math.random()*1000);
                setDefaultPdgCanceListener(tag);
                showPdg();
                App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true, ResponseData.class, MApiService.URL_USER_MODIFY_NICKNAME, beSignForm, unBeSignform, new Response.Listener<ResponseData>() {
                    @Override
                    public void onResponse(ResponseData response) {
                        App.accountService().getAccount().setNickname(value);
                        App.accountService().save();
                        dismissPdg();
                        toastShort("昵称修改成功");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissPdg();
                        //TODO 暂时用handleException 应该换成失败时的正式UI
                        handleException(MApiService.parseError(error));
                        checkIsNeedRelogin(error);
                    }
                }), tag);
            }
        });
    }
}

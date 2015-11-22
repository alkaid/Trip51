package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResSmsValCode;
import com.alkaid.trip51.util.SecurityUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by df on 2015/11/19.
 */
public class UserRegisterActivity extends BaseActivity{
    private EditText etAccountId,etPwd;
    private Button btnGetSms;
    private String mobile;
    private String pwd;
    private String smsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initTitleBar();
        etAccountId= (EditText) findViewById(R.id.etAccountId);
        etPwd= (EditText) findViewById(R.id.etPwd);
        btnGetSms= (Button) findViewById(R.id.btnGetSms);
        btnGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile=etAccountId.getText().toString().trim();
                pwd=etPwd.getText().toString().trim();
                //TODO 除了非空 还要校验格式
                if(TextUtils.isEmpty(mobile)||TextUtils.isEmpty(pwd)){
                    //TODO 验证失败时的UI 暂时toast要替换
                    toastShort("输入有误");
                }else{
                    Map<String,String> beSignForm=new HashMap<String, String>();
                    Map<String,String> unBeSignform=new HashMap<String, String>();
                    beSignForm.put("mobile", mobile);
                    final String tag="smscode"+(int)(Math.random()*1000);
                    setDefaultPdgCanceListener(tag);
                    showPdg();
                    //请求短信
                    App.mApiService().exec(new MApiRequest(CacheType.DISABLED,MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.v(response.toString());
                            Gson gson = new Gson();
                            ResSmsValCode resSmsValCode = gson.fromJson(response, ResSmsValCode.class);
                            if (resSmsValCode.isSuccess()) {
                                smsid=resSmsValCode.getSmsid();
                                Intent intent = new Intent(context, SmsValcodeActivity.class);
                                dismissPdg();
                                intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_PHONE,etAccountId.getText().toString().trim());
                                startActivityForResult(intent, 1);
                            } else {
                                dismissPdg();
                                //TODO 暂时用handleException 应该换成失败时的正式UI
                                handleException(TradException.create(resSmsValCode.getMsg()));
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            LogUtil.v(error.toString());
                            dismissPdg();
                            handleException(new TradException());
                        }
                    }), tag);
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
        tvTitle.setText("注册");
        tvBarRight.setText("登录");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            String valcode = data.getStringExtra(SmsValcodeActivity.BUNDLE_KEY_SMSVALCODE);
            showPdg();
            Map<String,String> beSignForm=new HashMap<String, String>();
            Map<String,String> unBeSignform=new HashMap<String, String>();
            beSignForm.put("mobile", mobile);
            unBeSignform.put("userpwd", SecurityUtil.getSHA1WithSalt(pwd));
            unBeSignform.put("valcode",valcode);
            unBeSignform.put("smsid",smsid);
            final String tag="userregister"+(int)(Math.random()*1000);
            setDefaultPdgCanceListener(tag);
            showPdg();
            //请求注册
            App.mApiService().exec(new MApiRequest(CacheType.DISABLED,MApiService.URL_REGISTER, beSignForm, unBeSignform, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.v(response.toString());
                    Gson gson = new Gson();
                    ResSmsValCode resSmsValCode = gson.fromJson(response, ResSmsValCode.class);
                    if (resSmsValCode.isSuccess()) {
                        Intent intent = new Intent(context, SmsValcodeActivity.class);
                        dismissPdg();
                        intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_PHONE,etAccountId.getText().toString().trim());
                        startActivityForResult(intent, 1);
                    } else {
                        dismissPdg();
                        //TODO 暂时用handleException 应该换成失败时的正式UI
                        handleException(TradException.create(resSmsValCode.getMsg()));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.v(error.toString());
                    dismissPdg();
                    handleException(new TradException());
                }
            }), tag);
        }else{
            toastShort("取消注册");
        }
    }
}

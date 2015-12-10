package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.AccountService;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResLogin;
import com.alkaid.trip51.model.response.ResSmsValCode;
import com.alkaid.trip51.util.SecurityUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
                    unBeSignform.put("flag",MApiService.SMSCODE_FOR_REGISTER+"");
                    final String tag="smscode"+(int)(Math.random()*1000);
                    setDefaultPdgCanceListener(tag);
                    showPdg();
                    //请求短信
                    App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResSmsValCode.class,MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<ResSmsValCode>() {
                        @Override
                        public void onResponse(ResSmsValCode response) {
                            dismissPdg();
                            smsid=response.getSmsid();
                            Intent intent = new Intent(context, SmsValcodeActivity.class);
                            intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_PHONE,etAccountId.getText().toString().trim());
                            startActivityForResult(intent, AccountService.REQUEST_CODE_REGISTER);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissPdg();
                            handleException(new TradException(error.getMessage(),error));
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
            unBeSignform.put("smsid",smsid==null?"1111":smsid); //TODO 测试用 正式版本要验证smsid
            final String tag="userregister"+(int)(Math.random()*1000);
            setDefaultPdgCanceListener(tag);
            showPdg();
            //请求注册
            App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResLogin.class,MApiService.URL_REGISTER, beSignForm, unBeSignform, new Response.Listener<ResLogin>() {
                @Override
                public void onResponse(ResLogin response) {
                    dismissPdg();
                    toastShort("注册/登录成功");
                    App.accountService().handleLogined(response);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissPdg();
                    //TODO 暂时用handleException 应该换成失败时的正式UI
                    handleException(new TradException(error.getMessage(),error));
                }
            }), tag);
        }else{
            toastShort("取消注册");
        }
    }
}

package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResLogin;
import com.alkaid.trip51.model.response.ResSmsValCode;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/19.
 */
public class MobileLoginFragment extends BaseFragment {
    private EditText etAccountId;
    private Button btnGetSms;
    private String mobile;
    private String smsid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mobile_login,container,false);
        etAccountId= (EditText) v.findViewById(R.id.etAccountId);
        btnGetSms= (Button) v.findViewById(R.id.btnGetSms);
        btnGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile=etAccountId.getText().toString().trim();
                //TODO 除了非空 还要校验格式
                if(TextUtils.isEmpty(mobile)){
                    //TODO 验证失败时的UI 暂时toast要替换
                    toastShort("输入有误");
                }else{
                    Map<String,String> beSignForm=new HashMap<String, String>();
                    Map<String,String> unBeSignform=new HashMap<String, String>();
                    beSignForm.put("mobile", mobile);
                    unBeSignform.put("flag",MApiService.SMSCODE_FOR_LOGIN+"");
                    final String tag="smscode"+(int)(Math.random()*1000);
                    setDefaultPdgCanceListener(tag);
                    showPdg();
                    //请求短信
                    App.mApiService().exec(new MApiRequest(CacheType.DISABLED, MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<String>() {
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
                            //TODO 暂时用handleException 应该换成失败时的正式UI
                            handleException(new TradException());
                        }
                    }), tag);
                }
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            String valcode = data.getStringExtra(SmsValcodeActivity.BUNDLE_KEY_SMSVALCODE);
            showPdg();
            Map<String,String> beSignForm=new HashMap<String, String>();
            Map<String,String> unBeSignform=new HashMap<String, String>();
            beSignForm.put("mobile", mobile);
            unBeSignform.put("valcode",valcode);
            unBeSignform.put("smsid",smsid==null?"1111":smsid); //TODO 测试用 正式版本要验证smsid
            final String tag="userlogin"+(int)(Math.random()*1000);
            setDefaultPdgCanceListener(tag);
            showPdg();
            //请求注册
            App.mApiService().exec(new MApiRequest(CacheType.DISABLED,MApiService.URL_LOGIN_MOBILE, beSignForm, unBeSignform, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.v(response.toString());
                    Gson gson = new Gson();
                    ResLogin resLogin=gson.fromJson(response,ResLogin.class);
                    dismissPdg();
                    if(resLogin.isSuccess()){
                        toastShort("登录成功");
                        App.accountService().handleLogined(resLogin);
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    } else {
                        //TODO 暂时用handleException 应该换成失败时的正式UI
                        handleException(TradException.create(resLogin.getMsg()));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.v(error.toString());
                    dismissPdg();
                    //TODO 暂时用handleException 应该换成失败时的正式UI
                    handleException(new TradException());
                }
            }), tag);
        }else{
            toastShort("取消登录");
        }
    }
}

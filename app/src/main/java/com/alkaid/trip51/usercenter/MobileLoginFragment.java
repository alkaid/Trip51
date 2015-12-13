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

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResLogin;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/19.
 */
public class MobileLoginFragment extends BaseFragment {
    private EditText etAccountId;
    private Button btnGetSms;
    private String mobile;
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
                    Intent intent = new Intent(context, SmsValcodeActivity.class);
                    intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_PHONE,etAccountId.getText().toString().trim());
                    intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_SMSVALCODE_FLAG,MApiService.SMSCODE_FOR_LOGIN);
                    intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_SMSID,"");
                    startActivityForResult(intent, 1);

//                    Map<String,String> beSignForm=new HashMap<String, String>();
//                    Map<String,String> unBeSignform=new HashMap<String, String>();
//                    beSignForm.put("mobile", mobile);
//                    unBeSignform.put("flag",MApiService.SMSCODE_FOR_LOGIN+"");
//                    final String tag="smscode"+(int)(Math.random()*1000);
//                    setDefaultPdgCanceListener(tag);
//                    showPdg();
//                    //请求短信
//                    App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResSmsValCode.class, MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<ResSmsValCode>() {
//                        @Override
//                        public void onResponse(ResSmsValCode response) {
//                            dismissPdg();
//                            String smsid=response.getSmsid();
//                            Intent intent = new Intent(context, SmsValcodeActivity.class);
//                            intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_PHONE,etAccountId.getText().toString().trim());
//                            intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_SMSVALCODE_FLAG,MApiService.SMSCODE_FOR_LOGIN);
//                            intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_SMSID,smsid);
//                            startActivityForResult(intent, 1);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            dismissPdg();
//                            //TODO 暂时用handleException 应该换成失败时的正式UI
//                            handleException(new TradException(error.getMessage(),error));
//                        }
//                    }), tag);
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
            String smsid = data.getStringExtra(SmsValcodeActivity.BUNDLE_KEY_SMSID);
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
            App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResLogin.class,MApiService.URL_LOGIN_MOBILE, beSignForm, unBeSignform, new Response.Listener<ResLogin>() {
                @Override
                public void onResponse(ResLogin response) {
                    dismissPdg();
                    toastShort("登录成功");
                    App.accountService().handleLogined(response);
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
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
            toastShort("取消登录");
        }
    }
}

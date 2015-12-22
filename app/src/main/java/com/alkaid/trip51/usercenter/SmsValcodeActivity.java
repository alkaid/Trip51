package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResSmsValCode;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by df on 2015/11/19.
 */
public class SmsValcodeActivity extends BaseActivity{
//    private static final String action_register="com.alkaid.smsvalcode.register";
//    private static final String action_login=""
    public static final String BUNDLE_KEY_PHONE="BUNDLE_KEY_PHONE";
    public static final String BUNDLE_KEY_SMSVALCODE_FLAG="BUNDLE_KEY_SMSVALCODE_FLAG";
    public static final String BUNDLE_KEY_SMSVALCODE="BUNDLE_KEY_SMSVALCODE";
    public static final String BUNDLE_KEY_SMSID="BUNDLE_KEY_SMSID";
    private Button btnSure;
    private EditText etValCode;
    private TextView tvPhone,btnGetSmsCode;
//    private long recTimeMs=60000;
    private String smsid;
    private int smsValCodeFlag;
    private String mobile;
    private RetryTimeCountDown retryTimeCountDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_valcode);
        smsValCodeFlag=getIntent().getIntExtra(BUNDLE_KEY_SMSVALCODE_FLAG, 0);
        smsid=getIntent().getStringExtra(BUNDLE_KEY_SMSID);
        mobile=getIntent().getStringExtra(BUNDLE_KEY_PHONE);
        initTitleBar();
        tvPhone= (TextView) findViewById(R.id.tvPhone);
        etValCode= (EditText) findViewById(R.id.etValCode);
        btnSure= (Button) findViewById(R.id.btnSure);
        tvPhone.setText(mobile);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valcode = etValCode.getText().toString().trim();
                if (TextUtils.isEmpty(valcode)) {
                    //TODO 更新UI提示错误 暂时toast要替换
                    toastShort("输入有误");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_KEY_SMSVALCODE, valcode);
                    intent.putExtra(SmsValcodeActivity.BUNDLE_KEY_SMSID,smsid);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnGetSmsCode= (TextView) findViewById(R.id.btnGetSmsCode);
        btnGetSmsCode.setText(App.accountService().getMillisUntilNextSmscode() / 1000 + "s后重新获取验证码");
        btnGetSmsCode.setEnabled(false);
        btnGetSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("Retry get sms code");
                loadData();
            }
        });
        if(App.accountService().getMillisUntilNextSmscode()<=0){
            loadData();
        }else{
            retryTimeCountDown=new RetryTimeCountDown();
            retryTimeCountDown.start();
        }
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

    private void loadData(){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        beSignForm.put("mobile", mobile);
        unBeSignform.put("flag", smsValCodeFlag+"");
        final String tag="retrySmscode"+(int)(Math.random()*1000);
//        setDefaultPdgCanceListener(tag);
        showPdg();
        //请求短信
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true,ResSmsValCode.class, MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<ResSmsValCode>() {
            @Override
            public void onResponse(ResSmsValCode response) {
                dismissPdg();
                smsid = response.getSmsid();
                toastShort(response.getMsg());
                App.accountService().resetMillisUntilNextSmscode();
                retryTimeCountDown=new RetryTimeCountDown();
                retryTimeCountDown.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                handleException(MApiService.parseError(error));
            }
        }), tag);
    }

    @Override
    protected void onDestroy() {
        if(null!=retryTimeCountDown){
            retryTimeCountDown.cancel();
        }
        super.onDestroy();
    }

    public class RetryTimeCountDown extends CountDownTimer{
        public RetryTimeCountDown() {
            super(App.accountService().getMillisUntilNextSmscode(), 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            App.accountService().setMillisUntilNextSmscode(millisUntilFinished);
            btnGetSmsCode.setText(millisUntilFinished/1000+"s后重新获取验证码");
        }
        @Override
        public void onFinish() {
            App.accountService().setMillisUntilNextSmscode(0);
            btnGetSmsCode.setText("重新获取验证码");
            btnGetSmsCode.setEnabled(true);
        }
    }

}

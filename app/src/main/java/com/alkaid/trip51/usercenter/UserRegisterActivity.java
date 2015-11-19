package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResSmsValCode;
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
                if(TextUtils.isEmpty(etAccountId.getText())||TextUtils.isEmpty(etPwd.getText())){

                }else{
                    mobile=etAccountId.getText().toString();
                    Map<String,String> beSignForm=new HashMap<String, String>();
                    Map<String,String> unBeSignform=new HashMap<String, String>();
                    beSignForm.put("mobile",mobile);
                    //请求短信
                    App.mApiService().exec(new MApiRequest(MApiService.URL_SMSCODE, beSignForm, unBeSignform, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.v(response.toString());
                            Gson gson=new Gson();
                            ResSmsValCode resSmsValCode = gson.fromJson(response,ResSmsValCode.class);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            LogUtil.v(error.toString());
                        }
                    }), "smscode");
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
}

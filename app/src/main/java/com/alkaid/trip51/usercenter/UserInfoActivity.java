package com.alkaid.trip51.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiMultipartRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.account.Account;
import com.alkaid.trip51.model.response.ResUserFace;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.commonq.library.CropConfig;
import com.commonq.library.CropInterface;
import com.commonq.library.CropUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/9.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener,CropInterface
{
    private static final int REQUEST_CODE_NICKNAME=1001;
    private RelativeLayout relTelphone;//电话绑定
    private RelativeLayout relHeadSetting;//头像设置
    private NetworkImageView nivFace;
    private Account account;

    private ViewGroup layPhotoPick,layNickname;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private TextView tvNickname,tvSex,tvRealName,tvMobile;

    CropConfig mCropParams = new CropConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initTitleBar();
        if(!checkLogined()){
            finish();
        }
        account= App.accountService().getAccount();
        mCropParams.outputX=64;
        mCropParams.outputY=64;
        initView();
    }

    private void initView(){
        layPhotoPick= (ViewGroup) findViewById(R.id.layPhotoPick);
        layPhotoPick.setVisibility(View.GONE);
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.activity_take_photo,layPhotoPick,true);
        btn_take_photo = (Button) layPhotoPick.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) layPhotoPick.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) layPhotoPick.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
        relTelphone = (RelativeLayout) findViewById(R.id.rl_tel);
        relTelphone.setOnClickListener(this);
        relHeadSetting = (RelativeLayout) findViewById(R.id.rl_head_setting);
        relHeadSetting.setOnClickListener(this);
        layNickname= (ViewGroup) findViewById(R.id.layNickname);
        tvNickname=(TextView) findViewById(R.id.tvNickname);
        tvSex= (TextView) findViewById(R.id.tvSex);
        tvRealName= (TextView) findViewById(R.id.tvRealName);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        layNickname.setOnClickListener(this);

        tvNickname.setText(App.accountService().getAccount().getNickname());
        //TODO 服务端没有返回sex
//        tvSex.setText(App.accountService().getAccount().get);
        tvRealName.setText(App.accountService().getAccount().getRealname());
        String mobile=App.accountService().getAccount().getMobile();
        if(null!=mobile&&mobile.length()>=11){
            mobile=mobile.substring(0,3)+"****"+mobile.substring(7);
        }
        tvMobile.setText(mobile);

        nivFace= (NetworkImageView) findViewById(R.id.nivFace);
        nivFace.setDefaultImageResId(R.drawable.default_user_face);
        nivFace.setErrorImageResId(R.drawable.default_user_face);
        nivFace.setImageUrl(account.getAvater(), App.mApiService().getImageLoader());

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_tel:
                startActivity(new Intent(this, ModifyTelBindActivity.class));
                break;
            case R.id.rl_head_setting:
                layPhotoPick.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_take_photo:
                layPhotoPick.setVisibility(View.GONE);
                Log.v("CropUtils", "--CropHelper.imageUri--" + CropUtils.buildUri());
                Intent intent = CropUtils.buildCaptureIntent(CropUtils.buildUri());
                startActivityForResult(intent, CropUtils.REQUEST_CAMERA);
                break;
            case R.id.btn_pick_photo:
                layPhotoPick.setVisibility(View.GONE);
                Intent intent_gallery = CropUtils.buildCropFromGalleryIntent(mCropParams);
                Log.v("CropUtils", "intent:" + intent_gallery.getAction());
                startActivityForResult(intent_gallery, CropUtils.REQUEST_CROP);
                break;
            case R.id.btn_cancel:
                layPhotoPick.setVisibility(View.GONE);
                break;
            case R.id.layNickname:
                Intent intent1=new Intent(context,SimpleEditorActivity.class);
                intent1.putExtra(SimpleEditorActivity.BUNDLE_KEY_FIELD,SimpleEditorActivity.FIELD_NICKNAME);
                startActivityForResult(intent1,REQUEST_CODE_NICKNAME);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropUtils.handleResult(this, requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NICKNAME:
                    tvNickname.setText(account.getNickname());
                    break;
            }
        }
    }

    @Override
    public void onImageCropped(Uri uri) {
        Log.d("CropUtils", "Crop Uri in path: " + uri.getPath());
//		Toast.makeText(this, "Photo cropped!", Toast.LENGTH_LONG).show();
//		nivFace.setImageBitmap(CropUtils.decodeUriAsBitmap(this, uri));
        uploadFace(CropUtils.decodeUriAsBitmap(this, uri));
    }

    @Override
    public void onImageCanceled() {

    }

    @Override
    public void onImageFailed(String message) {
        toastShort("发生错误，请稍后重试");
    }

    @Override
    public CropConfig getCropConfig() {
        return mCropParams;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    private void uploadFace(Bitmap bm){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        final String tag="uploadFace"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        setDefaultPdgCanceListener(tag);
        MApiMultipartRequest req = new MApiMultipartRequest<ResUserFace>(CacheType.DISABLED, true, ResUserFace.class, MApiService.URL_USER_MODIFY_FACE, beSignForm, unBeSignform, new Response.Listener<ResUserFace>() {
            @Override
            public void onResponse(ResUserFace response) {
                dismissPdg();
                account.setAvater(response.headpicurl);
                App.accountService().save();
                nivFace.setImageUrl(response.headpicurl,App.mApiService().getImageLoader());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error.getMessage(),error));
                checkIsNeedRelogin(error);
            }
        });
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(mCropParams.outputFormat, 100, baos);
        req.buildPart("image",baos.toByteArray(),"face.jpg");
        App.mApiService().exec(req, tag);
    }
}

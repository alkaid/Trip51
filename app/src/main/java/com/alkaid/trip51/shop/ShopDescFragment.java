package com.alkaid.trip51.shop;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResBaseInfo;
import com.alkaid.trip51.model.shop.Baseinfo;
import com.alkaid.trip51.model.shop.Comment;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.adapter.ShopDetailEvaluationAdapter;
import com.alkaid.trip51.util.BitmapUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户菜单明细列表
 */

/**
 * Created by jyz on 2015/11/8.
 */
public class ShopDescFragment extends BaseFragment{
    private String TAG = getClass().getName();
    private Shop currShop;

    /*界面相关*/
    private ImageView imgShop;//商店缩略图
    private TextView tvShopName;//商店名字
    private LinearLayout llStarFav;//评价的星星数
    private TextView tvTaste;//口味的评分
    private TextView tvEnvironment;//环境的评分
    private TextView tvService;//服务的评分
    private TextView tvAdress;//定位地址
    private TextView tvPhone;// 电话号码
    private TextView tvEvaluationNum;//评价的数量
    private ListView lvEvaluationDetail;//评价详情
    private TextView tvShopOtherInfo;//店铺其他信息

    private String imgUrl;
    private String shopName;
    private float starlevel = 5;
    private String tasteScore = "5";
    private String serviceScore = "5";
    private String environmentScore = "5";
    private String adress="";
    private String phone="";
    private String otherInfo="";
    private List<Comment> commmentList;

    private ShopDetailEvaluationAdapter shopDetailEvaluationAdapter;//评价列表的adapter

    private static final int REQUEST_DATA_SUCCESS = 1001;
    private static final int TOTAL_LEVEL = 5;

    private static final int TOTAL_SCORE = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currShop = (Shop) getArguments().getSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP);
        View v=inflater.inflate(R.layout.fragment_shop_detail,container,false);
        initView(v);
        loadData();
        return v;
    }

    private void initView(View v){
        imgShop = (ImageView) v.findViewById(R.id.imgShopThumb);
        tvShopName = (TextView) v.findViewById(R.id.tvShopName);
        llStarFav  = (LinearLayout) v.findViewById(R.id.ll_star_fav);
        tvTaste = (TextView) v.findViewById(R.id.tvTaste);
        tvEnvironment = (TextView) v.findViewById(R.id.tvEnvironment);
        tvService = (TextView) v.findViewById(R.id.tvService);
        tvAdress = (TextView) v.findViewById(R.id.tvAdress);
        tvPhone = (TextView) v.findViewById(R.id.tvPhone);
        tvEvaluationNum = (TextView) v.findViewById(R.id.tvEvaluationNum);
        lvEvaluationDetail = (ListView) v.findViewById(R.id.lv_evaluation );
        tvShopOtherInfo = (TextView) v.findViewById(R.id.tvShopOtherInfo);

        shopDetailEvaluationAdapter = new ShopDetailEvaluationAdapter(getContext());
        lvEvaluationDetail.setAdapter(shopDetailEvaluationAdapter);

    }

    private void updateUI(){
        if(imgShop!=null){
            if(imgUrl!=null) {
                imgShop.setImageBitmap(BitmapUtil.getHttpBitmap(imgUrl));
            }else{
                imgShop.setImageBitmap((BitmapFactory.decodeResource(getContext().getResources(), R.drawable.temp_shop_thumb)));
            }
        }
        if(llStarFav!=null){
            llStarFav.removeAllViews();
            for(int i=0;i<starlevel;i++){
                ImageView ivStar = new ImageView(getContext());
                ivStar.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.star_thumb_fav));
                llStarFav.addView(ivStar);
            }
            for(int i=0;i<TOTAL_LEVEL-starlevel;i++){
                ImageView ivNoStar = new ImageView(getContext());
                ivNoStar.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.star_thumb_unfav));
                llStarFav.addView(ivNoStar);
            }
        }
        if(tvShopName!=null){
            tvShopName.setText(shopName);
        }
        if(tvAdress!=null){
            tvAdress.setText(adress);
        }
        if(tvTaste!=null){
            if(tasteScore!=null) {
                tvTaste.setText("口味" + tasteScore + "分");
            }else{
                tvTaste.setText("口味"+TOTAL_SCORE+"分");
            }
        }
        if(tvService!=null){
            if(serviceScore!=null){
                tvService.setText("服务"+ serviceScore +"分");
            }else{
                tvService.setText("服务" + TOTAL_SCORE + "分");
            }
        }
        if(tvEnvironment!=null){
            if(environmentScore!=null) {
                tvEnvironment.setText("环境" + environmentScore + "分");
            }else{
                tvEnvironment.setText("环境"+TOTAL_SCORE+"分");
            }
        }
        if(tvPhone!=null){
            tvPhone.setText(phone);
        }
        if(tvShopOtherInfo!=null){
            tvShopOtherInfo.setText(otherInfo);
        }
        if(shopDetailEvaluationAdapter!=null){
            shopDetailEvaluationAdapter.notifyDataSetChanged();
        }
        if(tvEvaluationNum!=null){
            if(commmentList!=null) {
                tvEvaluationNum.setText("网友点评（"+commmentList.size()+"）人");
            }
        }
    }

    private void setData(ResBaseInfo response){
        if(response!=null){
            Baseinfo baseInfo = response.getBaseinfo();
            imgUrl = baseInfo.getImgurl();
            shopName = baseInfo.getShopname();
            starlevel = baseInfo.getTotallevel();
            tasteScore = baseInfo.getTastescore();
            environmentScore = baseInfo.getEnvscore();
            serviceScore = baseInfo.getServicescore();
            adress = baseInfo.getAddress();
            otherInfo = baseInfo.getTips();
            commmentList = response.getComments();
            //TODO 要split分号
            phone=baseInfo.getShopphone();
            //评价的假数据
            for(int i=0;i<2;i++){
                Comment comment = new Comment();
                comment.setNickname("黑咖啡－676");
                comment.setAvgFee(35);
                comment.setCommentlevel(4);
                comment.setContent("咖啡蛮好喝，店里布置也够温馨，我续杯18次，撑了，服务周到，下次还来");
                commmentList.add(comment);
            }
            shopDetailEvaluationAdapter.setData(commmentList);
        }
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        loadData();
    }

    private void loadData(){
        if(currShop == null){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("shopid", currShop.getShopid()+"");
//        unBeSignform.put("pageinodex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag="shopdetail"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResBaseInfo.class, MApiService.URL_SHOP_SHOP_DETAIL, beSignForm, unBeSignform, new Response.Listener<ResBaseInfo>() {
            @Override
            public void onResponse(ResBaseInfo response) {
                setData(response);
                mHandler.sendEmptyMessage(REQUEST_DATA_SUCCESS);
                dismissPdg();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(MApiService.parseError(error));
            }
        }), tag);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_DATA_SUCCESS:
                    updateUI();
                    break;
            }
        }
    } ;
}

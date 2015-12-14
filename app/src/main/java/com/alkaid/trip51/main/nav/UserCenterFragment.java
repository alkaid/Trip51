package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.usercenter.MyBalanceActivity;
import com.alkaid.trip51.usercenter.MyClientCenterActivity;
import com.alkaid.trip51.usercenter.MyCouponActivity;
import com.alkaid.trip51.usercenter.MyDiscussActivity;
import com.alkaid.trip51.usercenter.MyFavoriteActivity;
import com.alkaid.trip51.usercenter.MyPointsActivity;
import com.alkaid.trip51.usercenter.MyShareActivity;
import com.alkaid.trip51.usercenter.SettingActivity;
import com.alkaid.trip51.usercenter.UserInfoActivity;
import com.alkaid.trip51.usercenter.UserLoginActivity;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by alkaid on 2015/11/9.
 */
public class UserCenterFragment extends BaseFragment {
    private ViewGroup layContent;
    private LayoutInflater inflater;
    private TextView tvAccount,tvNickname;
    private View layFav,layComment,layShare,layMall,layServer,layPraise,
                layBalance,layPoints,layUserInfo,layCupon;
    private NetworkImageView nivFace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.main_user_center_fragment, container, false);
        initTitleBar(v);
        initView(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLoginStatus();
    }

    private void changeLoginStatus(){
        if(App.accountService().isLogined()){
            layFav.setOnClickListener(onClickWhenLogined);
            layComment.setOnClickListener(onClickWhenLogined);
            layShare.setOnClickListener(onClickWhenLogined);
            layMall.setOnClickListener(onClickWhenLogined);
            layServer.setOnClickListener(onClickWhenLogined);
            layPraise.setOnClickListener(onClickWhenLogined);
            layBalance.setOnClickListener(onClickWhenLogined);
            layPoints.setOnClickListener(onClickWhenLogined);
            layUserInfo.setOnClickListener(onClickWhenLogined);
            layCupon.setOnClickListener(onClickWhenLogined);
            tvNickname.setText(App.accountService().getAccount().getNickname());
            tvAccount.setText("账号："+App.accountService().getAccount().getMobile());
        }else{
            layFav.setOnClickListener(onClickWhenUnLogined);
            layComment.setOnClickListener(onClickWhenUnLogined);
            layShare.setOnClickListener(onClickWhenUnLogined);
            layMall.setOnClickListener(onClickWhenUnLogined);
            layServer.setOnClickListener(onClickWhenUnLogined);
            layPraise.setOnClickListener(onClickWhenUnLogined);
            layBalance.setOnClickListener(onClickWhenUnLogined);
            layPoints.setOnClickListener(onClickWhenUnLogined);
            layUserInfo.setOnClickListener(onClickWhenUnLogined);
            layCupon.setOnClickListener(onClickWhenUnLogined);
            tvNickname.setText("点击登录");
            tvAccount.setText("账号：------");
        }
        nivFace.setImageUrl(App.accountService().getAccount().getAvater(), App.mApiService().getImageLoader());
    }

    private void initTitleBar(View v) {
        View layTitleBar = v.findViewById(R.id.title_bar);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        ImageButton btnLeft = (ImageButton) v.findViewById(R.id.btn_back_wx);
        ImageButton btnRight = (ImageButton) v.findViewById(R.id.notify);
        tvTitle.setText("我的");
        btnLeft.setImageResource(R.drawable.home_navibar_icon_bell);
        btnRight.setImageResource(R.drawable.trip51_setting);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });
    }

    private void initView(View v) {
        tvAccount= (TextView) v.findViewById(R.id.tvAccount);
        tvNickname= (TextView) v.findViewById(R.id.tvNickname);
        layBalance = v.findViewById(R.id.ll_my_balance);
        layPoints = v.findViewById(R.id.ll_my_points);
        layUserInfo = v.findViewById(R.id.ll_user_info);
        layCupon = v.findViewById(R.id.ll_user_coupon);
        layContent = (ViewGroup) v.findViewById(R.id.content);
        layFav = initItem(R.drawable.user_icon_fav, "我的收藏");
        layComment = initItem(R.drawable.user_icon_comment, "我的评论");
        layShare = initItem(R.drawable.user_icon_share, "分享好友");
        layMall = initItem(R.drawable.user_icon_mall, "积分商城");
        layServer = initItem(R.drawable.user_icon_server, "服务中心");
        layPraise = initItem(R.drawable.user_icon_praise, "去鼓励我");

        nivFace= (NetworkImageView) v.findViewById(R.id.nivFace);
        nivFace.setDefaultImageResId(R.drawable.default_user_face);
        nivFace.setErrorImageResId(R.drawable.default_user_face);
        nivFace.setImageUrl(App.accountService().getAccount().getAvater(), App.mApiService().getImageLoader());
    }

    private View initItem(int resIconId, final String name) {
        ViewHolder holder = new ViewHolder();
        holder.lay = (ViewGroup) inflater.inflate(R.layout.item_user_center, layContent, false);
        holder.icon = (ImageView) holder.lay.findViewById(R.id.ivItemIcon);
        holder.itemName = (TextView) holder.lay.findViewById(R.id.tvItemName);
        holder.icon.setImageResource(resIconId);
        holder.itemName.setText(name);
        layContent.addView(holder.lay);
        return holder.lay;
    }

    //未登录时
    private View.OnClickListener onClickWhenUnLogined = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==layShare){
                startActivity(new Intent(getActivity(), MyShareActivity.class));
            }else if(v==layServer){
                startActivity(new Intent(getActivity(), MyClientCenterActivity.class));
            }else if(v==layPraise){

            }else{
                startActivity(new Intent(getActivity(), UserLoginActivity.class));
            }
        }
    };
    //已登录时
    private View.OnClickListener onClickWhenLogined = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==layFav){
                startActivity(new Intent(getActivity(), MyFavoriteActivity.class));
            }else if(v==layComment){
                startActivity(new Intent(getActivity(), MyDiscussActivity.class));
            }else if(v==layShare){
                startActivity(new Intent(getActivity(), MyShareActivity.class));
            }else if(v==layMall){

            }else if(v==layServer){
                startActivity(new Intent(getActivity(), MyClientCenterActivity.class));
            }else if(v==layPraise){

            }else{
                switch (v.getId()) {
                    case R.id.ll_my_balance:
                        startActivity(new Intent(getActivity(), MyBalanceActivity.class));
                        break;
                    case R.id.ll_my_points:
                        startActivity(new Intent(getActivity(), MyPointsActivity.class));
                        break;
                    case R.id.ll_user_info:
                        startActivity(new Intent(getActivity(), UserInfoActivity.class));
                        break;
                    case R.id.ll_user_coupon:
                        startActivity(new Intent(getActivity(), MyCouponActivity.class));
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private static class ViewHolder {
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
    }

}

package com.alkaid.trip51.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;
import com.alkaid.trip51.base.widget.view.BaseTabPageViewActivity;

/**
 * Created by alkaid on 2015/11/19.
 */
public class UserLoginActivity extends BaseTabPageViewActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
//        setContentView(R.layout.activity_empty_fragment_wraper);
//        FragmentManager fragmentManager =getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        Fragment f=new UserLoginTabFragment();
//        transaction.replace(R.id.content,f).commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        mTabsAdapter.addTab(mTabHost.newTabSpec("短信快捷登录").setIndicator("短信快捷登录"),
                MobileLoginFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("普通登录").setIndicator("普通登录"),
                NormalLoginFragment.class, null);
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        TextView tvBarRight= (TextView) findViewById(R.id.tvRight);
        tvTitle.setText("登录");
        tvBarRight.setText("注册");
        tvBarRight.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBarRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,UserRegisterActivity.class),1);
            }
        });
    }

    /*public static class UserLoginTabFragment extends BaseTabPageViewFragment {
        private View slideshowView,layMainMenu,layOrder;

        @Override
        protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup v = (ViewGroup) inflater.inflate(R.layout.activity_user_login,container,false);
            initTitleBar(v);
            return v;
        }

        @Override
        protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
            mTabsAdapter.addTab(mTabHost.newTabSpec("短信快捷登录").setIndicator("短信快捷登录"),
                    MobileLoginFragment.class, null);
            mTabsAdapter.addTab(mTabHost.newTabSpec("普通登录").setIndicator("普通登录"),
                    NormalLoginFragment.class, null);
        }
        private void initTitleBar(View v){
            View layTitleBar=v.findViewById(R.id.title_bar);
            TextView tvTitle= (TextView) v.findViewById(R.id.tvTitle);
            View btnLeft=v.findViewById(R.id.btn_back_wx);
            View btnRight=v.findViewById(R.id.notify);
            TextView tvBarRight= (TextView) v.findViewById(R.id.tvRight);
            tvTitle.setText("登录");
            tvBarRight.setText("注册");
            btnRight.setVisibility(View.GONE);
            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getChildFragmentManager().beginTransaction().remove()
                    FragmentManager fragmentManager =getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(UserLoginTabFragment.this).commit();
                    getActivity().finish();
                }
            });
        }

    }*/
}
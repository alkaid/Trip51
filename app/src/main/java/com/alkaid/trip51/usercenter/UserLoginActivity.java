package com.alkaid.trip51.usercenter;

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

/**
 * Created by alkaid on 2015/11/19.
 */
public class UserLoginActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_fragment_wraper);
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment f=new UserLoginTabFragment();
        transaction.replace(R.id.content,f).commit();
    }



    public static class UserLoginTabFragment extends BaseTabPageViewFragment {
        private View slideshowView,layMainMenu,layOrder;

        @Override
        protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return (ViewGroup) inflater.inflate(R.layout.activity_user_login,container,false);
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
                    getActivity().finish();
                }
            });
        }

    }
}
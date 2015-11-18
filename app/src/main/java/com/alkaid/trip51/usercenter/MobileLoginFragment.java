package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;

/**
 * Created by alkaid on 2015/11/19.
 */
public class MobileLoginFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mobile_login,container,false);
        return v;
    }
}

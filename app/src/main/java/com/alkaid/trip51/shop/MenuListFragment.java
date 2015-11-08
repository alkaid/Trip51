package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;

/**
 * 商户菜单明细列表
 */
/**
 * Created by jyz on 2015/11/8.
 */
public class MenuListFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.menu_list_fragment,container,false);
        return v;
    }
}

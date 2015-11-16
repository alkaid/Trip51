package com.alkaid.trip51.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.shop.OrderDetailActivity;
import com.alkaid.trip51.shop.adapter.MyCouponAdapter;
import com.alkaid.trip51.shop.adapter.OrderListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by alkaid on 2015/11/8.
 */
public class MyCouponListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView lv = new ListView(getContext());
        lv.setBackgroundResource(R.color.white_background);
        lv.setAdapter(new MyCouponAdapter(getContext()));
        return lv;
    }
}
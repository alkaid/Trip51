package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.shop.adapter.OrderListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by alkaid on 2015/11/8.
 */
public class OrderListFragment extends BaseFragment {
    PullToRefreshListView ptrlv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.default_pull_list_view, container, false);
        ptrlv = (PullToRefreshListView) v.findViewById(R.id.ptrList);
        ptrlv.setAdapter(new OrderListAdapter(getActivity()));
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context,OrderDetailActivity.class));
            }
        });
        return v;
    }
}
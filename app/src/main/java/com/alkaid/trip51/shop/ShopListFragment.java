package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListFragment extends BaseFragment {
    PullToRefreshListView ptrlv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_list_fragment,container,false);
        ptrlv= (PullToRefreshListView) v.findViewById(R.id.ptrList);
        ptrlv.setAdapter(new ShopListAdapter(getActivity()));

        return v;
    }
}

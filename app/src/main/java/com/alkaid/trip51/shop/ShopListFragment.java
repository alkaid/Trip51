package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.booking.BookingFilterActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.enums.SortType;
import com.alkaid.trip51.model.response.ResMainHome;
import com.alkaid.trip51.model.shop.Area;
import com.alkaid.trip51.model.shop.Circle;
import com.alkaid.trip51.model.shop.SearchCondition;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.model.shop.ShopCategory;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.alkaid.trip51.widget.DefaultFilter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListFragment extends BaseFragment {
    private PullToRefreshListView shopListView;
    private ShopListAdapter shopListAdapter;
    private SearchCondition searchCondition;
    private int lastPageIndex=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchCondition= (SearchCondition) getArguments().get(BookingFilterActivity.BUNDLE_KEY_SEARCH_CONDITION);
        View v=inflater.inflate(R.layout.shop_list_fragment,container,false);
        shopListView = (PullToRefreshListView) v.findViewById(R.id.ptrList);
//        shopListView.getRefreshableView().addHeaderView(header);
        shopListAdapter=new ShopListAdapter(context);
        shopListView.setAdapter(shopListAdapter);
        loadData(LOAD_ON_ENTER, 1);

        shopListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    shopListAdapter.pauseImageLoad();
                } else {
                    shopListAdapter.resumeImageLoad();
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App.shopService().setCurrShopid(id);    //保存当前shopid

                Intent intent = new Intent(context, ShopDetailActivity.class);
                Shop shop = (Shop) shopListAdapter.getItem(position);
                intent.putExtra("currShop", shop);
                startActivity(intent);
            }
        });
        shopListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("载入中...");
                refreshView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                String label = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINESE).format(new Date());
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次更新：" + label);
                loadData(LOAD_ON_PULLDOWN, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("载入中...");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                String label = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINESE).format(new Date());
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次加载：" + label);
                loadData(LOAD_ON_PULLUP, lastPageIndex + 1);
            }

        });
        // 添加滑动到底部的监听器
        shopListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
            }
        });

        DefaultFilter defaultFilter=new DefaultFilter(inflater, context, v, new DefaultFilter.OnItemClickListener() {
            @Override
            public void onClick(SearchCondition.Result result) {
                //解析点击后传过来的类型
                switch (result.condType){
                    case ShopCategory:
                        if(null!=result.parentData){
                            searchCondition.base.shopType= (ShopType) result.parentData;
                            if(searchCondition.base.shopType==ShopType.ALL){
                                searchCondition.base.shopCategory=null;
                            }
                        }
                        if(null!=result.subData){
                            searchCondition.base.shopCategory=(ShopCategory)result.subData;
                        }
                        break;
                    case Location:
                        if(null!=result.parentData){
                            searchCondition.base.area= (Area) result.parentData;
                        }
                        if(null!=result.subData){
                            if(result.subData instanceof Circle){
                                searchCondition.base.circle= (Circle) result.subData;
                            }else if(result.subData instanceof SearchCondition.NearBy){
                                searchCondition.base.nearBy= (SearchCondition.NearBy) result.subData;
                                searchCondition.base.area=null;
                            }
                        }
                        break;
                    case Sort:
                        searchCondition.base.sortType= (SortType) result.parentData;
                        break;
                }
                //搜索
                loadData(LOAD_ON_SEARCH,1);
            }
        });
        return v;
    }
    private static final int LOAD_ON_ENTER=0;
    private static final int LOAD_ON_PULLDOWN=1;
    private static final int LOAD_ON_PULLUP=2;
    private static final int LOAD_ON_SEARCH=4;

    private void loadData(final int loadOnType,int pageindex){
        if(loadOnType==LOAD_ON_ENTER || loadOnType==LOAD_ON_SEARCH){
            showPdg();
        }
        final String tag="shoplist"+(int)(Math.random()*1000);
        // Do work to refresh the list here.
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        unBeSignform.put("cityid", App.locationService().getSelectCity().getCityid()+"");
        unBeSignform.put("gpscityid", App.locationService().getGpsCity().getCityid()+"");
        unBeSignform.put("shoptype", ShopType.RESTAURANT.code + "");
        unBeSignform.put("pageindex", (loadOnType == LOAD_ON_ENTER || loadOnType == LOAD_ON_PULLDOWN) ? "1" : pageindex + "");
        unBeSignform.put("pagesize", "20");
        unBeSignform.put("sortid", SortType.DEFAULT.code);
        unBeSignform.put("coordinates", App.locationService().getCoordinates());
        SearchCondition.putUpHttpSearchRequestParams(unBeSignform, searchCondition);

        boolean mShouldRefreshCache=false;
        switch (loadOnType){
            case LOAD_ON_ENTER:
                mShouldRefreshCache=false;
                break;
            case LOAD_ON_PULLDOWN:
                mShouldRefreshCache=true;
                break;
            case LOAD_ON_PULLUP:
                mShouldRefreshCache=false;
                break;
            case LOAD_ON_SEARCH:
                mShouldRefreshCache=true;
                break;
        }
        //需求变更 要求实时刷新 故这里不再使用缓存 每次都刷新
        mShouldRefreshCache=true;
        //请求首页
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL,mShouldRefreshCache,ResMainHome.class, MApiService.URL_SHOP_SEARCH, beSignForm, unBeSignform, new Response.Listener<ResMainHome>() {
            @Override
            public void onResponse(ResMainHome response) {
                //TODO 更新视图
                switch (loadOnType){
                    case LOAD_ON_ENTER:
                    case LOAD_ON_SEARCH:
                        lastPageIndex=1;
                        shopListAdapter.setData(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        dismissPdg();
                        break;
                    case LOAD_ON_PULLDOWN:
                        lastPageIndex=1;
                        shopListAdapter.setData(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        shopListView.onRefreshComplete();
                        break;
                    case LOAD_ON_PULLUP:
                        lastPageIndex++;
                        shopListAdapter.getData().addAll(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        shopListView.onRefreshComplete();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                switch (loadOnType){
                    case LOAD_ON_ENTER:
                    case LOAD_ON_SEARCH:
                        lastPageIndex=0;
                        shopListAdapter.getData().clear();
                        shopListAdapter.notifyDataSetChanged();
                        dismissPdg();
                        break;
                    case LOAD_ON_PULLDOWN:
//                        shopListView.getLoadingLayoutProxy().setRefreshingLabel("出错了");
//                        shopListView.setShowViewWhileRefreshing(false);
                        shopListView.onRefreshComplete();
                        break;
                    case LOAD_ON_PULLUP:
                        shopListView.onRefreshComplete();
                        break;
                }
                handleException(MApiService.parseError(error));
            }
        }), tag);
    }

}

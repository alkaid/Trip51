package com.alkaid.trip51.main.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.base.widget.view.AbstractSearchFragment;
import com.alkaid.trip51.base.widget.view.ButtonSearchBar;
import com.alkaid.trip51.booking.BookingFilterActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.location.CityListActivity;
import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.enums.SortType;
import com.alkaid.trip51.model.response.ResMainHome;
import com.alkaid.trip51.model.shop.Area;
import com.alkaid.trip51.model.shop.Circle;
import com.alkaid.trip51.model.shop.SearchCondition;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.model.shop.ShopCategory;
import com.alkaid.trip51.shop.ShopDetailActivity;
import com.alkaid.trip51.shop.ShopListActivity;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.alkaid.trip51.widget.DefaultFilter;
import com.alkaid.trip51.widget.MainSearchFragment;
import com.alkaid.trip51.widget.SlideShowView;
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
 * Created by alkaid on 2015/10/31.
 */
public class MainHomeFragment extends BaseFragment {
    private View layMainMenu,layOrder;
    private SlideShowView slideshowView;
    private TextView tvCity;
    private PullToRefreshListView shopListView;
    private ShopListAdapter shopListAdapter;
    private int lastPageIndex=1;
    private SearchCondition searchCondition;
    private ButtonSearchBar searchBar;
    private MainSearchFragment searchFragment;
    private static final int REQUEST_CODE_CHANGECITY=201;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.main_home_fragment,container,false);
        ViewGroup layBar = (ViewGroup) v.findViewById(R.id.title_bar_layout);
        View titleBar=inflater.inflate(R.layout.main_home_title_bar,layBar,true);

        View header=inflater.inflate(R.layout.main_home_header,null,false);

        slideshowView= (SlideShowView) header.findViewById(R.id.slideshowView);
        layMainMenu=header.findViewById(R.id.layMainMenu);
        layOrder=header.findViewById(R.id.layOrder);

        layOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BookingFilterActivity.class));
            }
        });

        searchCondition=new SearchCondition();
        searchCondition.base=new SearchCondition.Base();
        shopListView = (PullToRefreshListView) v.findViewById(R.id.ptrList);
        shopListView.getRefreshableView().addHeaderView(header);
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
                 Shop shop = (Shop) shopListAdapter.getItem(position-1);
                 intent.putExtra("currShop",shop);
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
        tvCity = (TextView) v.findViewById(R.id.city);
        tvCity.setText(App.locationService().getSelectCity().getCityname());
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivityForResult(new Intent(getActivity(), CityListActivity.class), REQUEST_CODE_CHANGECITY);
            }
        });
//        shopListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            private int mListViewFirstItem = 0;
//            //listView中第一项的在屏幕中的位置
//            private int mScreenY = 0;
//            //是否向上滚动
//            private boolean mIsScrollToUp = false;
//
//            @Override
//            public void onScrollStateChanged(AbsListView v, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        if (v.getFirstVisiblePosition() == 0) {
//                            slideshowView.setVisibility(View.VISIBLE);
//                            layMainMenu.setVisibility(View.VISIBLE);
//                        } else {
//                            slideshowView.setVisibility(View.GONE);
//                            layMainMenu.setVisibility(View.GONE);
//                        }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });

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

        //搜索条
        searchBar = (ButtonSearchBar)titleBar.findViewById(R.id.button_search_bar);
//        searchBar.setHint(SearchUtils.getHintId(parcelable));
//        searchBar.setBackgroundResource(R.drawable.home_topbar_search);
//        searchBar.getSearchTextView().setHintTextColor(0x99ffffff);
//        searchBar.getSearchIconView().setImageResource(R.drawable.ic_home_search);
        searchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener() {
            @Override
            public void onSearchRequested()
            {
                searchFragment = MainSearchFragment.newInstance((getActivity()));
//                    ((MainActivity)getContext()).registerSearchFragment();
                searchFragment.setOnSearchFragmentListener(new AbstractSearchFragment.OnSearchFragmentListener() {
                    @Override
                    public void onSearchFragmentDetach() {

                    }

                    @Override
                    public void startSearch(String keyword) {
                        searchCondition=new SearchCondition();
                        searchCondition.advance.keyword=keyword;
                        Intent intent=new Intent(context, ShopListActivity.class);
                        intent.putExtra(BookingFilterActivity.BUNDLE_KEY_SEARCH_CONDITION,searchCondition);
                        startActivity(intent);
                    }
                });
            }
        });

//        //TODO 以下是测试用的筛选 要删除
//        View view2 = inflater.inflate(R.layout.popup2,null);
//        ListView pLv = (ListView) view2.findViewById(R.id.parent_lv);
//        final ListView cLv = (ListView) view2.findViewById(R.id.child_lv);
//        List<String> pList = new ArrayList<>();
//        final List<List<String>> cList = new ArrayList<>();
//        for(int i = 0; i < 10; i ++) {
//            pList.add("p" + i);
//            List<String> t = new ArrayList<>();
//            for(int j = 0; j < 15; j++) {
//                t.add(pList.get(i) + "-c" + j);
//            }
//            cList.add(t);
//        }
//        final List cValues = new ArrayList<>();
//        cValues.addAll(cList.get(0));
//        final PopupAdapter pAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,pList,R.color.base_start_color_default,R.color.base_start_color_pressed);
//        final PopupAdapter cAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,cValues,R.color.base_start_color_default,R.color.base_start_color_pressed);
//        pAdapter.setPressPostion(0);
//        pLv.setAdapter(pAdapter);
//        cLv.setAdapter(cAdapter);
//        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pAdapter.setPressPostion(position);
//                pAdapter.notifyDataSetChanged();
//                cValues.clear();
//                cValues.addAll(cList.get(position));
//                cAdapter.notifyDataSetChanged();
//                cAdapter.setPressPostion(-1);
//                cLv.setSelection(0);
//            }
//        });
//        final PopupButton btn1 = (PopupButton) v.findViewById(R.id.popbtn1);
//        final PopupButton btn2 = (PopupButton) v.findViewById(R.id.popbtn2);
//        final PopupButton btn3 = (PopupButton) v.findViewById(R.id.popbtn3);
//        btn1.setPopupView(view2);
////        btn2.setPopupView(view2);
////        btn3.setPopupView(view2);
//        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                cAdapter.setPressPostion(position);
//                cAdapter.notifyDataSetChanged();
//                btn2.setText(cValues.get(position) + "");
//                btn2.hidePopup();
//            }
//        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        if(null!=searchFragment && searchFragment.isVisible()){
            searchFragment.onBackPressed();
            return true;
        }
        return false;
    }

    private static final int LOAD_ON_ENTER=0;
    private static final int LOAD_ON_PULLDOWN=1;
    private static final int LOAD_ON_PULLUP=2;
    private static final int LOAD_ON_SEARCH=4;

    private void loadData(final int loadOnType,int pageindex){
        if(loadOnType==LOAD_ON_ENTER || loadOnType==LOAD_ON_SEARCH){
            showPdg();
        }
        final String tag="mainhome"+(int)(Math.random()*1000);
        // Do work to refresh the list here.
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        unBeSignform.put("cityid", App.locationService().getSelectCity().getCityid()+"");
        unBeSignform.put("gpscityid", App.locationService().getGpsCity().getCityid()+"");
        unBeSignform.put("shoptype", ShopType.RESTAURANT.code + "");
        unBeSignform.put("pageindex", (loadOnType==LOAD_ON_ENTER||loadOnType==LOAD_ON_PULLDOWN)?"1":pageindex+"");
        unBeSignform.put("pagesize", "20");
        unBeSignform.put("sortid", SortType.DEFAULT.code);
        unBeSignform.put("coordinates", App.locationService().getCoordinates());
        SearchCondition.putUpHttpSearchRequestParams(unBeSignform,searchCondition);

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
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL,mShouldRefreshCache,ResMainHome.class, MApiService.URL_MAIN_HOME, beSignForm, unBeSignform, new Response.Listener<ResMainHome>() {
            @Override
            public void onResponse(ResMainHome response) {
                //TODO 更新视图
                switch (loadOnType){
                    case LOAD_ON_ENTER:
                    case LOAD_ON_SEARCH:
                        lastPageIndex=1;
                        shopListAdapter.setData(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        slideshowView.load(response.advlist);
                        dismissPdg();
                        break;
                    case LOAD_ON_PULLDOWN:
                        lastPageIndex=1;
                        shopListAdapter.setData(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        slideshowView.load(response.advlist);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHANGECITY && resultCode== Activity.RESULT_OK){
            tvCity.setText(App.locationService().getSelectCity().getCityname());
            loadData(LOAD_ON_ENTER, 1);
        }
    }
}

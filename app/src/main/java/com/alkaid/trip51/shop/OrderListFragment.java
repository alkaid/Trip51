package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.response.ResOrderList;
import com.alkaid.trip51.shop.adapter.OrderListAdapter;
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
 * Created by alkaid on 2015/11/8.
 */
public class OrderListFragment extends BaseFragment {
    public static final String BUNDLE_KEY_CONDITION_ORDER_STATUS="BUNDLE_KEY_CONDITION_ORDER_STATUS";
    private PullToRefreshListView ptrlv;
    private int conditionOrderStatus;
    private OrderListAdapter orderListAdapter;
    private int lastPageIndex=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUseUmengData=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.default_pull_list_view, container, false);
        ptrlv = (PullToRefreshListView) v.findViewById(R.id.ptrList);
        orderListAdapter=new OrderListAdapter(getActivity());
        ptrlv.setAdapter(orderListAdapter);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context,OrderDetailActivity.class));
            }
        });
        conditionOrderStatus=getArguments().getInt(BUNDLE_KEY_CONDITION_ORDER_STATUS);
        //如果是PageView中的第一个页面 则在这里加载数据 否则到onLazyLoad里加载
        if(conditionOrderStatus== NetDataConstants.CONDITION_ORDER_STATUS_NORMAL) {
            loadData(conditionOrderStatus, LOAD_ON_ENTER, 1);
        }

        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                ResOrderList.Order order = (ResOrderList.Order) orderListAdapter.getItem(position);
                intent.putExtra(OrderDetailActivity.BUNDLE_KEY_ORDERNO, order.getOrderno());
                startActivity(intent);
            }
        });
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("载入中...");
                refreshView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                String label = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINESE).format(new Date());
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次更新：" + label);
                loadData(conditionOrderStatus, LOAD_ON_PULLDOWN, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setRefreshingLabel("载入中...");
                refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                refreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                String label = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINESE).format(new Date());
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次加载：" + label);
                loadData(conditionOrderStatus, LOAD_ON_PULLUP, lastPageIndex + 1);
            }

        });
        // 添加滑动到底部的监听器
        ptrlv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
            }
        });
        return v;
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        conditionOrderStatus=getArguments().getInt(BUNDLE_KEY_CONDITION_ORDER_STATUS);
        //如果不是PageView中的第一个页面 则在这里加载数据
        if(conditionOrderStatus!= NetDataConstants.CONDITION_ORDER_STATUS_NORMAL) {
            loadData(conditionOrderStatus, LOAD_ON_ENTER, 1);
        }
    }

    //TODO 三级Fragment收不到OnActivityResult,需在父Fragment中实现
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode== Activity.RESULT_OK && requestCode== AccountService.REQUEST_CODE_LOGIN) {
//            loadData(conditionOrderStatus, LOAD_ON_ENTER, 1);
//        }
//    }

    private static final int LOAD_ON_ENTER=0;
    private static final int LOAD_ON_PULLDOWN=1;
    private static final int LOAD_ON_PULLUP=2;
    private static final int LOAD_ON_SEARCH=4;

    private void loadData(int orderStatus,final int loadOnType,int pageindex){
        if(!checkLogined()){
            toastShort("请登录后再查看个人订单");
            return;
        }
        if(loadOnType==LOAD_ON_ENTER || loadOnType==LOAD_ON_SEARCH){
            showPdg();
        }
        final String tag="orderList"+(int)(Math.random()*1000);
        // Do work to refresh the list here.
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("orderstatus", orderStatus + "");
        unBeSignform.put("pageindex", (loadOnType == LOAD_ON_ENTER || loadOnType == LOAD_ON_PULLDOWN) ? "1" : pageindex + "");
        unBeSignform.put("pagesize", "20");
//        unBeSignform.put("sortid", SortType.DEFAULT.code);

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
        //请求首页
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL,mShouldRefreshCache,ResOrderList.class, MApiService.URL_ORDER_LIST, beSignForm, unBeSignform, new Response.Listener<ResOrderList>() {
            @Override
            public void onResponse(ResOrderList response) {
                //TODO 更新视图
                switch (loadOnType){
                    case LOAD_ON_ENTER:
                    case LOAD_ON_SEARCH:
                        lastPageIndex=1;
                        orderListAdapter.setData(response.getData());
                        orderListAdapter.notifyDataSetChanged();
                        dismissPdg();
                        break;
                    case LOAD_ON_PULLDOWN:
                        lastPageIndex=1;
                        orderListAdapter.setData(response.getData());
                        orderListAdapter.notifyDataSetChanged();
                        ptrlv.onRefreshComplete();
                        break;
                    case LOAD_ON_PULLUP:
                        lastPageIndex++;
                        orderListAdapter.getData().addAll(response.getData());
                        orderListAdapter.notifyDataSetChanged();
                        ptrlv.onRefreshComplete();
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
                        orderListAdapter.getData().clear();
                        orderListAdapter.notifyDataSetChanged();
                        dismissPdg();
                        break;
                    case LOAD_ON_PULLDOWN:
//                        shopListView.getLoadingLayoutProxy().setRefreshingLabel("出错了");
//                        shopListView.setShowViewWhileRefreshing(false);
                        ptrlv.onRefreshComplete();
                        break;
                    case LOAD_ON_PULLUP:
                        ptrlv.onRefreshComplete();
                        break;
                }
                handleException(new TradException(error.getMessage(),error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }
}
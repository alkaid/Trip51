package com.alkaid.trip51.booking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alkaid.base.common.DateUtil;
import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.enums.SeatType;
import com.alkaid.trip51.model.request.ReqOrderInfo;
import com.alkaid.trip51.model.response.ResOrder;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.OrderDetailActivity;
import com.alkaid.trip51.shop.ShopDetailActivity;
import com.alkaid.trip51.widget.Operator;
import com.alkaid.trip51.widget.WheelView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/7.
 */
public class BookingActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = getClass().getName();

    private Shop currShop;
    private ReqOrderInfo order;

    /**
     * 界面选择的数据
     */
    private int personNum;
    private String dinnerTime;
    private int roomNum;
    private SeatType seatType;
    private float price;
    private int sex;
    private String mobile;
    private boolean isReplaceother;
    private String otherMobile;
    private String otherUserName;

    /**
     * 点击的view
     */
    private RelativeLayout rlPersonNum;
    private RelativeLayout rlDinnerTime;
    private RadioGroup seatRadioGroup;
    private Operator opSeatNum;
    private RelativeLayout rlTotalPrice;
    private RadioGroup sexRadioGroup;
    private EditText etMobile;
    private RadioGroup otherSexRadioGroup;
    private EditText etOtherMobile;
    private View btnSure;
    private ToggleButton tbHelpBooking;
    private WheelView mWheelView;//选择弹出的选择空间
    private RelativeLayout rlSelectContent;//选择的内容
    private LinearLayout rlSelectItem;//用于选择的父layout

    private DatePicker dpPicker;

    /**
     * ui显示需要的数据
     */
    private String[] personNums = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private String[] timeSetLables = new String[]{"8:00AM - 9:00AM", "9:00AM - 10:00AM", "10:00AM - 11:00AM", "11:00AM - 12:00AM", "12:00AM - 1:00PM", "1:00PM - 2:00PM", "2:00PM - 3:00PM", "3:00PM - 4:00PM", "4:00PM - 5:00PM", "5:00PM - 6:00PM", "6:00PM - 7:00PM", "7:00PM - 8:00PM", "8:00PM - 9:00PM", "9:00PM - 10:00PM"};

    private static final String WHEEL_VIEW_DATA = "9001";
    private static final String WHEEL_VIEW_OFFSET = "9002";
    private static final String WHEEL_VIEW_SELECTED = "9003";

    /**
     * 常量标记Handler
     */
    private int currentWheelView = -1;//当前wheelView是属于哪个
    private static final int BOOKING_PERSON_LAYOUT_TAG = 8001;
    private static final int BOOKING_TIME_LAYOUT_TAG = 8002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currShop = (Shop) getIntent().getSerializableExtra(ShopDetailActivity.BUNDLE_KEY_SHOP);
        setContentView(R.layout.activity_booking);
        initData();
        initTitleBar();
        initView();

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
            }
        });

        Calendar d = Calendar.getInstance(Locale.CHINA);
        d.setTime(new Date());
        //设置日历的时间，把一个新建Date实例myDate传入
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        dpPicker.init(year, month, day, /*new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy年MM月dd日  HH:mm");
                Toast.makeText(context,format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
        }*/null);

    }

    private void initView() {
        rlPersonNum = (RelativeLayout) findViewById(R.id.rl_person_num);
        rlDinnerTime = (RelativeLayout) findViewById(R.id.rl_time);
        seatRadioGroup = (RadioGroup) findViewById(R.id.rgSeatType);
        opSeatNum = (Operator) findViewById(R.id.op_room_num);
        rlTotalPrice = (RelativeLayout) findViewById(R.id.rl_total_price);
        sexRadioGroup = (RadioGroup) findViewById(R.id.rgSex);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        tbHelpBooking = (ToggleButton) findViewById(R.id.tb_help_booking);
        otherSexRadioGroup = (RadioGroup) findViewById(R.id.rg_other_sex);
        etOtherMobile = (EditText) findViewById(R.id.et_other_mobile);
        btnSure = findViewById(R.id.btnSure);
        mWheelView = (WheelView) findViewById(R.id.wv_select_content);
        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
        rlSelectContent = (RelativeLayout) findViewById(R.id.rl_select_content);
        rlSelectItem = (LinearLayout) findViewById(R.id.rl_select_item);
        /*监听部分*/
        rlPersonNum.setOnClickListener(this);
        rlDinnerTime.setOnClickListener(this);
        seatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });


    }

    private void initData() {
        order = new ReqOrderInfo();
        if(currShop == null){
            LogUtil.e(TAG,"无店铺信息");
            return;
        }
       // App.shoppingCartService()
        order.setDinnertime(DateUtil.formatDateString(new Date(), NetDataConstants.DATETIME_FORMAT));
        order.setIscontainfood(NetDataConstants.TRUE);
        order.setIsreplaceother(NetDataConstants.FALSE);
        order.setMobile("18575534171");
        order.setOthermobile(null);
        order.setOthersex(NetDataConstants.SEX_FEMALE);
        order.setOtherusername(null);
        order.setPersonnum(4);
        order.setRoomnum(1);
        order.setRoomtype(SeatType.HALL.code);
        order.setSex(NetDataConstants.SEX_FEMALE);
        order.setShopid(39);
        order.setFoodamount(0.01f);
        order.setOrderamount(0.01f);
        List<ReqOrderInfo.ReqFood> foods = new ArrayList<ReqOrderInfo.ReqFood>();
        Food orginFood = new Food();
        orginFood.setFoodid(34);
        orginFood.setFoodname("test");
        orginFood.setPrice(0.01f);
        ReqOrderInfo.ReqFood food = new ReqOrderInfo.ReqFood(orginFood, 1);
        foods.add(food);
        order.setFoods(foods);
    }


    private void booking() {
        //检查登录
        if (!checkLogined()) {
            return;
        }
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        //构造订单伪数据
        String orderJson = new Gson().toJson(order);
        LogUtil.v("orderJson:" + orderJson);
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("orderinfo", orderJson);
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag = "booking" + (int) (Math.random() * 1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, false, ResOrder.class, MApiService.URL_BOOKING, beSignForm, unBeSignform, new Response.Listener<ResOrder>() {
            @Override
            public void onResponse(ResOrder response) {
                dismissPdg();
                //TODO 下单成功 刷新UI
                String orderNo = response.getOuttradeno();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.BUNDLE_KEY_ORDERNO, orderNo);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }

    private void initTitleBar() {
        View layTitleBar = findViewById(R.id.title_bar);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        View btnLeft = findViewById(R.id.btn_back_wx);
        View btnRight = findViewById(R.id.notify);
        tvTitle.setText("预订信息");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_person_num:
                if (rlSelectItem != null) {
                    mWheelView.setItems((ArrayList<String>) Arrays.asList(personNums));
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(2);
                    mWheelView.setVisibility(View.VISIBLE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_PERSON_LAYOUT_TAG;
                    dpPicker.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_time:
                if (rlSelectItem != null) {
                    mWheelView.setItems((ArrayList<String>) Arrays.asList(timeSetLables));
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(3);
                    mWheelView.setVisibility(View.GONE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_TIME_LAYOUT_TAG;
                    dpPicker.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnSure:
                break;
            case R.id.rl_complete:
                if (currentWheelView == R.id.rl_time && dpPicker.getVisibility() == View.VISIBLE) {
                    dpPicker.setVisibility(View.GONE);
                    mWheelView.setVisibility(View.VISIBLE);
                    break;

                }
        }
    }

    private static class ViewHolder {
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
        private TextView itemValue;
    }

}

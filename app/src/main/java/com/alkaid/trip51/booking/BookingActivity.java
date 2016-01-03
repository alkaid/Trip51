package com.alkaid.trip51.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.config.TimeSet;
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

import java.text.SimpleDateFormat;
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
 * 订单提交界面
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
    private int isReplaceother;
    private String otherMobile;
    private String otherUserName;
    private int isContainFood;

    /**
     * 点击的view
     */
    private RelativeLayout rlPersonNum;
    private TextView tvPersonNum;
    private RelativeLayout rlDinnerTime;
    private TextView tvBookingTime;
    private RadioGroup seatRadioGroup;
    private Operator opSeatNum;
    private RelativeLayout rlTotalPrice;
    private TextView tvTotalPrice;
    private RadioGroup sexRadioGroup;
    private EditText etMobile;
    private RadioGroup otherSexRadioGroup;
    private EditText etOtherMobile;
    private View btnSure;
    private ToggleButton tbHelpBooking;
    private WheelView mWheelView;//选择弹出的选择空间
    private RelativeLayout rlSelectContent;//选择的内容
    private LinearLayout rlSelectItem;//用于选择的父layout
    private RelativeLayout rlComplete;//

    private DatePicker dpPicker;
    private TimeSet selectedTimeSet;

    /**
     * ui显示需要的数据
     */
    private String[] personNums = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//    private String[] timeSetLables = new String[]{"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
    private List<String> timeSetLables=new ArrayList<>();

    private static final String WHEEL_VIEW_DATA = "9001";
    private static final String WHEEL_VIEW_OFFSET = "9002";
    private static final String WHEEL_VIEW_SELECTED = "9003";


    /**
     * 常量标记Handler
     */
    private int currentWheelView = -1;//当前wheelView是属于哪个
    private static final int BOOKING_PERSON_NUM_LAYOUT_TAG = 8001;
    private static final int BOOKING_TIME_LAYOUT_TAG = 8002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currShop = (Shop) getIntent().getSerializableExtra(ShopDetailActivity.BUNDLE_KEY_SHOP);
        setContentView(R.layout.activity_booking);
        initTitleBar();
        initView();
        initData();
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
        tvPersonNum = (TextView) findViewById(R.id.tvPersonNum);
        rlDinnerTime = (RelativeLayout) findViewById(R.id.rl_time);
        tvBookingTime = (TextView) findViewById(R.id.tvBookingTimeValue);
        seatRadioGroup = (RadioGroup) findViewById(R.id.rgSeatType);
        opSeatNum = (Operator) findViewById(R.id.op_room_num);
        rlTotalPrice = (RelativeLayout) findViewById(R.id.rl_total_price);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
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
        rlComplete = (RelativeLayout) findViewById(R.id.rl_complete);
        /*监听部分*/
        rlPersonNum.setOnClickListener(this);
        rlDinnerTime.setOnClickListener(this);
        rlComplete.setOnClickListener(this);
        opSeatNum.setOperationCallback(new Operator.OperationListener() {
            @Override
            public void onAddClick(int i) {

            }

            @Override
            public void onSubClick(int i) {

            }

            @Override
            public void onTextChange(int i) {

            }
        });

    }

    private void initData() {
        tvPersonNum.setText("请选择参与人数");
        tvBookingTime.setText("请选择预定日期");
        float totalPrice = App.shoppingCartService().getCartTotalPrice(currShop.getShopid());
        tvTotalPrice.setText(totalPrice+"");
        for(TimeSet t:currShop.getTimesets()){
            timeSetLables.add(t.timepart);
        }
    }

    private void setOrder() {
        if (currShop == null) {
            LogUtil.e(TAG, "无店铺信息");
            return;
        }
        order = new ReqOrderInfo();
        switch (seatRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rbHall:
                seatType = SeatType.HALL;
                break;
            case R.id.rbLounge:
                seatType = SeatType.LOUNGE;
                break;
            case R.id.rbPrivateRoom:
                seatType = SeatType.PRIVATE_ROOM;
                break;
        }
        switch (sexRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rbFemale:
                sex = NetDataConstants.SEX_FEMALE;
                break;
            case R.id.rbMale:
                sex = NetDataConstants.SEX_MALE;
                break;
        }
        switch (otherSexRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rbOtherFemale:
                sex = NetDataConstants.SEX_FEMALE;
                break;
            case R.id.rbOtherMale:
                sex = NetDataConstants.SEX_MALE;
                break;
        }
        if(opSeatNum!=null){
            roomNum = opSeatNum.selectedCount;
        }
        if (tbHelpBooking.isChecked()) {
            isReplaceother = NetDataConstants.TRUE;
        } else {
            isReplaceother = NetDataConstants.FALSE;
        }
        mobile = etMobile.getText().toString();
        otherMobile = etMobile.getText().toString();
        /**组装order*/
        List<Food> foods = App.shoppingCartService().getCart().get(currShop.getShopid());
        if (foods != null && foods.size() > 0) {
            isContainFood = NetDataConstants.TRUE;
        } else {
            isContainFood = NetDataConstants.FALSE;
        }
        float totalPrice = App.shoppingCartService().getCartTotalPrice(currShop.getShopid());
        if(TextUtils.isEmpty(dinnerTime)) {
            order.setDinnertime(DateUtil.formatDateString(new Date(), NetDataConstants.DATETIME_FORMAT));
        }else{
            order.setDinnertime(dinnerTime);
        }
        order.setIscontainfood(isContainFood);
        order.setIsreplaceother(isReplaceother);
        order.setMobile(mobile);
        order.setOthermobile(otherMobile);
        order.setOthersex(NetDataConstants.SEX_FEMALE);
        order.setOtherusername(otherUserName);
        order.setPersonnum(personNum);
        order.setRoomnum(roomNum);
        order.setRoomtype(seatType.code);
        order.setSex(sex);
        order.setShopid(currShop.getShopid());
        order.setFoodamount(totalPrice);
        order.setOrderamount(totalPrice);
        order.setFoods(foods);
        order.setTimeid(0);
//        order.setTimeid(selectedTimeSet.timeid);
    }

    private void booking() {
        //检查登录
        if (!checkLogined()) {
            return;
        }
        //检查必选项
        if(selectedTimeSet==null){
            toastLong("必须选择预订时间");
//            return;
        }
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        setOrder();
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
                intent.putExtra(OrderDetailActivity.BUNDLE_KEY_SHOPID,currShop.getShopid());
                //下单成功 清除该店的购物车
                App.shoppingCartService().getCart().remove(currShop.getShopid());
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(MApiService.parseError(error));
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
                    mWheelView.setItems( Arrays.asList(personNums));
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(2);
                    mWheelView.setVisibility(View.VISIBLE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_PERSON_NUM_LAYOUT_TAG;
                    dpPicker.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_time:
                if (rlSelectItem != null) {
                    mWheelView.setItems( timeSetLables);
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(3);
                    mWheelView.setVisibility(View.GONE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_TIME_LAYOUT_TAG;
                    dpPicker.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnSure:
                booking();
                break;
            case R.id.rl_complete:
                if (currentWheelView == BOOKING_TIME_LAYOUT_TAG && dpPicker.getVisibility() == View.VISIBLE) {
                    dpPicker.setVisibility(View.GONE);
                    mWheelView.setVisibility(View.VISIBLE);
                    return;
                }
                rlSelectContent.setVisibility(View.GONE);
                setCurrentWheelView();
                updateItem();
                break;
        }
    }

    private void setCurrentWheelView() {
        switch (currentWheelView) {
            case BOOKING_TIME_LAYOUT_TAG:
                Calendar calendar = Calendar.getInstance();
                calendar.set(dpPicker.getYear(), dpPicker.getMonth(), dpPicker.getDayOfMonth());
                dinnerTime = new SimpleDateFormat(NetDataConstants.DATE_FORMAT).format(calendar.getTimeInMillis()) + " " + mWheelView.getSeletedItem();
                selectedTimeSet=currShop.getTimesets().get(mWheelView.getSeletedIndex());
                break;
            case BOOKING_PERSON_NUM_LAYOUT_TAG:
                personNum = Integer.parseInt(mWheelView.getSeletedItem());
                break;
        }
    }

    private void updateItem() {
        switch (currentWheelView) {
            case BOOKING_TIME_LAYOUT_TAG:
                if(tvBookingTime!=null){
                    tvBookingTime.setText(dinnerTime);
                }
                break;
            case BOOKING_PERSON_NUM_LAYOUT_TAG:
                if (tvPersonNum != null) {
                    tvPersonNum.setText(personNum+"");
                }
                break;
        }
    }

    private static class ViewHolder {
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
        private TextView itemValue;
    }

}

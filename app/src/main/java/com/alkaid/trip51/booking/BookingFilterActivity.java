package com.alkaid.trip51.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.config.TimeSet;
import com.alkaid.trip51.model.enums.PersonNumType;
import com.alkaid.trip51.model.shop.Cuisine;
import com.alkaid.trip51.model.shop.SearchCondition;
import com.alkaid.trip51.model.shop.ShopCategory;
import com.alkaid.trip51.shop.ShopListActivity;
import com.alkaid.trip51.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alkaid on 2015/11/7.
 */
public class  BookingFilterActivity extends BaseActivity implements View.OnClickListener {
    public static final String BUNDLE_KEY_SEARCH_CONDITION="BUNDLE_KEY_SEARCH_CONDITION";
    /**相关布局*/
    private ViewGroup layContent, laySeat;//整个layout，餐桌layout
//    private Operator opNums;//餐桌数选项按钮
    private LayoutInflater inflater;
    private View btnSearch;//搜索按钮
    private LinearLayout rlSelectItem;//用于选择的父layout
    private RelativeLayout rlSelectContent;//选择的内容
    private WheelView mWheelView;//选择弹出的选择空间
    private RelativeLayout rlComplete;//
    private DatePicker dpPicker;
    private RadioGroup rgSeatType;
    private RadioButton rbHall,rbLounge,rbPrivateRoom;
    private EditText etKeywords;

    private ArrayList<ViewHolder> viewHolders;

    /***被选定的数据*/
    private String booking_time="选择时间";//预定时间的值，空为还未选定
    private String booking_person_num="选择人数";//预定人数
    private String booking_location="选择商圈";//选择商圈
    private String booking_cuisine="选择菜系";//预定菜系
    private String booking_shoptype="选择餐厅类型";//预定店铺类型
//    private TimeSet selectedTimeSet;
//    private PersonNumType selectedPersonNumType;
//    private Cuisine selectdCu
    private SearchCondition searchCondition;

    private int booking_seat_type = 0;//餐桌类型，
    private int booking_seat_num = 1;//预定餐桌数

    private int currentWheelView = -1;//当前wheelView是属于哪个

    private static final int BOOKING_TIME_LAYOUT_TAG = 1001;
    private static final int BOOKING_PERSON_NUM_LAYOUT_TAG = 1002;
    private static final int BOOKING_SEAT_TYPE_LAYOUT_TAG = 1003;
    private static final int BOOKING_LOCATION_LAYOUT_TAG = 1004;
    private static final int BOOKING_CUISINE_LAYOUT_TAG = 1005;
    private static final int BOOKING_SHOPTYPE_LAYOUT_TAG = 1006;

//    private static String[] times = new String[]{"2015-08-31 10：30~2015-08-31 11：30"
//            , "2015-08-31 11：30~~2015-08-31 12：30", "2015-08-31 13：30~2015-08-31 14：30", "2015-08-31 15：30~2015-08-31 16：30", "2015-08-31 16：30~2015-08-31 17：30", "2015-08-31 17：30~2015-08-31 18：30", "2015-08-31 18：30~2015-08-31 19：30"
//    };
//
//    private static final String[] cuisines = new String[]{""
//            , "火锅（1080）", "日本料理（987）", "东北菜（340）","粤菜（2040）"};
//
//    private static final String[] persons = new String[]{"1-2人"
//            , "2-4人", "4-6人", "6-8人", "8-12人", "12人以上"};
//
//    private static final String[] shopTypes = new String[]{"主题餐厅"
//            , "欧式浪漫餐厅", "商务简约餐厅", "大排档"};
    List<TimeSet> timeSets=new ArrayList<>();
    private List<String> timeSetLables=new ArrayList<>();
    List<Cuisine> cuisines=new ArrayList<>();
    private List<String> cuisineLables=new ArrayList<>();
    private List<String> personLables=new ArrayList<>();
    private List<ShopCategory> shopCategories=new ArrayList<>();
    private List<String> shopCategoryLables=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_booking);
        searchCondition=new SearchCondition();
        searchCondition.base=new SearchCondition.Base();
        searchCondition.advance=new SearchCondition.Advance();
        intiData();
        initTitleBar();
        etKeywords= (EditText) findViewById(R.id.etKeywords);
        btnSearch = findViewById(R.id.btnSearch);
        layContent = (ViewGroup) findViewById(R.id.content);
        rlComplete = (RelativeLayout) findViewById(R.id.rl_complete);
        rlComplete.setOnClickListener(this);
        inflater = LayoutInflater.from(context);
        initItem(R.drawable.booking_ic_time, "时间",booking_time, BOOKING_TIME_LAYOUT_TAG);
        initItem(R.drawable.booking_ic_person, "人数", "选择人数", BOOKING_PERSON_NUM_LAYOUT_TAG);
        laySeat = (ViewGroup) inflater.inflate(R.layout.item_booking_filter_seat, layContent, false);
        layContent.addView(laySeat);
        rgSeatType= (RadioGroup) findViewById(R.id.rgSeatType);
        initItem(R.drawable.booking_ic_location, "商圈", "大雁塔", BOOKING_LOCATION_LAYOUT_TAG);
        initItem(R.drawable.booking_ic_cuisine, "菜系", "粤菜", BOOKING_CUISINE_LAYOUT_TAG);
        initItem(R.drawable.booking_ic_shoptype, "就餐类型", "主题餐厅", BOOKING_SHOPTYPE_LAYOUT_TAG);



        rlSelectContent = (RelativeLayout) findViewById(R.id.rl_select_content);
        rlSelectItem = (LinearLayout) findViewById(R.id.rl_select_item);
        mWheelView = (WheelView) findViewById(R.id.wv_select_content);
        dpPicker= (DatePicker) findViewById(R.id.dpPicker);
        Calendar d = Calendar.getInstance(Locale.CHINA);
        d.setTime(new Date());
        //设置日历的时间，把一个新建Date实例myDate传入
        int year=d.get(Calendar.YEAR);
        int month=d.get(Calendar.MONTH);
        int day=d.get(Calendar.DAY_OF_MONTH);
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rgSeatType.getCheckedRadioButtonId()){
                    case R.id.rbHall:
                        searchCondition.advance.desktype=NetDataConstants.SEAT_TYPE_HALL;
                        break;
                    case R.id.rbLounge:
                        searchCondition.advance.desktype=NetDataConstants.SEAT_TYPE_LOUNGE;
                        break;
                    case R.id.rbPrivateRoom:
                        searchCondition.advance.desktype=NetDataConstants.SEAT_TYPE_PRIVATE_ROOM;
                        break;
                }
                String keywords=etKeywords.getText().toString().trim();
                if(!TextUtils.isEmpty(keywords)){
                    searchCondition.advance.keyword=keywords;
                }
                Intent intent=new Intent(context, ShopListActivity.class);
                intent.putExtra(BUNDLE_KEY_SEARCH_CONDITION,searchCondition);
                startActivity(intent);
                finish();
            }
        });
    }

    private void intiData() {
        //TODO 商圈还没做
        viewHolders = new ArrayList<>();
        timeSets = App.locationService().getCondition().getTimesets();
        for(TimeSet t:timeSets){
            timeSetLables.add(t.timepart);
        }
        cuisines=App.locationService().getCondition().getShopcuisinelist();
        for(Cuisine c:cuisines){
            cuisineLables.add(c.getCuisinename());
        }
        shopCategories=App.locationService().getCondition().getShopcategorylist();
        for(ShopCategory sc:shopCategories){
            shopCategoryLables.add(sc.getCategoryname());
        }
        for(PersonNumType pt:PersonNumType.values()){
            personLables.add(pt.desc);
        }
    }

    private void initTitleBar() {
        View layTitleBar = findViewById(R.id.title_bar);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        View btnLeft = findViewById(R.id.btn_back_wx);
        View btnRight = findViewById(R.id.notify);
        tvTitle.setText("餐厅预订");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initItem(int resIconId, String name, String value, int tag) {
        ViewHolder holder = new ViewHolder();
        holder.lay = (ViewGroup) inflater.inflate(R.layout.item_booking_filter, layContent, false);
        holder.lay.setOnClickListener(this);
        holder.lay.setTag(tag);
        holder.icon = (ImageView) holder.lay.findViewById(R.id.ivItemIcon);
        holder.itemName = (TextView) holder.lay.findViewById(R.id.tvItemName);
        holder.itemValue = (TextView) holder.lay.findViewById(R.id.tvItemValue);
        holder.icon.setImageResource(resIconId);
        holder.itemName.setText(name);
        viewHolders.add(holder);
        layContent.addView(holder.lay);
        updateItem();
    }

    private void updateItem(){
        for(ViewHolder holder:viewHolders){
            int tag = (int)holder.lay.getTag();
            switch (tag){
                case BOOKING_TIME_LAYOUT_TAG:
                    holder.itemValue.setText(booking_time);
                    break;
                case BOOKING_CUISINE_LAYOUT_TAG:
                    holder.itemValue.setText(booking_cuisine);
                    break;
                case BOOKING_LOCATION_LAYOUT_TAG:
                    holder.itemValue.setText(booking_location);
                    break;
                case BOOKING_PERSON_NUM_LAYOUT_TAG:
                    holder.itemValue.setText(booking_person_num);
                    break;
                case BOOKING_SHOPTYPE_LAYOUT_TAG:
                    holder.itemValue.setText(booking_shoptype);
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        int tag = -1;
        if(v.getTag()!=null) {
            tag = (Integer) v.getTag();
        }
        switch (tag) {
            case BOOKING_TIME_LAYOUT_TAG:
                if (rlSelectItem != null) {
                    mWheelView.setItems(timeSetLables);
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(3);
                    mWheelView.setVisibility(View.GONE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_TIME_LAYOUT_TAG;
                    dpPicker.setVisibility(View.VISIBLE);
                }
                break;
            case BOOKING_CUISINE_LAYOUT_TAG:
                if (rlSelectItem != null) {
                    mWheelView.setItems(cuisineLables);
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(2);
                    mWheelView.setVisibility(View.VISIBLE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_CUISINE_LAYOUT_TAG;
                    dpPicker.setVisibility(View.GONE);
                }
                break;
            case BOOKING_LOCATION_LAYOUT_TAG:
                dpPicker.setVisibility(View.GONE);
                break;
            case BOOKING_PERSON_NUM_LAYOUT_TAG:
                if (rlSelectItem != null) {
                    mWheelView.setItems(personLables);
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(2);
                    mWheelView.setVisibility(View.VISIBLE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_PERSON_NUM_LAYOUT_TAG;
                    dpPicker.setVisibility(View.GONE);
                }
                break;
            case BOOKING_SEAT_TYPE_LAYOUT_TAG:
                break;
            case BOOKING_SHOPTYPE_LAYOUT_TAG:
                if (rlSelectItem != null) {
                    mWheelView.setItems(shopCategoryLables);
                    mWheelView.setOffset(1);
                    mWheelView.setSeletion(3);
                    mWheelView.setVisibility(View.VISIBLE);
                    rlSelectContent.setVisibility(View.VISIBLE);
                    currentWheelView = BOOKING_SHOPTYPE_LAYOUT_TAG;
                    dpPicker.setVisibility(View.GONE);
                }
                break;

        }
        switch (v.getId()){
            case R.id.rl_complete:
                if(currentWheelView==BOOKING_TIME_LAYOUT_TAG && dpPicker.getVisibility()==View.VISIBLE){
                    dpPicker.setVisibility(View.GONE);
                    mWheelView.setVisibility(View.VISIBLE);
                    return;
                }
                rlSelectContent.setVisibility(View.GONE);
                setCurrentWheelViewTag();
                updateItem();
                break;
        }
    }

    private void setCurrentWheelViewTag(){
        switch (currentWheelView){
            case BOOKING_TIME_LAYOUT_TAG:
                Calendar calendar = Calendar.getInstance();
                calendar.set(dpPicker.getYear(), dpPicker.getMonth(), dpPicker.getDayOfMonth());
                searchCondition.advance.date=calendar.getTimeInMillis();
                searchCondition.advance.timeSet=timeSets.get(mWheelView.getSeletedIndex());
                booking_time = new SimpleDateFormat(NetDataConstants.DATE_FORMAT).format(calendar.getTimeInMillis())+" "+ mWheelView.getSeletedItem();
                break;
            case BOOKING_CUISINE_LAYOUT_TAG:
                booking_cuisine = mWheelView.getSeletedItem();
                searchCondition.advance.cuisine=cuisines.get(mWheelView.getSeletedIndex());
                break;
            case BOOKING_LOCATION_LAYOUT_TAG:
                break;
            case BOOKING_PERSON_NUM_LAYOUT_TAG:
                booking_person_num = mWheelView.getSeletedItem();
                searchCondition.advance.personNumType=PersonNumType.getByOrdinal(mWheelView.getSeletedIndex());
                break;
            case BOOKING_SHOPTYPE_LAYOUT_TAG:
                booking_shoptype = mWheelView.getSeletedItem();
                searchCondition.base.shopCategory=shopCategories.get(mWheelView.getSeletedIndex());
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

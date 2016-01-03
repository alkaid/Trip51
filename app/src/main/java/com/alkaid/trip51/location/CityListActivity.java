package com.alkaid.trip51.location;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.location.adapter.CityComparator;
import com.alkaid.trip51.location.adapter.CityGridViewAdapter;
import com.alkaid.trip51.location.adapter.CitySortAdapter;
import com.alkaid.trip51.location.widget.CityRightBar;
import com.alkaid.trip51.location.widget.CitySearchEditText;
import com.alkaid.trip51.model.SimpleCity;
import com.alkaid.trip51.util.CharacterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityListActivity extends Activity {
    LinearLayout llCityList;
    //城市列表相关ui
    private GridView gvLocationCities;//定位城市列表
    private GridView gvLastInterviewCities;//最近观看的城市列表
    private GridView gvHotCities;//热门城市列表
    private ListView citySortListView;
    private CityRightBar sideBar; //城市列表右侧字母排列
    private TextView tipDialog;//滚动提示ui
    private CitySearchEditText mClearEditText;//城市列表搜索栏
    private FrameLayout flSearch;//搜索条
    private View layTitleBar;

    private TextView tvLocationTitle;
    private TextView tvLastInterviewTitle;
    private TextView tvHotTitle;

    private CityComparator cityComparator;

    //城市列表相关数据
    private List<SimpleCity> locationCitys;
    private List<SimpleCity> lastInterviewCities;
    private List<SimpleCity> hotCities;
    private List<SimpleCity> allCities;

    private CharacterParser characterParser;//城市列表汉字转拼音的类

    //城市列表相关adapter
    private CitySortAdapter sortAdapter;
    private CityGridViewAdapter locationCitiesAdapter;
    private CityGridViewAdapter lastInterviewCitiesAdapter;
    private CityGridViewAdapter hotCitiesAdapter;

    private OnItemClickListener onItemClickListener;

    private int otherHeight = 0;//用于计算热门城市，定位城市，以及保存城市一共的高度
    private int sideBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initTitleBar();
        initData();
        initViews();
    }

    private void initTitleBar() {
        layTitleBar = findViewById(R.id.title_bar);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        View btnLeft = findViewById(R.id.btn_back_wx);
        View btnRight = findViewById(R.id.notify);
        tvTitle.setText("当前城市");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        View listViewHeadView = getLayoutInflater().inflate(R.layout.listview_city_headview,null);
        llCityList = (LinearLayout) findViewById(R.id.ll_acivity_city_list);
        //初始化view
        flSearch = (FrameLayout) findViewById(R.id.fl_search);
        sideBar = (CityRightBar) findViewById(R.id.sidrbar);
        tipDialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(tipDialog);
        mClearEditText = (CitySearchEditText) findViewById(R.id.filter_edit);
        citySortListView = (ListView) findViewById(R.id.lv_citys);
        gvLocationCities = (GridView)listViewHeadView.findViewById(R.id.gv_location_citys);
        gvLastInterviewCities = (GridView)listViewHeadView.findViewById(R.id.gv_last_interview_citys);
        gvHotCities = (GridView)listViewHeadView. findViewById(R.id.gv_hot_citys);
        tvLocationTitle = (TextView) listViewHeadView.findViewById(R.id.tv_location_citys_title);
        tvLastInterviewTitle = (TextView) listViewHeadView.findViewById(R.id.tv_last_interview_citys_title);
        tvHotTitle = (TextView) listViewHeadView.findViewById(R.id.tv_hot_citys_title);

        citySortListView.addHeaderView(listViewHeadView);

        //设置adapter
        locationCitiesAdapter = new CityGridViewAdapter(this);
        lastInterviewCitiesAdapter = new CityGridViewAdapter(this);
        hotCitiesAdapter = new CityGridViewAdapter(this);
        sortAdapter = new CitySortAdapter(this, allCities);
        citySortListView.setAdapter(sortAdapter);
        gvLocationCities.setAdapter(locationCitiesAdapter);
        gvLastInterviewCities.setAdapter(lastInterviewCitiesAdapter);
        gvHotCities.setAdapter(hotCitiesAdapter);

        //setListViewHeight(citySortListView);
        if (locationCitys != null && locationCitys.size() > 0) {
            locationCitiesAdapter.setData(locationCitys);
            tvLocationTitle.setVisibility(View.VISIBLE);
            gvLocationCities.setVisibility(View.VISIBLE);
        } else {
            tvLocationTitle.setVisibility(View.GONE);
            gvLocationCities.setVisibility(View.GONE);
        }
        if (lastInterviewCities != null && lastInterviewCities.size() > 0) {
            lastInterviewCitiesAdapter.setData(lastInterviewCities);
            tvLastInterviewTitle.setVisibility(View.VISIBLE);
            gvLastInterviewCities.setVisibility(View.VISIBLE);
        } else {
            tvLastInterviewTitle.setVisibility(View.GONE);
            gvLastInterviewCities.setVisibility(View.GONE);
        }
        if (hotCities != null && hotCities.size() > 0) {
            hotCitiesAdapter.setData(hotCities);
            tvHotTitle.setVisibility(View.VISIBLE);
            gvHotCities.setVisibility(View.VISIBLE);
        } else {
            tvHotTitle.setVisibility(View.GONE);
            gvHotCities.setVisibility(View.GONE);
        }
        locationCitiesAdapter.notifyDataSetChanged();
        lastInterviewCitiesAdapter.notifyDataSetChanged();
        hotCitiesAdapter.notifyDataSetChanged();

        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCity selectedCity = null;
                if (parent.getId() == gvLocationCities.getId()) {
                    selectedCity = locationCitys.get(position);
                } else if (parent.getId() == gvLastInterviewCities.getId()) {
                    selectedCity = lastInterviewCities.get(position);
                } else if (parent.getId() == gvHotCities.getId()) {
                    selectedCity = hotCities.get(position);
                } else if (parent.getId() == citySortListView.getId()) {
                    selectedCity = (SimpleCity) sortAdapter.getItem(position-1);
                }
                App.locationService().changeCity(selectedCity);
                setResult(Activity.RESULT_OK);
                finish();
            }
        };
        // 根据a-z进行排序源数据
        Collections.sort(allCities, cityComparator);
        //监听
        citySortListView.setOnItemClickListener(onItemClickListener);
        gvLocationCities.setOnItemClickListener(onItemClickListener);
        gvLastInterviewCities.setOnItemClickListener(onItemClickListener);
        gvHotCities.setOnItemClickListener(onItemClickListener);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new CityRightBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                final int position = sortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    citySortListView.setSelection(position);
//                    svCityList.scrollTo(0, getScrollViewHeight(position, citySortListView));
                }

            }
        });
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setViewHeightInOnCreate();
    }

    private void setViewHeightInOnCreate() {
        final WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        ViewTreeObserver vto = llCityList.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (sideBarHeight == 0) {
                    sideBarHeight = wm.getDefaultDisplay().getHeight() - layTitleBar.getMeasuredHeight() - flSearch.getMeasuredHeight();
                    sideBar.setHeight(sideBarHeight);
                }
                if (otherHeight == 0) {
                    if (tvLocationTitle.getVisibility() == View.VISIBLE) {
                        otherHeight = otherHeight + tvLocationTitle.getHeight() + gvLocationCities.getHeight();
                    }
                    if (tvLastInterviewTitle.getVisibility() == View.VISIBLE) {
                        otherHeight = otherHeight + tvLastInterviewTitle.getHeight() + gvLastInterviewCities.getHeight();
                    }
                    if (tvHotTitle.getVisibility() == View.VISIBLE) {
                        otherHeight = otherHeight + tvHotTitle.getHeight() + gvHotCities.getHeight();
                    }
                }
            }
        });
    }

    private void initData() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        cityComparator = new CityComparator();
        locationCitys = new ArrayList<>();
        //当前定位城市
        SimpleCity locationCity = App.locationService().getGpsCity();
        if (locationCity.getCityname() != null && !locationCity.getCityname().equals("")) {
            locationCitys.add(locationCity);
        }
        //热门城市
        hotCities = App.locationService().getHotCities();
        //最近观看的城市
        lastInterviewCities = App.locationService().getLastInterviewCities();
        //所有城市
        allCities = App.locationService().getCities();

        characterParser = CharacterParser.getInstance();
        setCitiesFirstLetter();
    }


    /**
     * 设置城市列表首字母
     *
     * @return
     */
    private void setCitiesFirstLetter() {
        if (allCities == null) {
            return;
        }
        for (int i = 0; i < allCities.size(); i++) {
            SimpleCity c = allCities.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(c.getCityname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                c.setFirstLetter(sortString.toUpperCase());
            } else {
                c.setFirstLetter("#");
            }
        }

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SimpleCity> filterCityList = new ArrayList<SimpleCity>();

        if (TextUtils.isEmpty(filterStr)) {
            filterCityList = allCities;
        } else {
            filterCityList.clear();
            for (SimpleCity city : allCities) {
                String name = city.getCityname();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterCityList.add(city);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterCityList, cityComparator);
        sortAdapter.updateListView(filterCityList);
    }

}

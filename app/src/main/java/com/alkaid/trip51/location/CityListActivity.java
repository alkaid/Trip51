package com.alkaid.trip51.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.location.adapter.CityGridViewAdapter;
import com.alkaid.trip51.location.widget.CitySearchEditText;
import com.alkaid.trip51.location.widget.CityRightBar;
import com.alkaid.trip51.util.CharacterParser;
import com.alkaid.trip51.location.adapter.CityComparator;
import com.alkaid.trip51.location.adapter.CitySortAdapter;
import com.alkaid.trip51.model.SimpleCity;

public class CityListActivity extends Activity {
    //城市列表相关ui
    private GridView gvLocationCities;//定位城市列表
    private GridView gvLastInterviewCities;//最近观看的城市列表
    private GridView gvHotCities;//热门城市列表
    private ListView citySortListView;
    private CityRightBar sideBar; //城市列表右侧字母排列
    private TextView dialog;//滚动提示ui
    private CitySearchEditText mClearEditText;//城市列表搜索栏

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

    public static final int CITY_ACTIVITY_BACK = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initTitleBar();
        initData();
        initViews();
    }

    private void initTitleBar() {
        View layTitleBar = findViewById(R.id.title_bar);
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
        //初始化view
        sideBar = (CityRightBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        mClearEditText = (CitySearchEditText) findViewById(R.id.filter_edit);
        citySortListView = (ListView) findViewById(R.id.lv_citys);
        gvLocationCities = (GridView) findViewById(R.id.gv_location_citys);
        gvLastInterviewCities = (GridView) findViewById(R.id.gv_last_interview_citys);
        gvHotCities = (GridView) findViewById(R.id.gv_hot_citys);

        //设置adapter
        locationCitiesAdapter = new CityGridViewAdapter(this);
        lastInterviewCitiesAdapter = new CityGridViewAdapter(this);
        hotCitiesAdapter = new CityGridViewAdapter(this);
        sortAdapter = new CitySortAdapter(this, allCities);
        citySortListView.setAdapter(sortAdapter);
        gvLocationCities.setAdapter(locationCitiesAdapter);
        gvLastInterviewCities.setAdapter(lastInterviewCitiesAdapter);
        gvHotCities.setAdapter(hotCitiesAdapter);

        locationCitiesAdapter.setData(locationCitys);
        lastInterviewCitiesAdapter.setData(lastInterviewCities);
        hotCitiesAdapter.setData(hotCities);
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
                    selectedCity = (SimpleCity)sortAdapter.getItem(position);
                }
                finishActivity(CITY_ACTIVITY_BACK);
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
                int position = sortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    citySortListView.setSelection(position);
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
    }

    private void initData() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        cityComparator = new CityComparator();
        locationCitys = new ArrayList<>();
        //当前定位城市
        SimpleCity locationCity = App.locationService().getCity(App.locationService().getGpsCityId());
        locationCitys.add(locationCity);
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

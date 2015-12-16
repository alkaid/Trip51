package com.alkaid.trip51.location;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.base.widget.view.ButtonSearchBar;
import com.alkaid.trip51.location.adapter.CityGridViewAdapter;
import com.alkaid.trip51.model.SimpleCity;
import com.alkaid.trip51.widget.pinnedheaderlistview.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市列表
 */

/**
 * Created by jyz on 2015/12/10.
 */
public class CityListActivity extends BaseActivity {

    private ButtonSearchBar bsbCitySearch;//城市检索条
    private GridView gvLocationCities;//定位城市列表
    private GridView gvLastInterviewCities;//最近观看的城市列表
    private GridView gvHotCities;//热门城市列表
    private PinnedHeaderListView phlCities;//所有城市列表，按子母排序
    private ListView lvCharacters;//子母列表

    private List<SimpleCity> locationCitys;
    private List<SimpleCity> lastInterviewCities;
    private List<SimpleCity> hotCities;
    private List<SimpleCity> allCities;
    private char[] characters = {'#','*','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'};

    private CityGridViewAdapter locationCitiesAdapter;
    private CityGridViewAdapter lastInterviewCitiesAdapter;
    private CityGridViewAdapter hotCitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initTitleBar();
        initData();
        initView();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("当前城市");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
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
    }

    private void initView(){
        bsbCitySearch = (ButtonSearchBar) findViewById(R.id.bsb_city);
        gvLocationCities = (GridView) findViewById(R.id.gv_location_citys);
        gvLastInterviewCities = (GridView) findViewById(R.id.gv_last_interview_citys);
        gvHotCities = (GridView) findViewById(R.id.gv_hot_citys);
        phlCities = (PinnedHeaderListView) findViewById(R.id.pinned_header_lv_citys);
        lvCharacters = (ListView) findViewById(R.id.lv_character_list);

        locationCitiesAdapter = new CityGridViewAdapter(context);
        lastInterviewCitiesAdapter = new CityGridViewAdapter(context);
        hotCitiesAdapter = new CityGridViewAdapter(context);

        locationCitiesAdapter.setData(locationCitys);
        lastInterviewCitiesAdapter.setData(lastInterviewCities);
        hotCitiesAdapter.setData(hotCities);

        gvLocationCities.setAdapter(locationCitiesAdapter);
        gvLastInterviewCities.setAdapter(lastInterviewCitiesAdapter);
        gvHotCities.setAdapter(hotCitiesAdapter);

        locationCitiesAdapter.notifyDataSetChanged();
        lastInterviewCitiesAdapter.notifyDataSetChanged();
        hotCitiesAdapter.notifyDataSetChanged();
        //bsbCitySearch.setHint("城市/行政区/拼音");
    }
}


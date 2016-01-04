package com.alkaid.trip51.dataservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.SimpleCity;
import com.alkaid.trip51.model.response.ResAllCityList;
import com.alkaid.trip51.model.response.ResCityId;
import com.alkaid.trip51.model.response.ResCityList;
import com.alkaid.trip51.model.response.ResShopCondition;
import com.alkaid.trip51.util.SpUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/19.
 */
public class LocationService {
    public static final String INIT_TAG_START_LOCATION="INIT_TAG_START_LOCATION";
    public static final String ACTION_RECIVE_LOCATION="action.trip51.receive.location";
    public static final String BUNDLE_KEY_LOCATION="BUNDLE_KEY_LOCATION";
    private static LocationService instance;
    private Context context;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
//    private String coordinates="113.917554,22.495232";  //TODO 写入SP 为空时使用SP SP为空时定一个默认值
    private String coordinates="0,0";
    private String provinceName="陕西";
    private SimpleCity selectCity=new SimpleCity(311,"西安");
    private SimpleCity gpsCity=new SimpleCity(0,null);
    private String tmpCityName;
    private List<SimpleCity> cities=new ArrayList<SimpleCity>();
    private List<SimpleCity> hotCities=new ArrayList<SimpleCity>();
    private List<SimpleCity> lastInterviewCities = new ArrayList<>();
    private ResShopCondition condition;
    private ServiceListener serviceListener;
    private int taskStep=0; //已经执行完了哪些异步任务
    private boolean isLocating=false;

    private LocationService(Context context){
        this.context=context;
    }
//    public LocationService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static LocationService create(Context context){
        instance=new LocationService(context);
        instance.init();
        return instance;
    }

    private void init(){
        SharedPreferences sp=SpUtil.getSp();
        provinceName= sp.getString(SpUtil.key_provincename,provinceName);
        selectCity.setCityname(sp.getString(SpUtil.key_cityname, selectCity.getCityname()));
        selectCity.setCityid(sp.getLong(SpUtil.key_cityid, selectCity.getCityid()));
        coordinates=sp.getString(SpUtil.key_coordinates, coordinates);
        mLocationClient = new LocationClient(context);
        mLocationClient.setLocOption(getDefaultOption());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    public LocationClient locationClient(){
        return mLocationClient;
    }

    public String getCoordinates() {
        //TODO 需要解决首页显示时还没定位成功的问题，可以考虑用欢迎页面定位，coordinates有值了才跳过
        LogUtil.v("coordinate=" + coordinates);
        return coordinates;
    }

    public void startLocation(ServiceListener serviceListener){
        //TODO 为避免线程问题 这里先用标识位判断，已在定位时拒绝定位请求。后期必须改进，应用BlockingQueue加Thread或Task解决
        if(isLocating){
            LogUtil.w("Location Service is busy now!");
            return;
        }
        isLocating=true;
        mLocationClient.start();
        this.serviceListener = serviceListener;
    }

    /**
     * 实现实时位置回调监听
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            boolean isSuccess=false;
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            sb.append("\nprovince:");
            sb.append(location.getProvince());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                isSuccess=true;
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                isSuccess=true;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                isSuccess=true;
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            List<Poi> list = location.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApi", sb.toString());
            if(!isSuccess) {
                LogUtil.w("定位失败，使用上次的定位结果");
                location = mLocationClient.getLastKnownLocation();
                switch (location.getLocType()) {
                    case BDLocation.TypeGpsLocation:
                    case BDLocation.TypeNetWorkLocation:
                    case BDLocation.TypeOffLineLocation:
                        isSuccess = true;
                        break;
                }
                LogUtil.w("上次的定位结果也是错误的，使用初始默认数据");
            }
            if(isSuccess) {
//                coordinates = "113.917554,22.495232";     //Test
                coordinates = location.getLongitude() + "," + location.getLatitude();
            }
            LogUtil.v("coordinates="+coordinates);
            //广播
            Intent intent=new Intent(ACTION_RECIVE_LOCATION);
            intent.putExtra(BUNDLE_KEY_LOCATION, location);
            context.sendBroadcast(intent);
            taskStep|=1;
            //保存
            if(!TextUtils.isEmpty(location.getProvince())){
                provinceName=location.getProvince();
                if(provinceName.endsWith("省"))
                    provinceName = provinceName.substring(0,provinceName.length()-1);
            }
            if(!TextUtils.isEmpty(location.getCity())){
                tmpCityName=location.getCity();
                if(tmpCityName.endsWith("市"))
                    tmpCityName = tmpCityName.substring(0,tmpCityName.length()-1);
            }
            //获取城市列表
            requestCityList(new Response.Listener<ResAllCityList>() {
                @Override
                public void onResponse(ResAllCityList resdata) {
                    //保存
                    for(ResAllCityList.Province p:resdata.provincelist)
                    cities.addAll(p.citylist);
                    taskStep |= 2;
                    obtainCityid();
                    onAnyTaskComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.w(error);
                    taskStep |= 2;
                    obtainCityid();
                    onAnyTaskComplete();
                }
            });
            //获取热门城市
            requestHotCityList(new Response.Listener<ResCityList>() {
                @Override
                public void onResponse(ResCityList resdata) {
                    //保存
                    hotCities = resdata.getCitylist();
                    taskStep |= 4;
                    onAnyTaskComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.w(error);
                    taskStep |= 4;
                    onAnyTaskComplete();
                }
            });
        }
    }

    private void obtainCityid(){
        //匹配当前城市id
        boolean isMatch=false;
        for (SimpleCity c:cities){
            if(c.getCityname().equals(tmpCityName)){
                selectCity=c;
                gpsCity=new SimpleCity(c.getCityid(),c.getCityname());
                isMatch=true;
                break;
            }
        }
//            cityId = 77; isMatch = true;  //Test
        //匹配不到则接口获取cityid
        if(!isMatch && null!=tmpCityName){
            requestCityId(new Response.Listener<ResCityId>() {
                @Override
                public void onResponse(ResCityId resdata) {
                    selectCity=new SimpleCity(resdata.getCityid(),tmpCityName);
                    gpsCity=new SimpleCity(resdata.getCityid(),tmpCityName);
                    onLocationChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.w(error);
                    onLocationChanged();
                }
            });
        }else{
            onLocationChanged();
        }
    }

    public LocationClientOption getDefaultOption(){
        LocationClientOption option = new LocationClientOption();
//        <string name="hight_accuracy_desc">高精度定位模式下，会同时使用GPS、Wifi和基站定位，返回的是当前条件下精度最好的定位结果</string>
//        <string name="saving_battery_desc">低功耗定位模式下，仅使用网络定位即Wifi和基站定位，返回的是当前条件下精度最好的网络定位结果</string>
//        <string name="device_sensor_desc">仅用设备定位模式下，只使用用户的GPS进行定位。这个模式下，由于GPS芯片锁定需要时间，首次定位速度会需要一定的时间</string>
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        mLocationClient.setLocOption(option);
        return option;
    }

    /**
     * 获取城市列表
     * @param listener
     * @param errorListener
     */
    public void requestHotCityList(Response.Listener<ResCityList> listener,Response.ErrorListener errorListener){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
//        unBeSignform.put("provincename", provinceName);
        final String tag="citylist"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.DAILY,false,ResCityList.class, MApiService.URL_CITY_HOTLIST, beSignForm, unBeSignform,listener,errorListener), tag);
    }
    public void requestProvinceCityList(Response.Listener<ResCityList> listener,Response.ErrorListener errorListener){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("provincename", provinceName);
        final String tag="citylist"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.DAILY,false,ResCityList.class, MApiService.URL_CITY_LIST_BY_PROVINCE, beSignForm, unBeSignform,listener,errorListener), tag);
    }
    public void requestCityList(final Response.Listener<ResAllCityList> listener, final Response.ErrorListener errorListener){
        final Map<String,String> beSignForm=new HashMap<String, String>();
        final Map<String,String> unBeSignform=new HashMap<String, String>();
//        unBeSignform.put("provincename", provinceName);
        final int cacheDataVersion=Integer.parseInt( SpUtil.getString(SpUtil.key_data_version_citylist,"0"));
        unBeSignform.put("versionnum", cacheDataVersion+"");
        final String tag="citylist"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, true, ResAllCityList.class, MApiService.URL_CITY_LIST, beSignForm, unBeSignform, new Response.Listener<ResAllCityList>() {
            @Override
            public void onResponse(ResAllCityList response) {
                //若版本相同 重新取缓存后再回调
                if(response.versionnum==cacheDataVersion){
                    App.mApiService().exec(new MApiRequest(CacheType.NORMAL, false, ResAllCityList.class, MApiService.URL_CITY_LIST, beSignForm, unBeSignform,listener,errorListener),tag);
                }else{
                    //版本不同 用网络数据直接回调
                    SpUtil.putString(SpUtil.key_data_version_citylist,response.versionnum+"");
                    listener.onResponse(response);
                }
            }
        }, errorListener, new MApiRequest.OnParsedResponseListener<ResAllCityList>() {
            @Override
            public void onParsedResponseWithNetwork(Response<ResAllCityList> r) {
                r.cacheDataVersion=cacheDataVersion;
                r.netDataVersion=r.result.versionnum;
            }
        }), tag);
    }
    private void requestCityId(Response.Listener<ResCityId> listener,Response.ErrorListener errorListener){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("cityname", tmpCityName);
        final String tag="getcityid"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true, ResCityId.class, MApiService.URL_CITY_GETID, beSignForm, unBeSignform, listener, errorListener), tag);
    }

    private void saveSp(){
        SharedPreferences.Editor ed=SpUtil.getSp().edit();
        ed.putString(SpUtil.key_provincename,provinceName).putLong(SpUtil.key_cityid, selectCity.getCityid()).putString(SpUtil.key_cityname, selectCity.getCityname()).putString(SpUtil.key_coordinates, coordinates).commit();
    }

    //任何异步任务结束后都要执行该方法来判断是否所有任务逗已经完成
    private void onAnyTaskComplete(){
        if(null!= serviceListener && taskStep==(1|2|4|8)){
            serviceListener.onComplete(INIT_TAG_START_LOCATION);
            serviceListener =null;
        }
    }

    /**
     * 切换城市
     */
    public void changeCity(SimpleCity selectCity){
        this.selectCity=selectCity;
        onLocationChanged();
    }

    private void onLocationChanged(){
        saveSp();
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("cityid", selectCity.getCityid()+"");
        final String tag="getConditions"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true, ResShopCondition.class, MApiService.URL_SHOP_CONDITION, beSignForm, unBeSignform, new Response.Listener<ResShopCondition>() {
            @Override
            public void onResponse(ResShopCondition response) {
                condition = response;
//                for(Area a: response.getArealist()){
//                    for(Circle c:a.getCirclelist()){
//                        c.areaid=a.getAreaid();
//                    }
//                }
                taskStep |= 8;
                onAnyTaskComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w(error);
                taskStep |= 8;
                condition = new ResShopCondition();
                onAnyTaskComplete();
            }
        }), tag);
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public List<SimpleCity> getCities() {
        return cities;
    }

    public void setCities(List<SimpleCity> cities) {
        this.cities = cities;
    }

    public List<SimpleCity> getHotCities() {
        return hotCities;
    }

    public void setHotCities(List<SimpleCity> hotCities) {
        this.hotCities = hotCities;
    }

    public ResShopCondition getCondition() {
        return condition;
    }

    public void setCondition(ResShopCondition condition) {
        this.condition = condition;
    }

    public SimpleCity getGpsCity() {
        return gpsCity;
    }
    public SimpleCity getSelectCity() {
        return selectCity;
    }

    /**
     * 最近查看的城市列表
     * @return
     */
    public List<SimpleCity> getLastInterviewCities(){
        return lastInterviewCities;
    }

}

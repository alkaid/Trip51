package com.alkaid.trip51.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.model.AdvBanner;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 * @author caizhiming
 *
 */

public class SlideShowView extends FrameLayout {
    private Context context;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;
    private List<AdvBanner> advs;

    //放轮播图片的ImageView 的list
    private List<NetworkImageView> imageViewsList=new ArrayList<>();
    //放圆点的View的list
    private List<View> dotViewsList=new ArrayList<>();

    private ViewGroup layDots;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        initUI(context);
    }

    public void load(List<AdvBanner> advs){
        if(AdvBanner.same(this.advs,advs))
            return;
        stopPlay();
        currentItem=0;
        destoryBitmaps();
        layDots.removeAllViews();
        this.advs=advs==null?new ArrayList<AdvBanner>():advs;
        imageViewsList.clear();
        dotViewsList.clear();
        if(advs.isEmpty()){
            NetworkImageView view =  new NetworkImageView(context);
            view.setImageResource(R.drawable.temp1);
            view.setDefaultImageResId(R.drawable.temp1);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);
            addDotView();
        }else{
            for (final AdvBanner adv:advs){
                NetworkImageView view =  new NetworkImageView(context);
                view.setImageResource(R.drawable.temp1);
                view.setDefaultImageResId(R.drawable.temp1);
                view.setErrorImageResId(R.drawable.temp1);
                view.setScaleType(ScaleType.FIT_XY);
                view.setImageUrl(adv.picurl, App.mApiService().getImageLoader());
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(adv.linkurl))
                            return;
                        //TODO 服务端返回数据不正确 没有带协议头，暂时由客户端先加上
                        if(!adv.linkurl.contains("http")){
                            adv.linkurl="http://"+adv.linkurl;
                        }
                        Uri uri = Uri.parse(adv.linkurl);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view);
                addDotView();
            }
        }
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
        if(isAutoPlay){
            startPlay();
        }
    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        if(null!=scheduledExecutorService) {
            scheduledExecutorService.shutdownNow();
        }
    }

    private void addDotView(){
        View v=LayoutInflater.from(context).inflate(R.layout.banner_dot,layDots,false);
        layDots.addView(v);
        dotViewsList.add(v);
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        layDots= (ViewGroup) findViewById(R.id.layDots);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        pagerAdapter=new MyPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     * @author caizhiming
     */
    private class MyPagerAdapter  extends PagerAdapter{

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager)container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager)container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     * @author caizhiming
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for(int i=0;i < dotViewsList.size();i++){
                if(i == pos){
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.navigation_dot_pressed);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.navigation_dot_normal);
                }
            }
        }

    }

    /**
     *执行轮播图切换任务
     *@author caizhiming
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }
    /**
     * 销毁ImageView资源，回收内存
     * @author caizhiming
     */
    private void destoryBitmaps() {

        for (int i = 0; i < imageViewsList.size(); i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

}
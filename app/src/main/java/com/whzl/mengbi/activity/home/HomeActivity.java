package com.whzl.mengbi.activity.home;


import android.os.Bundle;
import android.view.View;

import com.hjm.bottomtabbar.BottomTabBar;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.BaseAtivity;
import com.whzl.mengbi.fragemengt.follow.FollowFragment;
import com.whzl.mengbi.fragemengt.home.HomeFragment;
import com.whzl.mengbi.fragemengt.my.MyFragment;
import com.whzl.mengbi.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.Arrays;
import java.util.List;

/**
 * funcciton 创建首页所有的fragment,以及fragment
 */
public class HomeActivity extends BaseAtivity {

    /**
     * 底部导航栏相关
     */
    private BottomTabBar mBottomTabBar;

    /**
     * 顶部轮番图
     */
    private Banner mBanner;

    /**
     * fragment相关
     */
    private HomeFragment mHomeFragment ;
    private FollowFragment mFollowFragment ;
    private MyFragment mMyFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        //实例化控件
        initData();
        initView();

    }


    private void initView(){
        mBottomTabBar = (BottomTabBar)findViewById(R.id.home_bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager(), 720, 1280)//初始化方法，必须第一个调用；传入参数为V4包下的FragmentManager
//                .setImgSize(70, 70)//设置ICON图片的尺寸
//                .setFontSize(14)//设置文字的尺寸
//                .setTabPadding(5, 0, 5)//设置ICON图片与上部分割线的间隔、图片与文字的间隔、文字与底部的间隔
//                  .setChangeColor(Color.parseColor(""),Color.parseColor(""))//设置选中的颜色、未选中的颜色
                  .addTabItem("首页", R.mipmap.home_gray, HomeFragment.class)//设置文字、一张图片、fragment
                  .addTabItem("关注", R.mipmap.follow_white, FollowFragment.class)//设置文字、两张图片、fragment
                  .addTabItem("我的", R.mipmap.my_white, MyFragment.class)
//                .isShowDivider(true)//设置是否显示分割线
//                .setDividerColor(Color.parseColor("#FF0000"))
//                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"))//设置底部导航栏颜色
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        if (position == 1)
                            mBottomTabBar.setSpot(1, false);
                    }
                })
                .setSpot(1, false)
                .setSpot(2, false);


        //资源文件

        Integer[] images={R.mipmap.home_gray,R.mipmap.follow_white,R.mipmap.my_white};
        List<Integer> stringA = Arrays.asList(images);
        String[] str={"a","b","c"};
        List<String> titles = Arrays.asList(str);
        mBanner = (Banner) findViewById(R.id.home_banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(stringA);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

    }

    private  void initData(){

    }



    @Override
    protected void onStart() {
        //开始轮番
        mBanner.startAutoPlay();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //结束轮番
        mBanner.stopAutoPlay();
        super.onStop();
    }
}

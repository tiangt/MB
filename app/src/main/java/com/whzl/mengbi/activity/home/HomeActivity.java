package com.whzl.mengbi.activity.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.BaseAtivity;
import com.whzl.mengbi.fragemengt.follow.FollowFragment;
import com.whzl.mengbi.fragemengt.home.HomeFragment;
import com.whzl.mengbi.fragemengt.my.MyFragment;
import com.whzl.mengbi.view.BottomNavigationViewHelper;
import com.whzl.mengbi.view.GlideImageLoader;
import com.whzl.mengbi.view.ViewPagerAdapter;
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
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

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

        //资源文件
        Integer[] images={R.mipmap.ic_home_gray,R.mipmap.ic_follow_white,R.mipmap.ic_my_white};
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


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_follow:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_my:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(HomeFragment.newInstance("首页"));
        adapter.addFragment(FollowFragment.newInstance("图书"));
        adapter.addFragment(MyFragment.newInstance("发现"));
        viewPager.setAdapter(adapter);
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

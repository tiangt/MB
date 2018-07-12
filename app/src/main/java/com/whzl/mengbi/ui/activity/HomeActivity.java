package com.whzl.mengbi.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.adapter.ViewPagerAdapter;
import com.whzl.mengbi.ui.fragment.home.FollowFragment;
import com.whzl.mengbi.ui.fragment.home.HomeFragment;
import com.whzl.mengbi.ui.fragment.home.MeFragment;
import com.whzl.mengbi.ui.widget.BottomNavigationViewHelper;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.ui.fragment.home.FollowFragment;
import com.whzl.mengbi.ui.fragment.home.HomeFragment;
import com.whzl.mengbi.ui.fragment.home.MeFragment;
import com.whzl.mengbi.ui.adapter.ViewPagerAdapter;


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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_home_layout);
        //实例化控件
        initView();
    }

    private void initView(){
        //底部导航切换，滑动
        viewPager = findViewById(R.id.home_viewpager);
        bottomNavigationView = findViewById(R.id.home_bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        boolean islogin = (boolean)SPUtils.get(HomeActivity.this,"islogin",false);
                        switch (item.getItemId()) {
                            case R.id.item_home:
                                 item.setIcon(R.drawable.ic_home_bottom_checked);
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_follow:
                                if (!islogin) {
                                    Intent mIntent = new Intent(HomeActivity.this,LoginActivity.class);
                                    mIntent.putExtra("visitor", true);
                                    startActivity(mIntent);
                                }
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_my:
                                item.setIcon(R.drawable.mechance);
                                if (!islogin) {
                                    Intent mIntent = new Intent(HomeActivity.this,LoginActivity.class);
                                    mIntent.putExtra("visitor", true);
                                    startActivity(mIntent);
                                }
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

        //禁止ViewPager滑动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance("首页"));
        adapter.addFragment(FollowFragment.newInstance("关注"));
        adapter.addFragment(MeFragment.newInstance("我的"));
        viewPager.setAdapter(adapter);
    }
}

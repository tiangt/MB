package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.FindRankFragment;
import com.whzl.mengbi.ui.fragment.RankListFragment;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发现--榜单
 *
 * @author cliang
 * @date 2019.2.21
 */
public class FindRankActivity extends BaseActivity {

    @BindView(R.id.period_tab)
    TabLayout periodTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_find_bg)
    ImageView bgFind;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String rankName;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_find_rank);
    }

    @Override
    protected void setupView() {
        int index = getIntent().getIntExtra("rank", 0);
        if (0 == index) {
            tvTitle.setText("明星榜");
            rankName = "CELEBRITY";
            GlideImageLoader.getInstace().displayImage(this, R.drawable.bg_find_star, bgFind);
        } else if (1 == index) {
            tvTitle.setText("富豪榜");
            rankName = "RICH";
            GlideImageLoader.getInstace().displayImage(this, R.drawable.bg_find_regal, bgFind);
        } else if (2 == index) {
            tvTitle.setText("人气榜");
            rankName = "POPULAR";
            GlideImageLoader.getInstace().displayImage(this, R.drawable.bg_find_pop, bgFind);
        }

        ArrayList<String> titles = new ArrayList<>();
        titles.add("今日");
        titles.add("本周");
        titles.add("本月");
        titles.add("昨日");
        titles.add("上周");
        titles.add("上月");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(FindRankFragment.newInstance(rankName, "DAY", "F"));
        fragments.add(FindRankFragment.newInstance(rankName, "WEEK", "F"));
        fragments.add(FindRankFragment.newInstance(rankName, "MONTH", "F"));
        fragments.add(FindRankFragment.newInstance(rankName, "DAY", "T"));
        fragments.add(FindRankFragment.newInstance(rankName, "WEEK", "T"));
        fragments.add(FindRankFragment.newInstance(rankName, "MONTH", "T"));
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        periodTab.setupWithViewPager(viewpager);
        periodTab.clearOnTabSelectedListeners();
        periodTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.iv_return)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;

            default:
                break;
        }
    }
}

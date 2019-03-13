package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.FindRankFragment;
import com.whzl.mengbi.util.UIUtil;
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
    @BindView(R.id.iv_question)
    ImageView ivQuestion;

    private String rankName;
    private PopupWindow popupWindow;
    private String[] tipDescs;
    private String[] tipTitles;
    private int index;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_find_rank);
    }

    @Override
    protected void setStatusBar() {
        View mViewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTransparentForImageView(this, mViewNeedOffset);
    }

    @Override
    protected void setupView() {
        tipTitles = getResources().getStringArray(R.array.rank_tip_title);
        tipDescs = getResources().getStringArray(R.array.rank_tip_desc);

        index = getIntent().getIntExtra("rank", 0);
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

    @OnClick({R.id.iv_return, R.id.iv_question})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;

            case R.id.iv_question:
                showPopWindow();
                break;

            default:
                break;
        }
    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.pop_rank_tip, null);
        TextView tvTip = popView.findViewById(R.id.tv_tip);
        TextView tvTip2 = popView.findViewById(R.id.tv_tip_2);
        if(0 == index){
            tvTip.setText(getString(R.string.rank_tip_star));
        }else if(1 == index){
            tvTip.setText(getString(R.string.rank_tip_regal));
        }else{
            tvTip.setText(getString(R.string.rank_tip_anchor1));
            tvTip2.setVisibility(View.VISIBLE);
            tvTip2.setText(getString(R.string.rank_tip_anchor2));
        }

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(ivQuestion, 0, -UIUtil.dip2px(FindRankActivity.this, 8));
    }
}

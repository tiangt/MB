package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.FindRankFragment;
import com.whzl.mengbi.ui.fragment.RankFragment;
import com.whzl.mengbi.ui.fragment.RankListFragment;
import com.whzl.mengbi.util.StringUtils;
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

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_find_rank);
    }

    @Override
    protected void setupView() {
        tipTitles = getResources().getStringArray(R.array.rank_tip_title);
        tipDescs = getResources().getStringArray(R.array.rank_tip_desc);

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
        View popView = getLayoutInflater().inflate(R.layout.popwindow_rank_tip, null);
        RecyclerView recyclerView = popView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(FindRankActivity.this));
        recyclerView.setAdapter(new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return tipDescs == null ? 0 : tipDescs.length;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(FindRankActivity.this);
                textView.setTextColor(Color.parseColor("#979797"));
                textView.setTextSize(15);
                textView.setPadding(0, 0, UIUtil.dip2px(FindRankActivity.this, 2), UIUtil.dip2px(FindRankActivity.this, 2));
                return new TipViewHolder(textView);
            }
        });
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(ivQuestion, 0, -UIUtil.dip2px(FindRankActivity.this, 8));
    }

    class TipViewHolder extends BaseViewHolder {
        public TipViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            TextView itemView = (TextView) this.itemView;
            SpannableString title = StringUtils.spannableStringColor(tipTitles[position], Color.parseColor("#6e6e6e"));
            itemView.append(title);
            SpannableString desc = StringUtils.spannableStringAbsSize(tipDescs[position], 14);
            itemView.append(desc);
        }
    }
}

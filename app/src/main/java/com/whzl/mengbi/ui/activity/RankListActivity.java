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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.RankMotherFragment;
import com.whzl.mengbi.ui.fragment.WeekStarFragment;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/8/22
 */
public class RankListActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.sort_tab_layout)
    TabLayout sortTabLayout;
    private PopupWindow popupWindow;
    private String[] tipDescs;
    private String[] tipTitles;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
        tipTitles = getResources().getStringArray(R.array.rank_tip_title);
        tipDescs = getResources().getStringArray(R.array.rank_tip_desc);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_rank_list, R.string.rank, R.string.tip, true);
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        showPopWindow();
    }

    @Override
    protected void setupView() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("明星榜");
        titles.add("富豪榜");
        titles.add("人气榜");
        titles.add("周星榜");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RankMotherFragment.newInstance("CELEBRITY"));
        fragments.add(RankMotherFragment.newInstance("RICH"));
        fragments.add(RankMotherFragment.newInstance("POPULAR"));
        fragments.add(WeekStarFragment.newInstance());
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        sortTabLayout.setupWithViewPager(viewpager);
    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_rank_tip, null);
        RecyclerView recyclerView = popView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return tipDescs == null ? 0 : tipDescs.length;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(RankListActivity.this);
                textView.setTextColor(Color.parseColor("#979797"));
                textView.setTextSize(15);
                textView.setPadding(0, 0, UIUtil.dip2px(RankListActivity.this, 2), UIUtil.dip2px(RankListActivity.this, 2));
                return new TipViewHolder(textView);
            }
        });
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(toolbar, 0, -UIUtil.dip2px(this, 8));
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

    @Override
    protected void loadData() {

    }
}

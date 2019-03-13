package com.whzl.mengbi.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.fragment.base.BaseViewFragment;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author cliang
 * @date 2018.10.24
 */
public class RankFragment extends BaseViewFragment implements View.OnClickListener {

    private View layout;
    private RelativeLayout rlRankTitle;
    private TextView tvRankDesc;
    private TabLayout sortTabLayout;
    private ViewPager viewPager;
    private PopupWindow popupWindow;
    private String[] tipDescs;
    private String[] tipTitles;

    public static RankFragment newInstance() {
        return new RankFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_rank, container, false);
        rlRankTitle = layout.findViewById(R.id.rl_rank_title);
        tvRankDesc = layout.findViewById(R.id.tv_rank_desc);
        tvRankDesc.setOnClickListener(this);
        sortTabLayout = layout.findViewById(R.id.sort_tab_layout);
        viewPager = layout.findViewById(R.id.viewpager);
        initView();
        return layout;
    }

    private void initView(){
        tipTitles = getResources().getStringArray(R.array.rank_tip_title);
        tipDescs = getResources().getStringArray(R.array.rank_tip_desc);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("明星榜");
        titles.add("富豪榜");
        titles.add("人气榜");
        titles.add("周星榜");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RankMotherFragment.newInstance("CELEBRITY"));
        fragments.add(RankMotherFragment.newInstance("RICH"));
        fragments.add(RankMotherFragment.newInstance("POPULAR"));

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        sortTabLayout.setupWithViewPager(viewPager);
        sortTabLayout.clearOnTabSelectedListeners();
        sortTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_rank_tip, null);
        RecyclerView recyclerView = popView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getMyActivity()));
        recyclerView.setAdapter(new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return tipDescs == null ? 0 : tipDescs.length;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(getMyActivity());
                textView.setTextColor(Color.parseColor("#979797"));
                textView.setTextSize(15);
                textView.setPadding(0, 0, UIUtil.dip2px(getMyActivity(), 2), UIUtil.dip2px(getMyActivity(), 2));
                return new TipViewHolder(textView);
            }
        });
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(rlRankTitle, 0, -UIUtil.dip2px(getMyActivity(), 8));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_rank_desc:
                showPopWindow();
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
    }
}

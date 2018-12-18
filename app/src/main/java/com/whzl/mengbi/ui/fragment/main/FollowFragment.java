package com.whzl.mengbi.ui.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.animation.OvershootInterpolator;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.FollowSortFragment;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/12/18
 */
public class FollowFragment extends BaseFragment {
    @BindView(R.id.tab_follow)
    MagicIndicator tabFollow;
    @BindView(R.id.vp_follow)
    ViewPager vpFollow;
    private ArrayList<String> titles;

    public static Fragment newInstance() {
        FollowFragment followFragment = new FollowFragment();
        return followFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_follow_main;
    }

    @Override
    public void init() {
        titles = new ArrayList<>();
        titles.add("关注");
        titles.add("守护");
        titles.add("管理");
        titles.add("看过");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(com.whzl.mengbi.ui.fragment.FollowFragment.newInstance());
        fragments.add(FollowSortFragment.newInstance("guard"));
        fragments.add(FollowSortFragment.newInstance("manage"));
        fragments.add(FollowSortFragment.newInstance("watch"));
        vpFollow.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        vpFollow.setOffscreenPageLimit(4);
        initIndicator();
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getMyActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setSkimOver(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.parseColor("#40323232"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#323232"));
                simplePagerTitleView.setText(titles.get(index));
                TextPaint paint = simplePagerTitleView.getPaint();
                paint.setFakeBoldText(true);
                simplePagerTitleView.setOnClickListener(v -> vpFollow.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setRoundRadius(10);
                linePagerIndicator.setYOffset(UIUtil.dip2px(context,5));
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 11));
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2.5));
                linePagerIndicator.setColors(Color.parseColor("#ffd634"));
                return linePagerIndicator;
            }
        });
        tabFollow.setNavigator(commonNavigator);
//        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
//        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        titleContainer.setDividerDrawable(new ColorDrawable() {
//            @Override
//            public int getIntrinsicWidth() {
//                return UIUtil.dip2px(getMyActivity(), 5);
//            }
//        });

        final FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper(tabFollow);
        fragmentContainerHelper.setInterpolator(new OvershootInterpolator(1.5f));
        fragmentContainerHelper.setDuration(300);
        vpFollow.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                fragmentContainerHelper.handlePageSelected(position);
            }
        });
    }

}

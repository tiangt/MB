package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.RankListFragment;
import com.whzl.mengbi.ui.fragment.me.PropFragment;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class PackActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_pack, "背包", "说明", true);
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        ToastUtils.showToast("说明");
    }

    @Override
    protected void setupView() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("道具");
        titles.add("VIP");
        titles.add("靓号");
        titles.add("座驾");
        titles.add("优惠券");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(PropFragment.newInstance());
        fragments.add(RankListFragment.newInstance("RICH", "DAY", "F"));
        fragments.add(RankListFragment.newInstance("RICH", "DAY", "F"));
        fragments.add(RankListFragment.newInstance("RICH", "DAY", "F"));
        fragments.add(RankListFragment.newInstance("RICH", "DAY", "F"));
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        setTabWidth(tabLayout, UIUtil.dip2px(this, 18));
        tabLayout.setupWithViewPager(viewpager);
    }


    public void setTabWidth(final TabLayout tabLayout, final int padding) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);


                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void loadData() {

    }

}

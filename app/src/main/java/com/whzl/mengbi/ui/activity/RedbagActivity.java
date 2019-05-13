package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.RedBagFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2019/1/14
 */
public class RedbagActivity extends BaseActivity {
    @BindView(R.id.btn_note)
    ImageButton btnNote;
    @BindView(R.id.btn_close)
    ImageButton btnClose;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    public int programId;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_red_bag);
    }

    @Override
    protected void setupView() {
        programId = getIntent().getIntExtra("programId", 0);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.statusbar_black));
    }

    @Override
    protected void loadData() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("普通红包");
        titles.add("手气红包");
        titles.add("红包基金");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RedBagFragment.newInstance(RedBagFragment.NORMAL));
        fragments.add(RedBagFragment.newInstance(RedBagFragment.LUCK));
        fragments.add(RedBagFragment.newInstance(RedBagFragment.FUND));
        viewpager.setOffscreenPageLimit(titles.size());
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


    @OnClick({R.id.btn_note, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_note:
//                showNotePop();
                startActivity(new Intent(RedbagActivity.this, JsBridgeActivity.class)
                        .putExtra("title", "红包说明")
                        .putExtra("url", SPUtils.get(RedbagActivity.this, SpConfig.REDPACKETHELPURL, "").toString()));
                break;
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private void showNotePop() {
        View popView = getLayoutInflater().inflate(R.layout.popwindow_note_redbag, null);
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(btnNote, 0, UIUtil.dip2px(this, 6.5f));
    }

}

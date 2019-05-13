package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.RedBagFragment;
import com.whzl.mengbi.ui.fragment.RedFundFragment;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.whzl.mengbi.ui.fragment.RedBagFragment.LUCK;
import static com.whzl.mengbi.ui.fragment.RedBagFragment.NORMAL;

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
    @BindView(R.id.btn_send)
    Button btnSend;
    public int programId;

    private String type = NORMAL;

    private String[] types = new String[]{NORMAL, LUCK, RedBagFragment.FUND};
    private RedBagFragment normalFragment;
    private RedBagFragment luckFragment;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_red_bag);
    }

    @Override
    protected void setupView() {
        programId = getIntent().getIntExtra("programId", 0);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("普通红包");
        titles.add("手气红包");
        titles.add("红包基金");
        normalFragment = RedBagFragment.newInstance(NORMAL);
        luckFragment = RedBagFragment.newInstance(LUCK);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(normalFragment);
        fragments.add(luckFragment);
        fragments.add(RedFundFragment.Companion.newInstance());
        viewpager.setOffscreenPageLimit(titles.size());
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition(), false);
                type = types[tab.getPosition()];
                if (tab.getPosition() == 2) {
                    btnSend.setVisibility(View.INVISIBLE);
                } else {
                    btnSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                type = types[position];
                if (position == 2) {
                    btnSend.setVisibility(View.INVISIBLE);
                } else {
                    btnSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.statusbar_black));
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.btn_note, R.id.btn_close, R.id.btn_send})
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
            case R.id.btn_send:
                KeyBoardUtil.hide(this);
                if (type.equals(NORMAL)) {
                    if (normalFragment.checkNormal()) {
                        if (ClickUtil.isFastClick()) {
                            normalFragment.sendRedPack("NORMAL");
                        }
                    }
                } else if (type.equals(LUCK)) {
                    if (luckFragment.checkLuck()) {
                        if (ClickUtil.isFastClick()) {
                            luckFragment.sendRedPack("RANDOM");
                        }
                    }
                }
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

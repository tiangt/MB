package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.PersonalInfoBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.me.MyAnchorLevelFragment;
import com.whzl.mengbi.ui.fragment.me.MyRegalLevelFragment;
import com.whzl.mengbi.ui.fragment.me.MyRoyalLevelFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.youth.banner.transformer.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的等级
 *
 * @author cliang
 * @date 2019.2.18
 */
public class MyLevelActivity extends BaseActivity {

    @BindView(R.id.tab_layout_head)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private long mUserId;
    private PersonalInfoBean.DataBean dataBean;
    private String levelType;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_level, "我的等级", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
    }

    @Override
    protected void loadData() {
        mUserId = getIntent().getLongExtra("userId", 0);
        getMyLevelInfo(mUserId, mUserId);
    }

    private void initView(String levelType) {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("贵族等级");
        titles.add("富豪等级");
        if ("ANCHOR_LEVEL".equals(levelType)) {
            titles.add("主播等级");
        }

        fragments = new ArrayList<>();
        fragments.add(MyRoyalLevelFragment.newInstance(dataBean));
        fragments.add(MyRegalLevelFragment.newInstance(dataBean));
        if ("ANCHOR_LEVEL".equals(levelType)) {
            fragments.add(MyAnchorLevelFragment.newInstance(dataBean));
        }

        viewPager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        viewPager.setPageTransformer(true, new ZoomOutTranformer());

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(50);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getMyLevelInfo(long userId, long visitorId) {
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("visitorId", visitorId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.HOME_PAGE, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                PersonalInfoBean personalInfoBean = GsonUtils.GsonToBean(result.toString(), PersonalInfoBean.class);
                if (personalInfoBean.getCode() == 200) {
                    if (personalInfoBean.getData() != null) {
                        dataBean = personalInfoBean.getData();
                        for (int i = 0; i < dataBean.getLevelList().size(); i++) {
                            if ("ANCHOR_LEVEL".equals(dataBean.getLevelList().get(i).getLevelType())) {
                                levelType = dataBean.getLevelList().get(i).getLevelType();
                                break;
                            }
                        }
                        initView(levelType);
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }
}

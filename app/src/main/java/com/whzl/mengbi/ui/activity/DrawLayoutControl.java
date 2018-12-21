package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.LivePkEvent;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.PackActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.me.WelfareFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/21
 */
public class DrawLayoutControl {
    private Activity activity;
    private LinearLayout drawLayout;
    private List<GetActivityBean.ListBean> bannerInfoList;
    private RecyclerView rvActivity;
    private BaseListAdapter baseListAdapter;
    private Switch switchVoice;
    private RecyclerView rvTips;
    private String[] titles;
    private int[] imgIds = new int[]{
            R.drawable.ic_recharge_draw_layout,
            R.drawable.ic_shop_draw_layout,
            R.drawable.ic_pack_draw_layout,
            R.drawable.ic_welfare_draw_layout,
            R.drawable.ic_guard_draw_layout,
            R.drawable.ic_set_draw_layout,
            R.drawable.ic_manage_draw_layout};

    public DrawLayoutControl(Activity liveDisplayActivity, LinearLayout drawLayout) {
        this.activity = liveDisplayActivity;
        this.drawLayout = drawLayout;
    }

    public void init() {
        bannerInfoList = new ArrayList<>();
        switchVoice = drawLayout.findViewById(R.id.switch_voice_draw_layout_live);
        rvActivity = drawLayout.findViewById(R.id.rv_activity_draw_layout);
        rvTips = drawLayout.findViewById(R.id.rv_tips_draw_layout);

        initActivityRV();
        initSwitch();
        initTipsRV();
    }

    private void initTipsRV() {
        titles = activity.getResources().getStringArray(R.array.tips_title_draw_layout);
        rvTips.setLayoutManager(new GridLayoutManager(activity, 4));
        rvTips.setAdapter(new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return titles.length;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(activity).inflate(R.layout.item_tips_draw_layout, parent, false);
                return new TipsHolder(itemView);
            }
        });
    }

    private void initSwitch() {
        switchVoice.setChecked((boolean) SPUtils.get(activity, SpConfig.PK_VIOCE_LIVE, false));
        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.put(activity, SpConfig.PK_VIOCE_LIVE, true);
                } else {
                    SPUtils.put(activity, SpConfig.PK_VIOCE_LIVE, false);
                }
                EventBus.getDefault().post(new LivePkEvent());
            }
        });
    }

    private void initActivityRV() {
        rvActivity.setLayoutManager(new GridLayoutManager(activity, 3));
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return bannerInfoList == null ? 1 : bannerInfoList.size() + 1;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(activity).inflate(R.layout.item_activity_draw_layout, parent, false);
                return new Holder(itemView);
            }

        };
        rvActivity.setAdapter(baseListAdapter);
    }


    public void notifyData(List<GetActivityBean.ListBean> mBannerInfoList) {
        if (mBannerInfoList == null) {
            return;
        }
        this.bannerInfoList.clear();
        this.bannerInfoList.addAll(mBannerInfoList);
        baseListAdapter.notifyDataSetChanged();
    }

    class Holder extends BaseViewHolder {
        @BindView(R.id.iv_activity)
        ImageView ivActivity;
        @BindView(R.id.tv_activity)
        TextView tvActivity;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (position == bannerInfoList.size()) {
                GlideImageLoader.getInstace().displayImage(activity, R.drawable.ic_wait_draw_layout_live, ivActivity);
                tvActivity.setText("敬请期待");
                return;
            }
            GetActivityBean.ListBean listBean = bannerInfoList.get(position);
            GlideImageLoader.getInstace().displayImage(activity, listBean.imageUrl, ivActivity);
            tvActivity.setText(listBean.name);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (position == bannerInfoList.size()) {
                return;
            }
            ((LiveDisplayActivity) activity).jumpToBannerActivity(bannerInfoList.get(position));
        }
    }

    class TipsHolder extends BaseViewHolder {
        @BindView(R.id.iv_tips_draw_layout)
        ImageView ivTips;
        @BindView(R.id.tv_tips_draw_layout)
        TextView tvTips;

        public TipsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvTips.setText(titles[position]);
            GlideImageLoader.getInstace().displayImage(activity, imgIds[position], ivTips);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            switch (position) {
                case 0:
                    Intent intent = new Intent(activity, WXPayEntryActivity.class);
                    activity.startActivity(intent);
                    break;
                case 1:
                    activity.startActivity(new Intent(activity, ShopActivity.class));
                    break;
                case 2:
                    activity.startActivity(new Intent(activity, PackActivity.class));
                    break;
                case 3:
                    activity.startActivity(new Intent(activity, FrgActivity.class)
                            .putExtra(FrgActivity.FRAGMENT_CLASS, WelfareFragment.class));
                    break;
                case 4:
                    ((LiveDisplayActivity) activity).showGuardDialog();
                    break;
                case 5:
                    activity.startActivity(new Intent(activity, SettingActivity.class));
                    break;
                case 6:
                    activity.startActivity(new Intent(activity, CustomServiceCenterActivity.class));
                    break;
            }
        }
    }

}

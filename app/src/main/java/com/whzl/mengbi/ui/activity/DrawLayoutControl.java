package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.RecommendAnchorInfoBean;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.EffectActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.RedpackDialog;
import com.whzl.mengbi.ui.fragment.me.WelfareFragment;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/21
 */
public class DrawLayoutControl {
    private Activity activity;
    private NestedScrollView drawLayout;
    private List<GetActivityBean.ListBean> bannerInfoList;
    private RecyclerView rvActivity;
    private BaseListAdapter baseListAdapter;
    private RecyclerView rvTips;
    private String[] titles;
    private int[] imgIds = new int[]{
            R.drawable.ic_recharge_draw_layout,
            R.drawable.ic_shop_draw_layout,
            R.drawable.ic_welfare_draw_layout,
            R.drawable.ic_pk_draw_layout,
            R.drawable.ic_guard_draw_layout,
            R.drawable.ic_effect_draw_layout,
            R.drawable.ic_set_draw_layout,
            R.drawable.ic_manage_draw_layout};
    private RecyclerView recommendRecycler;
    private BaseListAdapter recommendAdapter;
    private List<RecommendAnchorInfoBean> mRecommendAnchorInfoList = new ArrayList();

    public DrawLayoutControl(Activity liveDisplayActivity, NestedScrollView drawLayout) {
        this.activity = liveDisplayActivity;
        this.drawLayout = drawLayout;
    }

    public void init() {
        bannerInfoList = new ArrayList<>();
        rvActivity = drawLayout.findViewById(R.id.rv_activity_draw_layout);
        rvTips = drawLayout.findViewById(R.id.rv_tips_draw_layout);
        recommendRecycler = drawLayout.findViewById(R.id.rv_anchor_draw_layout);

        initActivityRV();
        initTipsRV();
        initAnchorRV();
        loadAnchorData();
    }

    private void loadAnchorData() {
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECOMMEND_ANCHOR, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        RecommendInfo recommendInfo = JSON.parseObject(jsonStr, RecommendInfo.class);
                        if (recommendInfo.getCode() == 200) {
                            if (recommendInfo.getData() != null) {
                                mRecommendAnchorInfoList.clear();
                                mRecommendAnchorInfoList.addAll(recommendInfo.getData().getList());
                                recommendAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
    }

    private void initAnchorRV() {
        recommendRecycler.setNestedScrollingEnabled(false);
        recommendRecycler.setFocusableInTouchMode(false);
        recommendRecycler.setHasFixedSize(true);
        recommendRecycler.setLayoutManager(new GridLayoutManager(activity, 2));
        recommendRecycler.addItemDecoration(new SpacesItemDecoration(UIUtil.dip2px(activity, 5.5f)));
        recommendAdapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return mRecommendAnchorInfoList == null ? 0 : mRecommendAnchorInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(activity).inflate(R.layout.item_anchor_drawlayout, null);
                return new AnchorInfoViewHolder(itemView);
            }
        };
        recommendRecycler.setAdapter(recommendAdapter);
    }

    class AnchorInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.tv_is_live_mark)
        TextView tvIsLive;


        public AnchorInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            RecommendAnchorInfoBean recommendAnchorInfoBean = mRecommendAnchorInfoList.get(position);
            tvIsLive.setVisibility("T".equals(recommendAnchorInfoBean.getStatus()) ? View.VISIBLE : View.GONE);
            String recommendName = recommendAnchorInfoBean.getAnchorNickname();
            if (recommendName.length() > 8) {
                tvAnchorName.setText(recommendName.substring(0, 8) + "...");
            } else {
                tvAnchorName.setText(recommendAnchorInfoBean.getAnchorNickname());
            }
            GlideImageLoader.getInstace().displayProgramCover(activity, recommendAnchorInfoBean.getCover(), ivCover, 5);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RecommendAnchorInfoBean recommendAnchorInfoBean = mRecommendAnchorInfoList.get(recommendAdapter.getRealPosition(this));
            if (((LiveDisplayActivity) activity).mProgramId == recommendAnchorInfoBean.getProgramId()) {
                return;
            }
            ((LiveDisplayActivity) activity).jumpToLive(recommendAnchorInfoBean.getProgramId());
            ((LiveDisplayActivity) activity).closeDrawLayoutNoAnimal();
        }
    }

    /**
     * 中间快捷
     */
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


    /**
     * 上方活动
     */
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
//            if (position == bannerInfoList.size() + 1) {
//                GlideImageLoader.getInstace().displayImage(activity, R.drawable.ic_wait_draw_layout_live, ivActivity);
//                tvActivity.setText("敬请期待");
//                return;
//            }
            if (position == bannerInfoList.size()) {
                GlideImageLoader.getInstace().displayImageNoCache(activity, R.drawable.ic_redpacket_draw_layout_live, ivActivity);
                tvActivity.setText("红包");
                return;
            }
            GetActivityBean.ListBean listBean = bannerInfoList.get(position);
            GlideImageLoader.getInstace().displayImage(activity, listBean.imageUrl, ivActivity);
            tvActivity.setText(listBean.name);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
//            if (position == bannerInfoList.size() + 1) {
//                return;
//            }

            if (position == bannerInfoList.size()) {
                //红包
                if (((LiveDisplayActivity) activity).mUserId == 0) {
                    ((LiveDisplayActivity) activity).login();
                    ((LiveDisplayActivity) activity).closeDrawLayoutNoAnimal();
                    return;
                }
//                activity.startActivity(new Intent(activity, RedbagActivity.class).
//                        putExtra("programId", ((LiveDisplayActivity) activity).mProgramId));
                RedpackDialog.newInstance(((LiveDisplayActivity) activity).mProgramId)
                        .show(((LiveDisplayActivity) activity).getSupportFragmentManager());
                ((LiveDisplayActivity) activity).closeDrawLayoutNoAnimal();
                return;
            }
            if (bannerInfoList.get(position).flag != null) {
                if (bannerInfoList.get(position).flag.equals(AppConfig.LUCK_ROB)) {
                    ((LiveDisplayActivity) activity).showSnatchDialog();
                } else if (bannerInfoList.get(position).flag.equals(AppConfig.GUESS)) {
                    ((LiveDisplayActivity) activity).showGuessDialog();
                } else if (bannerInfoList.get(position).flag.equals(AppConfig.CARDGAME)) {
                    ((LiveDisplayActivity) activity).jumpToFlopActivity();
                } else {
                    ToastUtils.showToastUnify(activity, "请升级版本");
                }
            } else {
                ((LiveDisplayActivity) activity).jumpToBannerActivity(bannerInfoList.get(position));
                ((LiveDisplayActivity) activity).closeDrawLayoutNoAnimal();
            }
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
            ((LiveDisplayActivity) activity).closeDrawLayoutNoAnimal();
            switch (position) {
                case 0:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    Intent intent = new Intent(activity, WXPayEntryActivity.class);
                    activity.startActivity(intent);
                    break;
                case 1:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    activity.startActivity(new Intent(activity, ShopActivity.class));
                    break;
//                case 2:
//                    if (((LiveDisplayActivity) activity).mUserId == 0) {
//                        ((LiveDisplayActivity) activity).login();
//                        return;
//                    }
//                    activity.startActivity(new Intent(activity, PackActivity.class));
//                    break;
                case 2:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    activity.startActivity(new Intent(activity, FrgActivity.class)
                            .putExtra(FrgActivity.FRAGMENT_CLASS, WelfareFragment.class));
                    break;
                case 3:
                    ((LiveDisplayActivity) activity).jumpToBlackRoomActivity();
                    break;
                case 4:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    ((LiveDisplayActivity) activity).showGuardDialog();
                    break;
                case 5:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    EffectActivity.Companion.start(activity);
                    break;
                case 6:
                    if (((LiveDisplayActivity) activity).mUserId == 0) {
                        ((LiveDisplayActivity) activity).login();
                        return;
                    }
                    activity.startActivity(new Intent(activity, SettingActivity.class).putExtra("from", "live"));
                    break;
                case 7:
                    activity.startActivity(new Intent(activity, CustomServiceCenterActivity.class));
                    break;
            }
        }
    }

}

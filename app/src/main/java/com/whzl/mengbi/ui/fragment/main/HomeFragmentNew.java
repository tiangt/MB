package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.LiveShowListInfo;
import com.whzl.mengbi.model.entity.RecommendAnchorInfoBean;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.presenter.impl.HomePresenterImpl;
import com.whzl.mengbi.ui.activity.CommWebActivity;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.LoginActivity;
import com.whzl.mengbi.ui.activity.RankListActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.HomeView;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;


/**
 * @author shaw
 * @date 2018/7/18
 */
public class HomeFragmentNew extends BaseFragment implements HomeView {
    private static final int TYPE_RECOMMEND = 250;
    private static final int TYPE_ANCHOR = 520;
    @BindView(R.id.bannerLayout)
    ConstraintLayout bannerLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recommend_recycler)
    RecyclerView recommendRecycler;
    @BindView(R.id.anchor_recycler)
    RecyclerView anchorRecycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private HomePresenterImpl mHomePresenter;
    private int mCurrentPager = 1;
    private ArrayList<RecommendAnchorInfoBean> mRecommendAnchorInfoList = new ArrayList<>();
    private BaseListAdapter recommendAdapter;
    private ArrayList<LiveShowListInfo> mAnchorInfoList = new ArrayList<>();
    private BaseListAdapter anchorAdapter;
    private List<BannerInfo.DataBean.ListBean> mBannerInfoList;

    @Override
    protected void initEnv() {
        super.initEnv();
        mHomePresenter = new HomePresenterImpl(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    public void init() {
        initBanner();
        initRecommendRecycler();
        initAnchorRecycler();
        loadData();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.setEnableLoadMore(true);
            mCurrentPager = 1;
            mHomePresenter.getBanner();
            mHomePresenter.getRecommend();
            mHomePresenter.getAnchorList(mCurrentPager++);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mHomePresenter.getAnchorList(mCurrentPager++));
    }

    private void initRecommendRecycler() {
        recommendRecycler.setNestedScrollingEnabled(false);
        recommendRecycler.setFocusableInTouchMode(false);
        recommendRecycler.setHasFixedSize(true);
        recommendRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recommendAdapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return mRecommendAnchorInfoList == null ? 0 : mRecommendAnchorInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anchor_home, null);
                return new AnchorInfoViewHolder(itemView, TYPE_RECOMMEND);
            }
        };
        recommendRecycler.setAdapter(recommendAdapter);
    }

    private void initAnchorRecycler() {
        anchorRecycler.setNestedScrollingEnabled(false);
        anchorRecycler.setFocusableInTouchMode(false);
        anchorRecycler.setHasFixedSize(true);
        anchorRecycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        anchorRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        anchorAdapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return mAnchorInfoList == null ? 0 : mAnchorInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anchor_home, parent, false);
                return new AnchorInfoViewHolder(itemView, TYPE_ANCHOR);
            }
        };
        anchorRecycler.setAdapter(anchorAdapter);
    }

    class AnchorInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.iv_is_live_mark)
        ImageView ivIsLive;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.tv_watch_count)
        TextView tvWatchCount;

        private final int type;

        public AnchorInfoViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            switch (type) {
                case TYPE_RECOMMEND:
                    RecommendAnchorInfoBean recommendAnchorInfoBean = mRecommendAnchorInfoList.get(position);
                    ivIsLive.setVisibility("T".equals(recommendAnchorInfoBean.getStatus()) ? View.VISIBLE : View.GONE);
                    tvAnchorName.setText(recommendAnchorInfoBean.getAnchorNickname());
                    tvWatchCount.setText(recommendAnchorInfoBean.getRoomUserCount() + "");
                    GlideImageLoader.getInstace().loadRoundImage(getContext(), recommendAnchorInfoBean.getCover(), ivCover, 5);
                    break;
                case TYPE_ANCHOR:
                    LiveShowListInfo liveShowListInfo = mAnchorInfoList.get(position);
                    ivIsLive.setVisibility("T".equals(liveShowListInfo.getStatus()) ? View.VISIBLE : View.GONE);
                    tvAnchorName.setText(liveShowListInfo.getAnchorNickname());
                    tvWatchCount.setText(liveShowListInfo.getRoomUserCount() + "");
                    GlideImageLoader.getInstace().loadRoundImage(getContext(), liveShowListInfo.getCover(), ivCover, 5);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
            switch (type) {
                case TYPE_RECOMMEND:
                    RecommendAnchorInfoBean recommendAnchorInfoBean = mRecommendAnchorInfoList.get(position);
                    intent.putExtra(BundleConfig.PROGRAM_ID, recommendAnchorInfoBean.getProgramId());
                    break;
                case TYPE_ANCHOR:
                    LiveShowListInfo liveShowListInfo = mAnchorInfoList.get(position);
                    intent.putExtra(BundleConfig.PROGRAM_ID, liveShowListInfo.getProgramId());
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader.getInstace());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(position -> {
            if (mBannerInfoList != null && mBannerInfoList.size() > 0) {
                BannerInfo.DataBean.ListBean listBean = mBannerInfoList.get(position);
                if ("rank".equals(listBean.getType())) {
                    Intent intent = new Intent(getContext(), RankListActivity.class);
                    startActivity(intent);
                }
                if ("room".equals(listBean.getType()) && listBean.getTarget() > 0) {
                    Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
                    intent.putExtra("programId", listBean.getTarget());
                    startActivity(intent);
                }
                if ("web".equals(listBean.getType()) && !TextUtils.isEmpty(listBean.getUrl())) {
                    Intent intent = new Intent(getContext(), CommWebActivity.class);
                    intent.putExtra("title", listBean.getTitle());
                    intent.putExtra("url", listBean.getUrl());
                    startActivity(intent);
                }
                if ("recharge".equals(listBean.getType())) {
                    long userId = (long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
                    if (userId == 0) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void loadData() {
        mHomePresenter.getBanner();
        mHomePresenter.getRecommend();
        mHomePresenter.getAnchorList(mCurrentPager++);
    }

    @Override
    public void showBanner(BannerInfo bannerInfo) {
        if (bannerInfo.getData() == null
                || bannerInfo.getData().getList() == null
                || bannerInfo.getData().getList().isEmpty()) {
            bannerLayout.setVisibility(View.GONE);
            return;
        }
        bannerLayout.setVisibility(View.VISIBLE);
        mBannerInfoList = bannerInfo.getData().getList();
        ArrayList<String> banners = new ArrayList<>();
        for (int i = 0; i < mBannerInfoList.size(); i++) {
            banners.add(mBannerInfoList.get(i).getImage());
        }
        banner.setImages(banners);
        banner.start();
    }

    @Override
    public void showRecommend(RecommendInfo recommendInfo) {
        if (recommendInfo != null && recommendInfo.getData() != null) {
            mRecommendAnchorInfoList.clear();
            mRecommendAnchorInfoList.addAll(recommendInfo.getData().getList());
            recommendAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLiveShow(LiveShowInfo liveShowInfo) {
        if (liveShowInfo != null && liveShowInfo.getData() != null && liveShowInfo.getData().getList() != null) {
            if (mCurrentPager == 2) {
                mAnchorInfoList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mAnchorInfoList.addAll(liveShowInfo.getData().getList());
            if (liveShowInfo.getData().getList() == null || liveShowInfo.getData().getList().size() < NetConfig.DEFAULT_PAGER_SIZE) {
                anchorAdapter.notifyDataSetChanged();
                if (mAnchorInfoList.size() > 0) {
                    anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                } else {
                    anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                }
                refreshLayout.postDelayed(() -> refreshLayout.setEnableLoadMore(false), 300);
            } else {
                refreshLayout.setEnableLoadMore(true);
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                anchorAdapter.notifyDataSetChanged();
            }
        } else {
            if (mAnchorInfoList.size() > 0) {
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            }
            refreshLayout.postDelayed(() -> refreshLayout.setEnableLoadMore(false), 300);
        }
    }

    @Override
    public void onError(String msg) {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        mCurrentPager--;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePresenter.onDestroy();
    }
}

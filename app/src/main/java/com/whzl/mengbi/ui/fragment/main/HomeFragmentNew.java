package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.LiveShowListInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.model.entity.RecommendAnchorInfoBean;
import com.whzl.mengbi.presenter.impl.HomePresenterImpl;
import com.whzl.mengbi.ui.activity.LiveDisplayActivityNew;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.HomeView;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author shaw
 * @date 2018/7/18
 */
public class HomeFragmentNew extends BaseFragment implements HomeView {
    private static final int TYPE_RECOMMEND = 250;
    private static final int TYPE_ANCHOR = 520;
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
            refreshLayout.setEnableAutoLoadMore(true);
            mCurrentPager = 1;
            mHomePresenter.getBanner();
            mHomePresenter.getRecommend();
            mHomePresenter.getAnchorList(mCurrentPager++);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mHomePresenter.getAnchorList(mCurrentPager++));
    }

    private void initRecommendRecycler() {

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
            Intent intent = new Intent(getContext(), LiveDisplayActivityNew.class);
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
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
    }

    private void loadData() {
        mHomePresenter.getBanner();
        mHomePresenter.getRecommend();
        mHomePresenter.getAnchorList(mCurrentPager++);
    }

    @Override
    public void showBanner(BannerInfo bannerInfo) {
        List<BannerInfo.DataBean.ListBean> bannerInfoList = bannerInfo.getData().getList();
        ArrayList<String> banners = new ArrayList<>();
        for (int i = 0; i < bannerInfoList.size(); i++) {
            banners.add(bannerInfoList.get(i).getImage());
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
        if (liveShowInfo != null || liveShowInfo.getData() != null) {
            if (mCurrentPager == 2) {
                mAnchorInfoList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mAnchorInfoList.addAll(liveShowInfo.getData().getList());
            if (liveShowInfo.getData().getList() == null || liveShowInfo.getData().getList().size() < NetConfig.DEFAULT_PAGER_SIZE) {
                anchorAdapter.notifyDataSetChanged();
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }, 300);
            } else {
                refreshLayout.setEnableLoadMore(true);
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                anchorAdapter.notifyDataSetChanged();
            }
        } else {
            if (mAnchorInfoList.size() > 0) {
                anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            }
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setEnableLoadMore(false);
                }
            }, 300);
        }
    }

}

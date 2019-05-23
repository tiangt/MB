package com.whzl.mengbi.ui.fragment.main;

import android.animation.Animator;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.HomeRefreshEvent;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.HeadlineTopInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.LiveShowListInfo;
import com.whzl.mengbi.model.entity.RecommendAnchorInfoBean;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.presenter.impl.HomePresenterImpl;
import com.whzl.mengbi.ui.activity.HistoryListActivity;
import com.whzl.mengbi.ui.activity.JsBridgeActivity;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.LoginActivity;
import com.whzl.mengbi.ui.activity.RankListActivity;
import com.whzl.mengbi.ui.activity.SearchActivity;
import com.whzl.mengbi.ui.adapter.BaseAnimation;
import com.whzl.mengbi.ui.adapter.SlideInRightAnimation;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.HomeView;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.glide.RoundImageLoader;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author shaw
 * @date 2018/7/18
 */
public class HomeFragment extends BaseFragment implements HomeView {
    private static final int TYPE_RECOMMEND = 250;
    private static final int TYPE_ANCHOR = 520;
    @BindView(R.id.anchor_recycler_home)
    RecyclerView anchorRecycler;
    @BindView(R.id.refresh_layout_home)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_history)
    ImageView ivHistory;

    //    @BindView(R.id.rl_search)
//    RelativeLayout rlSearch;
    private HomePresenterImpl mHomePresenter;
    private int mCurrentPager = 1;
    private ArrayList<RecommendAnchorInfoBean> mRecommendAnchorInfoList = new ArrayList<>();
    private BaseListAdapter recommendAdapter;
    private ArrayList<LiveShowListInfo> mAnchorInfoList = new ArrayList<>();
    private BaseListAdapter anchorAdapter;
    private List<BannerInfo.DataBean.ListBean> mBannerInfoList;
    private Banner banner;
    private ConstraintLayout bannerLayout;
    private RecyclerView recommendRecycler;
    private LinearLayout rlTopThree;
    private RecyclerView topThreeRecycler;
    private BaseListAdapter topThreeAdapter;
    private ArrayList<HeadlineTopInfo.DataBean.ListBean> mHeadlineList = new ArrayList<>();
    private boolean needAnimal = false;

    @Override
    protected void initEnv() {
        super.initEnv();
        mHomePresenter = new HomePresenterImpl(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        initAnchorRecycler();
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setEnableOverScrollDrag(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.setEnableLoadMore(true);
            anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            mCurrentPager = 1;
            mLastPosition = -1;
            mLastPositionB = -1;
            mHomePresenter.getBanner();
            mHomePresenter.getHeadlineTop();
            mHomePresenter.getRecommend();
            mHomePresenter.getAnchorList(mCurrentPager++);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> mHomePresenter.getAnchorList(mCurrentPager++));
        loadData();
    }

    private void initRecommendRecycler() {
        recommendRecycler.setNestedScrollingEnabled(false);
        recommendRecycler.setFocusableInTouchMode(false);
        recommendRecycler.setHasFixedSize(true);
        recommendRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recommendRecycler.addItemDecoration(new SpacesItemDecoration(5));
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
        anchorRecycler.addItemDecoration(new SpacesItemDecoration(5));

        //RecyclerView缓存，预防OOM
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool() {
            @Override
            public void putRecycledView(RecyclerView.ViewHolder scrap) {
                super.putRecycledView(scrap);
            }

            @Override
            public RecyclerView.ViewHolder getRecycledView(int viewType) {
                final RecyclerView.ViewHolder recycledView = super.getRecycledView(viewType);
                return recycledView;
            }
        };

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
        pool.setMaxRecycledViews(anchorAdapter.getItemViewType(0), 10);

        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_home, refreshLayout, false);
        anchorAdapter.addHeaderView(view);
        initHead(view);
    }

    private void initHead(View view) {
        banner = view.findViewById(R.id.banner);
        bannerLayout = view.findViewById(R.id.bannerLayout);
        recommendRecycler = view.findViewById(R.id.recommend_recycler);
        rlTopThree = view.findViewById(R.id.rl_top_three);
        topThreeRecycler = view.findViewById(R.id.rv_top_three);
        initRecommendRecycler();
        initBanner();
        initTopThreeRecycler();
    }


    @OnClick({R.id.iv_search, R.id.iv_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getMyActivity(), SearchActivity.class));
                break;

            case R.id.iv_history:
                Intent intent = new Intent(getContext(), HistoryListActivity.class);
                intent.putExtra("index", 3);
                startActivity(intent);
                break;
        }
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
        @BindView(R.id.tv_is_live_mark)
        TextView tvIsLive;
        @BindView(R.id.iv_pk)
        ImageView ivIsPk;
        @BindView(R.id.iv_week_star)
        ImageView ivWeekStar;
        @BindView(R.id.iv_popularity)
        ImageView ivPop;

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
                    tvIsLive.setVisibility("T".equals(recommendAnchorInfoBean.getStatus()) ? View.VISIBLE : View.GONE);
                    ivIsPk.setVisibility("T".equals(recommendAnchorInfoBean.getIsPk()) ? View.VISIBLE : View.GONE);
                    ivWeekStar.setVisibility("T".equals(recommendAnchorInfoBean.getIsWeekStar()) ? View.VISIBLE : View.GONE);
                    ivPop.setVisibility("T".equals(recommendAnchorInfoBean.getIsPopularity()) ? View.VISIBLE : View.GONE);
                    String recommendName = recommendAnchorInfoBean.getAnchorNickname();
                    if (recommendName.length() > 8) {
                        tvAnchorName.setText(recommendName.substring(0, 8) + "...");
                    } else {
                        tvAnchorName.setText(recommendAnchorInfoBean.getAnchorNickname());
                    }
                    tvWatchCount.setText(recommendAnchorInfoBean.getRoomUserCount() + "");
                    GlideImageLoader.getInstace().displayProgramCover(getContext(), recommendAnchorInfoBean.getCover(), ivCover, 5);
                    if (needAnimal) {
                        addAnimation(this, position, TYPE_RECOMMEND);
                    }
                    break;
                case TYPE_ANCHOR:
                    LiveShowListInfo liveShowListInfo = mAnchorInfoList.get(position);
                    tvIsLive.setVisibility("T".equals(liveShowListInfo.getStatus()) ? View.VISIBLE : View.GONE);
                    ivIsPk.setVisibility("T".equals(liveShowListInfo.getIsPk()) ? View.VISIBLE : View.GONE);
                    ivWeekStar.setVisibility("T".equals(liveShowListInfo.getIsWeekStar()) ? View.VISIBLE : View.GONE);
                    ivPop.setVisibility("T".equals(liveShowListInfo.getIsPopularity()) ? View.VISIBLE : View.GONE);
                    String anchorName = liveShowListInfo.getAnchorNickname();
                    if (anchorName.length() > 8) {
                        tvAnchorName.setText(anchorName.substring(0, 8) + "...");
                    } else {
                        tvAnchorName.setText(liveShowListInfo.getAnchorNickname());
                    }
                    tvWatchCount.setText(liveShowListInfo.getRoomUserCount() + "");
                    GlideImageLoader.getInstace().displayProgramCover(getContext(), liveShowListInfo.getCover(), ivCover, 5);
                    if (needAnimal) {
                        addAnimation(this, position, TYPE_ANCHOR);
                    }
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
                    LiveShowListInfo liveShowListInfo = mAnchorInfoList.get(position - 1);
                    intent.putExtra(BundleConfig.PROGRAM_ID, liveShowListInfo.getProgramId());
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }

    private int mLastPosition = -1;
    private int mLastPositionB = -1;

    public void addAnimation(RecyclerView.ViewHolder holder, int position, int typeRecommend) {
        switch (typeRecommend) {
            case TYPE_ANCHOR:
                if (position > mLastPosition) {
                    BaseAnimation animation = new SlideInRightAnimation();
                    for (Animator anim : animation.getAnimators(holder.itemView)) {
                        startAnim(anim);
                    }
                    mLastPosition = position;
                }
                break;
            case TYPE_RECOMMEND:
                if (position > mLastPositionB) {
                    BaseAnimation animation = new SlideInRightAnimation();
                    for (Animator anim : animation.getAnimators(holder.itemView)) {
                        startAnim(anim);
                    }
                    mLastPositionB = position;
                }
                break;
        }

    }

    private void startAnim(Animator animator) {
        animator.setDuration(400).start();
        animator.setInterpolator(new LinearInterpolator());
    }


    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(RoundImageLoader.getInstace());
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
                    Intent intent = new Intent(getContext(), JsBridgeActivity.class);
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
        mHomePresenter.getHeadlineTop();
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
        //自动加载更多
//        refreshLayout.setEnableAutoLoadMore(false);
        if (liveShowInfo != null && liveShowInfo.getData() != null && liveShowInfo.getData().getList() != null) {
            if (mCurrentPager == 2) {
                mAnchorInfoList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mAnchorInfoList.addAll(liveShowInfo.getData().getList());
            if (liveShowInfo.getData().getList() == null || liveShowInfo.getData().getList().size() < 50) {
                anchorAdapter.notifyDataSetChanged();
                if (mAnchorInfoList.size() > 0) {
                    anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                } else {
                    anchorAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                }
                refreshLayout.postDelayed(() -> refreshLayout.setEnableLoadMore(false), 300);
            } else {
                refreshLayout.setEnableLoadMore(true);
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
    public void showHeadlineTop(HeadlineTopInfo headlineTopInfo) {
        if (headlineTopInfo != null && headlineTopInfo.getData() != null && headlineTopInfo.getData().getList() != null) {
            if (headlineTopInfo.getData().getList().size() == 0) {
                rlTopThree.setVisibility(View.GONE);
            } else {
//                if (mCurrentPager == 2) {
//                mAnchorInfoList.clear();
//                    refreshLayout.finishRefresh();
//                }
                rlTopThree.setVisibility(View.VISIBLE);
                mHeadlineList.clear();
                mHeadlineList.addAll(headlineTopInfo.getData().getList());
                topThreeAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 头条前三
     */
    private void initTopThreeRecycler() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getMyActivity());
        layoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        topThreeRecycler.setLayoutManager(layoutManage);
        topThreeRecycler.addItemDecoration(new SpacesItemDecoration(10));
        topThreeAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mHeadlineList == null ? 0 : (mHeadlineList.size() > 3 ? 3 : mHeadlineList.size());
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_top_three, null);
                return new TopThreeViewHolder(itemView);
            }
        };
        topThreeRecycler.setAdapter(topThreeAdapter);
    }

    class TopThreeViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_top_avatar)
        CircleImageView ivTopAvatar;
        @BindView(R.id.im)
        ImageView ivTopBg;

        public TopThreeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mHeadlineList.get(position).getAnchorAvatar(), ivTopAvatar);
            if (0 == position) {
                ivTopBg.setImageResource(R.drawable.ic_head_top_1);
            } else if (1 == position) {
                ivTopBg.setImageResource(R.drawable.ic_head_top_2);
            } else if (2 == position) {
                ivTopBg.setImageResource(R.drawable.ic_head_top_3);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, mHeadlineList.get(position).getProgramId());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeRefreshEvent event) {
        if (!anchorRecycler.canScrollVertically(-1)) {
            return;
        }
        anchorRecycler.smoothScrollToPosition(0);
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
        EventBus.getDefault().unregister(this);
    }

    private boolean checkLogin() {
        String sessionId = (String) SPUtils.get(getMyActivity(), SpConfig.KEY_SESSION_ID, "");
        long userId = Long.parseLong(SPUtils.get(getMyActivity(), "userId", (long) 0).toString());
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            return false;
        }
        return true;
    }

}

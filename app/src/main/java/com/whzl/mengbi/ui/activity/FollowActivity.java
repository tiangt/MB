package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorFollowedDataBean;
import com.whzl.mengbi.model.entity.RecommendAnchorInfoBean;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.activity.me.PlayNotifyActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.FollowFragment;
import com.whzl.mengbi.ui.widget.recyclerview.SpacesItemDecoration;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author shaw
 * @date 2018/7/23
 */
public class FollowActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recommend_recycler)
    RecyclerView recommendRecycler;
    private ArrayList<AnchorFollowedDataBean.AnchorInfoBean> mAnchorList = new ArrayList<>();
    private int mCurrentPager = 1;
    private BaseListAdapter adapter;
    private long userId;
    private BaseListAdapter recommendAdapter;
    private ArrayList<RecommendAnchorInfoBean> mRecommendAnchorInfoList = new ArrayList<>();

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.comm_white), 0);
    }


    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_follow, "我的订阅", "开播提醒", true);
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
    }

    @Override
    protected void setupView() {
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
//        getAnchorList(mCurrentPager++);
        initRecommendRecycler();
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        startActivity(new Intent(FollowActivity.this, PlayNotifyActivity.class));
    }

    @Override
    protected void loadData() {
        refreshLayout.autoRefresh();
    }


    private void initRecommendRecycler() {
        recommendRecycler.setNestedScrollingEnabled(false);
        recommendRecycler.setFocusableInTouchMode(false);
        recommendRecycler.setHasFixedSize(true);
        recommendRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        recommendRecycler.addItemDecoration(new SpacesItemDecoration(5));
        recommendAdapter = new BaseListAdapter() {

            @Override
            protected int getDataCount() {
                return mRecommendAnchorInfoList == null ? 0 : mRecommendAnchorInfoList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(FollowActivity.this).inflate(R.layout.item_anchor_home, null);
                return new AnchorInfoViewHolder(itemView);
            }
        };
        recommendRecycler.setAdapter(recommendAdapter);

        View view = LayoutInflater.from(this).inflate(R.layout.head_follow, refreshLayout, false);
        recommendAdapter.addHeaderView(view);
        initHead(view);
    }

    private void initHead(View view) {
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
            tvWatchCount.setText(recommendAnchorInfoBean.getRoomUserCount() + "");
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(FollowActivity.this, 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(FollowActivity.this).load(recommendAnchorInfoBean.getCover()).apply(requestOptions).into(ivCover);
//            GlideImageLoader.getInstace().loadRoundImage(FollowActivity.this, recommendAnchorInfoBean.getCover(), ivCover, 5);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            Intent intent = new Intent(FollowActivity.this, LiveDisplayActivity.class);
            RecommendAnchorInfoBean recommendAnchorInfoBean = mRecommendAnchorInfoList.get(recommendAdapter.getRealPosition(this));
            intent.putExtra(BundleConfig.PROGRAM_ID, recommendAnchorInfoBean.getProgramId());
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPager = 1;
        getAnchorList(mCurrentPager++);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getAnchorList(mCurrentPager++);
    }

    public void getAnchorList(int pager) {
        userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("page", pager);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ANCHOR_FOLLOWED, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        AnchorFollowedDataBean anchorFollowedDataBean = GsonUtils.GsonToBean(jsonStr, AnchorFollowedDataBean.class);
                        loadSuccess(anchorFollowedDataBean);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        ToastUtils.showToast(errorMsg);
                        mCurrentPager--;
                    }
                });
    }

    private void loadSuccess(AnchorFollowedDataBean anchorFollowedDataBean) {
        recommendRecycler.setVisibility(View.GONE);
        if (anchorFollowedDataBean != null && anchorFollowedDataBean.data != null && anchorFollowedDataBean.data.list != null) {
            if (mCurrentPager == 2) {
                mAnchorList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mAnchorList.addAll(anchorFollowedDataBean.data.list);
            if (anchorFollowedDataBean.data.list == null || anchorFollowedDataBean.data.list.size() == 0/*< NetConfig.DEFAULT_PAGER_SIZE*/) {
                adapter.notifyDataSetChanged();
                if (mAnchorList.size() > 0) {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                } else {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                    showEmptyView();
                }
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }, 300);
            } else {
                refreshLayout.setEnableLoadMore(true);
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
                adapter.notifyDataSetChanged();
            }
        } else {
            if (mAnchorList.size() > 0) {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
            } else {
                adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
            }
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setEnableLoadMore(false);
                }
            }, 300);
        }
    }

    private void showEmptyView() {
        recommendRecycler.setVisibility(View.VISIBLE);
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECOMMEND_ANCHOR, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        RecommendInfo recommendInfo = JSON.parseObject(jsonStr, RecommendInfo.class);
                        if (recommendInfo.getCode() == 200) {
                            if (recommendInfo != null && recommendInfo.getData() != null) {
                                mRecommendAnchorInfoList.clear();
                                mRecommendAnchorInfoList.addAll(recommendInfo.getData().getList());
                                recommendAdapter.notifyDataSetChanged();
                            }
                        } else {
//                            listenter.onError(recommendInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed" + errorMsg);
//                        listenter.onError(errorMsg);
                    }
                });
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(FollowActivity.this));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mAnchorList == null ? 0 : mAnchorList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(FollowActivity.this).inflate(R.layout.item_follow_me, parent, false);
                return new AnchorViewHolder(itemView);
            }

            @Override
            protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more_end, parent, false);
                TextView tvFoot = view.findViewById(R.id.tv_foot);
                tvFoot.setText("没有更多了~");
                return new LoadMoreFooterViewHolder(view);
            }
        };
        recycler.setAdapter(adapter);
    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {

        public LoadMoreFooterViewHolder(View view) {
            super(view);
        }

        @Override
        public void onBindViewHolder(int position) {
            // 强制关闭复用，刷新动画
            this.setIsRecyclable(false);
            // 设置自定义加载中和到底了效果
        }

        @Override
        public void onItemClick(View view, int position) {
            // 设置点击效果，比如加载失败，点击重试
        }
    }

    public static FollowFragment newInstance() {
        return new FollowFragment();
    }

    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_follow_state)
        TextView tvFollowState;
        @BindView(R.id.tv_last_time)
        TextView tvLastTime;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
//            GlideImageLoader.getInstace().displayImage(getContext(), anchorInfoBean.avatar, ivAvatar);
//            RequestOptions requestOptions = new RequestOptions().transform(new GlideRoundTransform(5));
//            Glide.with(FollowActivity.this).load(anchorInfoBean.avatar).apply(requestOptions).into(ivAvatar);
            GlideImageLoader.getInstace().loadRoundImage(FollowActivity.this, anchorInfoBean.avatar, ivAvatar, 5);
//            tvStatus.setVisibility("T".equals(anchorInfoBean.status) ? View.VISIBLE : View.GONE);
            if ("T".equals(anchorInfoBean.status)) {
                tvStatus.setVisibility(View.VISIBLE);
                tvLastTime.setVisibility(View.GONE);
            } else {
                tvStatus.setVisibility(View.GONE);
                tvLastTime.setVisibility(View.VISIBLE);
                tvLastTime.setText("上次直播:");
                if (!TextUtils.isEmpty(anchorInfoBean.lastShowBeginTime)) {
                    String timeRange = DateUtils.getTimeRange(anchorInfoBean.lastShowBeginTime);
                    tvLastTime.append(timeRange);
                } else {
                    tvLastTime.append("无");
                }
            }
            tvAnchorName.setText(anchorInfoBean.anchorNickname);
            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(anchorInfoBean.anchorLevelValue));
            setFollowState(tvFollowState, anchorInfoBean);
        }


        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            Intent intent = new Intent(FollowActivity.this, LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, anchorInfoBean.programId);
            startActivity(intent);
        }

    }

    private void setFollowState(TextView tvFollowState, AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean) {
        tvFollowState.setBackground(null);
        tvFollowState.setTextColor(Color.parseColor("#20000000"));
        tvFollowState.setText("已订阅");
        tvFollowState.setOnClickListener(v -> {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//            dialog.setTitle("提示");
//            dialog.setMessage("是否确定取消关注该主播");
//            dialog.setNegativeButton("取消", null);
//            dialog.setPositiveButton("确定", (dialog1, which) -> {
            HashMap map = new HashMap();
            map.put("userId", userId);
            map.put("programId", anchorInfoBean.programId);
            unFollow(map, tvFollowState, anchorInfoBean);
//            });
//            dialog.show();
        });
    }

    private void unFollow(HashMap paramsMap, TextView tvFollowState, AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.UNFOLLOW_ANCHOR, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
//                            mCurrentPager = 1;
//                            getAnchorList(mCurrentPager++);
                            ToastUtils.showToast("取消订阅成功");
                            setUnFollowState(tvFollowState, anchorInfoBean);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        ToastUtils.showToast(errorMsg);
                    }
                });
    }

    private void setUnFollowState(TextView tvFollowState, AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean) {
        tvFollowState.setBackgroundResource(R.drawable.bg_state_no_follow_follow);
        tvFollowState.setTextColor(Color.parseColor("#70000000"));
        tvFollowState.setText("订阅");
        tvFollowState.setOnClickListener(v -> follow(tvFollowState, anchorInfoBean));
    }

    private void follow(TextView tvFollowState, AnchorFollowedDataBean.AnchorInfoBean infoBean) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", infoBean.programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FELLOW_HOST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    ToastUtils.showToast("订阅成功");
                    setFollowState(tvFollowState, infoBean);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }


}

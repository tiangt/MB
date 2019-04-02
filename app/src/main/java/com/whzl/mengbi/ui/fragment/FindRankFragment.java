package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.model.entity.RankListInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现--榜单
 *
 * @author cliang
 * @date 2019.2.21
 */
public class FindRankFragment extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_find_rank)
    RecyclerView recycler;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private int TOP_RANK = 0;
    private int OTHER_RANK = 1;
    private BaseListAdapter mAdapter;
    private List<RankListInfo.DataBean.ListBean> mListData = new ArrayList<>();
    private String rankName;
    private String rankType;
    private String preCycle;

    public static FindRankFragment newInstance(String rankName, String rankType, String preCycle) {
        Bundle args = new Bundle();
        args.putString("rankName", rankName);
        args.putString("rankType", rankType);
        args.putString("preCycle", preCycle);
        FindRankFragment rankListFragment = new FindRankFragment();
        rankListFragment.setArguments(args);
        return rankListFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find_rank;
    }

    @Override
    public void init() {
        Bundle args = getArguments();
        rankName = args.getString("rankName");
        rankType = args.getString("rankType");
        preCycle = args.getString("preCycle");
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    private void initRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getMyActivity()));
        mAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mListData == null ? 0 : mListData.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                if (viewType == TOP_RANK) {
                    View topView = LayoutInflater.from(getActivity()).inflate(R.layout.item_rank_top, parent, false);
                    return new TopViewHolder(topView);
                } else {
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_rank_list, parent, false);
                    return new ViewHolder(itemView);
                }
            }

            @Override
            protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
                if (mListData.size() > 1 && mListData.size() < 20) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_last, parent, false);
                    TextView tvFoot = view.findViewById(R.id.tv_last);
                    tvFoot.setText(mListData.size() + 1 + "");
                    return new LoadMoreFooterViewHolder(view);
                }else{
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
                    return new LoadMoreFooterViewHolder(view);
                }
            }

            @Override
            protected int getDataViewType(int position) {
                if (position < 1) {
                    return TOP_RANK;
                } else {
                    return OTHER_RANK;
                }
            }
        };
        recycler.setAdapter(mAdapter);
        mAdapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
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

    private void getData() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("rankName", rankName);
        paramsMap.put("rankType", rankType);
        paramsMap.put("preCycle", preCycle);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FIND_RANK, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                if (getContext() == null) {
                    return;
                }
                RankListInfo rankList = GsonUtils.GsonToBean(result.toString(), RankListInfo.class);
                if (rankList.code == 200) {
                    if (rankList.data != null && rankList.data.list != null) {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                        if (rankList.data.list.size() == 0) {
                            rlEmpty.setVisibility(View.VISIBLE);
                        } else {
                            rlEmpty.setVisibility(View.GONE);
                            mListData.clear();
                            mListData.addAll(rankList.data.list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
            }
        });
    }

    class TopViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_top_avatar)
        CircleImageView ivTopAvatar;
        @BindView(R.id.imageView2)
        ImageView imageView;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.iv_live_state)
        ImageView ivLiveState;
        @BindView(R.id.tv_rank_info)
        TextView tvRankInfo;

        public TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if ("CELEBRITY".equals(rankName)) {
                tvRankInfo.setText(getString(R.string.rank_star));
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.bg_star_no1, imageView);
            } else if ("RICH".equals(rankName)) {
                tvRankInfo.setText(getString(R.string.rank_regal));
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.bg_regal_no1, imageView);
            } else if ("POPULAR".equals(rankName)) {
                tvRankInfo.setText(getString(R.string.rank_pop));
                GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.bg_pop_no1, imageView);
            }

            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListData.get(0).user.avatar, ivTopAvatar);
            tvNickName.setText(mListData.get(0).user.nickname);
            if (mListData.get(0).user != null) {
                tvNickName.setText(mListData.get(0).user.nickname);
                if ("ANCHOR_LEVEL".equals(mListData.get(0).user.levelType)) {
                    ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(mListData.get(0).user.level));
                } else {
                    ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(mListData.get(0).user.level));
                }
            }

            if (mListData.get(0).program != null && "T".equals(mListData.get(0).program.status)) {
                ivLiveState.setVisibility(View.VISIBLE);
                Glide.with(getMyActivity()).asGif().load(R.drawable.gif_live).into(ivLiveState);
            } else {
                ivLiveState.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RankListInfo.DataBean.ListBean detailBean = mListData.get(position);
            if (detailBean.program != null && detailBean.program.programId > 0) {
                Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
                intent.putExtra("programId", detailBean.program.programId);
                startActivity(intent);
            }
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_rank)
        TextView tvRank;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.iv_user_level)
        ImageView ivUserLevel;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.tv_gap)
        TextView tvGap;
        @BindView(R.id.iv_user_image)
        CircleImageView ivUserImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GlideImageLoader.getInstace().displayImage(getMyActivity(), mListData.get(position).user.avatar, ivUserImage);
            tvRank.setText(position + 1 + "");
            RankListInfo.DataBean.ListBean detailBean = mListData.get(position);
            if (detailBean.program != null && "T".equals(detailBean.program.status)) {
                ivStatus.setVisibility(View.VISIBLE);
                Glide.with(getMyActivity()).asGif().load(R.drawable.gif_live).into(ivStatus);
            } else {
                ivStatus.setVisibility(View.GONE);
            }

            if (detailBean.user != null) {
                tvNickName.setText(detailBean.user.nickname);
                if ("ANCHOR_LEVEL".equals(detailBean.user.levelType)) {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(detailBean.user.level));
                } else {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(detailBean.user.level));
                }
            }

            if (detailBean.rank != null) {
                if ("CELEBRITY".equals(rankName)) {
//                    tvGap.setText(StringUtils.formatNumber(detailBean.rank.gap) + "经验");
                    tvGap.setText(LightSpanString.getLightString(StringUtils.formatNumber(detailBean.rank.gap), Color.parseColor("#ff611c")));
                    tvGap.append(LightSpanString.getLightString("经验", Color.parseColor("#757575")));
                } else if ("RICH".equals(rankName)) {
//                    tvGap.setText(StringUtils.formatNumber(detailBean.rank.gap) + "萌币");
                    tvGap.setText(LightSpanString.getLightString(StringUtils.formatNumber(detailBean.rank.gap), Color.parseColor("#ff611c")));
                    tvGap.append(LightSpanString.getLightString("萌币", Color.parseColor("#757575")));
                } else if ("POPULAR".equals(rankName)) {
//                    tvGap.setText(StringUtils.formatNumber(detailBean.rank.gap) + "个");
                    tvGap.setText(LightSpanString.getLightString(StringUtils.formatNumber(detailBean.rank.gap), Color.parseColor("#ff611c")));
                    tvGap.append(LightSpanString.getLightString("个", Color.parseColor("#757575")));

                }

            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RankListInfo.DataBean.ListBean detailBean = mListData.get(position);
            if (detailBean.program != null && detailBean.program.programId > 0) {
                Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
                intent.putExtra("programId", detailBean.program.programId);
                startActivity(intent);
            }
        }
    }
}

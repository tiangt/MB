package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.RankListBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/22
 */
public class RankListFragment extends BasePullListFragment<RankListBean.DetailBean> {

    private String rankName;
    private String rankType;
    private String preCycle;
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_TOP_3 = 1;
    private int[] rankIcons;
    private int[] itemBgs;
    private int[] statusColors;
    private int[] nickNameColors;
    private int[] statusBgs;

    public static RankListFragment newInstance(String rankName, String rankType, String preCycle) {
        Bundle args = new Bundle();
        args.putString("rankName", rankName);
        args.putString("rankType", rankType);
        args.putString("preCycle", preCycle);
        RankListFragment rankListFragment = new RankListFragment();
        rankListFragment.setArguments(args);
        return rankListFragment;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        Bundle args = getArguments();
        rankName = args.getString("rankName");
        rankType = args.getString("rankType");
        preCycle = args.getString("preCycle");
        rankIcons = new int[]{R.drawable.contribute_rank_1, R.drawable.contribute_rank_2, R.drawable.contribute_rank_3};
        itemBgs = new int[]{R.drawable.rank_champion_bg, R.drawable.rank_2_bg, R.drawable.rank_3_bg};
        statusColors = new int[]{R.color.rank_1_status_text_color, R.color.rank_2_status_text_color, R.color.rank_3_status_text_color};
        nickNameColors = new int[]{R.color.rank_1_nick_name_color, R.color.rank_2_nick_name_color, R.color.rank_3_nick_name_color};
        statusBgs = new int[]{R.drawable.shape_rank_1_status_text_bg, R.drawable.shape_rank_2_status_text_bg, R.drawable.shape_rank_3_status_text_bg};
    }

    @Override
    protected void loadData(int action,int page) {
        LogUtils.e("sssss   "+page);
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("rankName", rankName);
            paramsMap.put("rankType", rankType);
            paramsMap.put("preCycle", preCycle);
            ApiFactory.getInstance().getApi(Api.class)
                    .getRankList(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<RankListBean>(RankListFragment.this) {

                        @Override
                        public void onSuccess(RankListBean rankListBean) {
                            if (rankListBean == null) {
                                loadSuccess(null);
                                return;
                            }
                            loadSuccess(rankListBean.list);
                        }

                        @Override
                        public void onError(int code) {
                            loadFail();
                        }
                    });
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP_3) {
            View itemView = getLayoutInflater().inflate(R.layout.item_rank_top_3, parent, false);
            return new Top3ViewHolder(itemView);
        } else {
            View itemView = getLayoutInflater().inflate(R.layout.item_rank_normal, parent, false);
            return new NormalViewHolder(itemView);
        }
    }

    @Override
    protected int getViewType(int position) {
        if (position < 3) {
            return VIEW_TYPE_TOP_3;
        } else {
            return VIEW_TYPE_NORMAL;

        }
    }

    class Top3ViewHolder extends BaseViewHolder {
        @BindView(R.id.rl_container)
        RelativeLayout rlContainer;
        @BindView(R.id.iv_rank)
        ImageView ivRank;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_user_level)
        ImageView ivUserLevel;
        @BindView(R.id.tv_nick_name)
        TextView tvNickName;
        @BindView(R.id.rl_gap_container)
        RelativeLayout rlGapContainer;
        @BindView(R.id.tv_gap)
        TextView tvGap;

        public Top3ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            rlContainer.setBackgroundResource(itemBgs[position]);
            ivRank.setImageResource(rankIcons[position]);
            tvNickName.setTextColor(getResources().getColor(nickNameColors[position]));
            tvStatus.setTextColor(getResources().getColor(statusColors[position]));
            tvStatus.setBackgroundResource(statusBgs[position]);
            rlGapContainer.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            RankListBean.DetailBean detailBean = mDatas.get(position);
            tvStatus.setVisibility(detailBean.program != null && "T".equals(detailBean.program.status) ? View.VISIBLE : View.GONE);
            ivAvatar.setBorderColor(getResources().getColor(statusColors[position]));
            if (detailBean.user != null) {
                GlideImageLoader.getInstace().displayImage(getContext(), detailBean.user.avatar, ivAvatar);
                tvNickName.setText(detailBean.user.nickname);
                if ("ANCHOR_LEVEL".equals(detailBean.user.levelType)) {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(detailBean.user.level));
                } else {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(detailBean.user.level));
                }
            }
            if (detailBean.rank != null) {
                tvGap.setText(detailBean.rank.gap + "");
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RankListBean.DetailBean detailBean = mDatas.get(position);
            if (detailBean.program != null && detailBean.program.programId > 0) {
                Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
                intent.putExtra("programId", detailBean.program.programId);
                startActivity(intent);
            }
        }
    }


    class NormalViewHolder extends BaseViewHolder {
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

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvRank.setText(position + 1 + "");
            RankListBean.DetailBean detailBean = mDatas.get(position);
            ivStatus.setVisibility(detailBean.program != null && "T".equals(detailBean.program.status) ? View.VISIBLE : View.GONE);
            tvNickName.setTextColor(detailBean.program != null && "T".equals(detailBean.program.status)
                    ? Color.parseColor("#1edd8e")
                    : Color.parseColor("#404040"));
            if (detailBean.user != null) {
                tvNickName.setText(detailBean.user.nickname);
                if ("ANCHOR_LEVEL".equals(detailBean.user.levelType)) {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(detailBean.user.level));
                } else {
                    ivUserLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(detailBean.user.level));
                }
            }
            if (detailBean.rank != null) {
                tvGap.setText(detailBean.rank.gap + "");
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RankListBean.DetailBean detailBean = mDatas.get(position);
            if (detailBean.program != null && detailBean.program.programId > 0) {
                Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
                intent.putExtra("programId", detailBean.program.programId);
                startActivity(intent);
            }
        }
    }
}

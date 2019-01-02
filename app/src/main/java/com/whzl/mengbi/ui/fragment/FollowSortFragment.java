package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.contract.FollowSortContract;
import com.whzl.mengbi.model.entity.FollowSortBean;
import com.whzl.mengbi.presenter.FollowSortPresenter;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/12/18
 */
public class FollowSortFragment extends BasePullListFragment<FollowSortBean.ListBean, FollowSortPresenter> implements FollowSortContract.View {

    private String type;

    public static FollowSortFragment newInstance(String type) {
        FollowSortFragment followSortFragment = new FollowSortFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        followSortFragment.setArguments(bundle);
        return followSortFragment;
    }

    @Override
    protected boolean setShouldLoadMore() {
        return true;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresenter = new FollowSortPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void init() {
        super.init();
        type = getArguments().getString("type");
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_follow_sort, getPullView(), false);
        TextView tv = view.findViewById(R.id.tv_content);
        switch (type) {
            case "guard":
                tv.setText("您还没有守护的主播");
                break;
            case "manage":
                tv.setText("还没有主播给您设过房管");
                break;
            case "watch":
                tv.setText("最近没有观看记录");
                break;
        }
        setEmptyView(view);
        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    @Override
    protected void loadData(int action, int mPage) {
        switch (type) {
            case "guard":
                mPresenter.getGuardPrograms(mPage);
                break;
            case "manage":
                mPresenter.getManageProgram(mPage);
                break;
            case "watch":
                mPresenter.getWatchRecord(mPage);
                break;
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_follow_follow, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onGetGuardPrograms(FollowSortBean bean) {
        loadSuccess(bean.list);
    }

    @Override
    public void onGetManageProgram(FollowSortBean bean) {
        loadSuccess(bean.list);
    }

    @Override
    public void onGetWatchRecord(FollowSortBean bean) {
        loadSuccess(bean.list);
    }

    @Override
    public void onClearWatchRecord() {
        loadSuccess(null);
    }

    class ViewHolder extends BaseViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            FollowSortBean.ListBean listBean = mDatas.get(position);
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(getContext(), 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(FollowSortFragment.this).load(listBean.anchorAvatar).apply(requestOptions).into(ivAvatar);
            if ("T".equals(listBean.status)) {
                tvStatus.setVisibility(View.VISIBLE);
                tvLastTime.setVisibility(View.GONE);
            } else {
                tvStatus.setVisibility(View.GONE);
                tvLastTime.setVisibility(View.VISIBLE);
                tvLastTime.setText("上次直播:");
                if (!TextUtils.isEmpty(listBean.lastShowBeginTime)) {
                    String timeRange = DateUtils.getTimeRange(listBean.lastShowBeginTime);
                    tvLastTime.append(timeRange);
                } else {
                    tvLastTime.append("无");
                }
            }
            tvAnchorName.setText(listBean.anchorNickname);
            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(listBean.anchorLevelValue));
            if (type.equals("guard")) {
                tvFollowState.setVisibility(View.VISIBLE);
            } else {
                tvFollowState.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            FollowSortBean.ListBean listBean = mDatas.get(position);
            Intent intent = new Intent(getContext(), LiveDisplayActivity.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, listBean.programId);
            startActivity(intent);
        }
    }

    public void clickIbClear() {
        mPresenter.clearWatchRecord();
    }
}

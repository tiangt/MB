package com.whzl.mengbi.ui.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.whzl.mengbi.eventbus.event.FollowRefreshEvent;
import com.whzl.mengbi.eventbus.event.LoginSuccussEvent;
import com.whzl.mengbi.model.entity.FollowSortBean;
import com.whzl.mengbi.presenter.FollowSortPresenter;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.adapter.base.LoadMoreFootViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cliang
 * @date 2019.2.20
 */
public class WatchHistoryFragment extends BasePullListFragment<FollowSortBean.ListBean, FollowSortPresenter> implements FollowSortContract.View {

    private boolean needFresh = false;

    public static WatchHistoryFragment newInstance() {
        WatchHistoryFragment fragment = new WatchHistoryFragment();
        return fragment;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        super.init();
        setAboutAnchor(true);
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_follow_sort, getPullView(), false);
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText("最近没有观看记录");
        setEmptyView(view);
        View foot = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_load_more_end, getPullView(), false);
        TextView tvFoot = foot.findViewById(R.id.tv_foot);
        tvFoot.setText("没有更多了~");
        setFooterViewHolder(new LoadMoreFootViewHolder(foot));
    }

    @Override
    protected void loadData(int action, int mPage) {
        needFresh = false;
        mPresenter.getWatchRecord(mPage);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_follow_follow, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onGetGuardPrograms(FollowSortBean bean) {

    }

    @Override
    public void onGetManageProgram(FollowSortBean bean) {

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
            Glide.with(getMyActivity()).load(listBean.anchorAvatar).apply(requestOptions).into(ivAvatar);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccussEvent event) {
//        getRefreshLayout().autoRefresh(2000);
        needFresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FollowRefreshEvent event) {
        if (hasLoadData && needFresh) {
            getRefreshLayout().autoRefresh();
        }
    }
}

package com.whzl.mengbi.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorFollowedDataBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
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
public class FollowActivity extends BaseActivityNew implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private BaseListAdapter adapter;
    private ArrayList<AnchorFollowedDataBean.AnchorInfoBean> mAnchorList = new ArrayList<>();
    private int mCurrentPager = 1;
    private long mUserId;

    @Override
    protected void initEnv() {
        super.initEnv();
        mUserId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_follow, R.string.follow_title, true);
    }

    @Override
    protected void setupView() {
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void loadData() {
        getAnchorList(mCurrentPager++);
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mAnchorList == null ? 0 : mAnchorList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(FollowActivity.this).inflate(R.layout.item_anchor_followed, parent, false);
                return new AnchorViewHolder(itemView);
            }
        };
        recycler.setAdapter(adapter);
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

        HashMap hashMap = new HashMap();
        hashMap.put("userId", mUserId);
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
                        ToastUtils.showToast(errorMsg);
                    }
                });
    }

    private void loadSuccess(AnchorFollowedDataBean anchorFollowedDataBean) {
        if (anchorFollowedDataBean != null && anchorFollowedDataBean.data != null && anchorFollowedDataBean.data.list != null) {
            if (mCurrentPager == 2) {
                mAnchorList.clear();
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
            mAnchorList.addAll(anchorFollowedDataBean.data.list);
            if (anchorFollowedDataBean.data.list == null || anchorFollowedDataBean.data.list.size() < NetConfig.DEFAULT_PAGER_SIZE) {
                adapter.notifyDataSetChanged();
                if (mAnchorList.size() > 0) {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_SHOW);
                }else {
                    adapter.onLoadMoreStateChanged(BaseListAdapter.LOAD_MORE_STATE_END_HIDE);
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
            }else {
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

    class AnchorViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_anchor_name)
        TextView tvAnchorName;
        @BindView(R.id.tv_room_num)
        TextView tvRoomNum;
        @BindView(R.id.iv_level_icon)
        ImageView ivLevelIcon;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public AnchorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            GlideImageLoader.getInstace().displayImage(FollowActivity.this, anchorInfoBean.avatar, ivAvatar);
            tvStatus.setVisibility("T".equals(anchorInfoBean.status) ? View.VISIBLE : View.GONE);
            tvRoomNum.setText(getString(R.string.room_num, anchorInfoBean.programId));
            tvAnchorName.setText(anchorInfoBean.anchorNickname);
            ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(anchorInfoBean.anchorLevelValue));
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            Intent intent = new Intent(FollowActivity.this, LiveDisplayActivityNew.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, anchorInfoBean.programId);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            super.onItemLongClick(view, position);
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(FollowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("是否确定取消关注该主播");
            dialog.setNegativeButton("取消", null);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap map = new HashMap();
                    map.put("userId", mUserId);
                    map.put("programId", anchorInfoBean.programId);
                    unFollow(map);
                }
            });
            dialog.show();
        }
    }

    private void unFollow(HashMap paramsMap) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.UNFOLLOW_ANCHOR, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        ResponseInfo responseInfo = GsonUtils.GsonToBean(jsonStr, ResponseInfo.class);
                        if (responseInfo.getCode() == 200) {
                            mCurrentPager = 1;
                            getAnchorList(mCurrentPager++);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        ToastUtils.showToast(errorMsg);
                    }
                });
    }
}

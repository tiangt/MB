package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.qq.tencent.JsonUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorFollowedDataBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivityNew;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.GsonUtils;
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
 */
public class FollowFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private ArrayList<AnchorFollowedDataBean.AnchorInfoBean> mAnchorList = new ArrayList<>();
    private int mCurrentPager = 1;
    private BaseListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    public void init() {
        initRecycler();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        getAnchorList(mCurrentPager++);
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
        long userId = (long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("pager", pager);
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
        if (anchorFollowedDataBean != null || anchorFollowedDataBean.data != null) {
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
            }
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setEnableLoadMore(false);
                }
            }, 300);
        }
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mAnchorList == null ? 0 : mAnchorList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anchor_followed, parent, false);
                return new AnchorViewHolder(itemView);
            }
        };
        recycler.setAdapter(adapter);
    }

    public static FollowFragment newInstance() {
        return new FollowFragment();
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
            GlideImageLoader.getInstace().displayImage(getContext(), anchorInfoBean.avatar, ivAvatar);
            tvStatus.setVisibility("T".equals(anchorInfoBean.status) ? View.VISIBLE : View.GONE);
            tvRoomNum.setText(getString(R.string.room_num, anchorInfoBean.programId));
            tvAnchorName.setText(anchorInfoBean.anchorNickname);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            AnchorFollowedDataBean.AnchorInfoBean anchorInfoBean = mAnchorList.get(position);
            Intent intent = new Intent(getContext(), LiveDisplayActivityNew.class);
            intent.putExtra(BundleConfig.PROGRAM_ID, anchorInfoBean.programId);
            startActivity(intent);
        }
    }
}

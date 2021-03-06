package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomRankBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.AudienceContributeListAdapter;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/11
 */
public class ContributeRankFragment extends BaseFragment implements OnRefreshListener {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rl_empty_user)
    RelativeLayout relativeLayout;
    private ArrayList<RoomRankBean.DataBean.ListBean> mDatas = new ArrayList<>();
    private String mType;
    private AudienceContributeListAdapter mAdapter;
    private int programId;

    public static ContributeRankFragment newInstance(String type, int programId) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("programId", programId);
        ContributeRankFragment fragment = new ContributeRankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_con_rank;
    }

    @Override
    public void init() {
        programId = getArguments().getInt("programId");
        mType = getArguments().getString("type");
        mAdapter = new AudienceContributeListAdapter(getContext(), R.layout.item_audience_contribuate, mDatas);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                RoomRankBean.DataBean.ListBean userInfoBean = mDatas.get(position);
//                AudienceInfoDialog.newInstance(userInfoBean.userId, programId)
//                        .setAnimStyle(R.style.Theme_AppCompat_Dialog)
//                        .setDimAmount(0)
//                        .setShowBottom(true)
//                        .show(getChildFragmentManager());
//                getUserInfo(userInfoBean.userId, programId, mVisitorId);
                if (ClickUtil.isFastClick()) {
                    if (getActivity() != null) {
                        ((LiveDisplayActivity) getActivity()).showAudienceInfoDialog(userInfoBean.userId, true);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        if (getContext() == null) {
            return;
        }
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        getData(programId, mType);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);
    }

    private void getData(int programId, String type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("type", type + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CONTRIBUTION_LIST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                if (getContext() == null) {
                    return;
                }
                refreshLayout.finishRefresh();
                RoomRankBean roomRankBean = GsonUtils.GsonToBean(result.toString(), RoomRankBean.class);
                if (roomRankBean.code == 200) {
//                    if ("day".equals(type)) {
//                        mAdapter.setDatas(contributeDataBean.data.day);
//                    } else {
//                        mAdapter.setDatas(contributeDataBean.data.week);
//                    }
                    if (relativeLayout != null) {
                        if (roomRankBean.data.list == null || roomRankBean.data.list.size() == 0) {
                            relativeLayout.setVisibility(View.VISIBLE);
                        } else {
                            relativeLayout.setVisibility(View.GONE);
                        }
                    }
                    mAdapter.setDatas(roomRankBean.data.list);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData(programId, mType);
    }
}

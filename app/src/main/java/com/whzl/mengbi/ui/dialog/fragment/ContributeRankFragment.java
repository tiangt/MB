package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.ContributeDataBean;
import com.whzl.mengbi.ui.adapter.AudienceContributeListAdapter;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
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
public class ContributeRankFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ArrayList<ContributeDataBean.DataBean.UserInfoBean> mDatas = new ArrayList<>();
    private String mType;
    private AudienceContributeListAdapter mAdapter;

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
        return R.layout.comm_recycler;
    }

    @Override
    public void init() {
        int programId = getArguments().getInt("programId");
        mType = getArguments().getString("type");
        mAdapter = new AudienceContributeListAdapter(getContext(), R.layout.item_audience_contribuate, mDatas);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ContributeDataBean.DataBean.UserInfoBean userInfoBean = mDatas.get(position);
                AudienceInfoDialog.newInstance(userInfoBean.userId, programId)
                        .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getChildFragmentManager());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        getData(programId, mType);
    }

    private void getData(int programId, String type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("type", type + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CONTRIBUTION_LIST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ContributeDataBean contributeDataBean = GsonUtils.GsonToBean(result.toString(), ContributeDataBean.class);
                if (contributeDataBean.code == 200) {
                    if ("day".equals(type)) {
                        mAdapter.setDatas(contributeDataBean.data.day);
                    } else {
                        mAdapter.setDatas(contributeDataBean.data.week);
                    }

                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
            }
        });
    }
}

package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.ContributeDataBean;
import com.whzl.mengbi.ui.adapter.AudienceContributeListAdapter;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.ui.fragment.BaseFragment;
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
        mAdapter = new AudienceContributeListAdapter(getContext(), R.layout.item_audience_contribuate, mDatas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        int programId = getArguments().getInt("programId");
        mType = getArguments().getString("type");
        getData(programId, mType);
    }

    private void getData(int programId, String type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("type", type + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.CONTRIBUATION_LIST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
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

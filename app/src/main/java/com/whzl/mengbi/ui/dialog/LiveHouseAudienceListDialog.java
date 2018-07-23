package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.ui.adapter.AudienceListAdapter;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.recyclerview.MultiItemTypeAdapter;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class LiveHouseAudienceListDialog extends BaseAwesomeDialog {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ArrayList<AudienceListBean.DataBean.AudienceInfoBean> mDatas = new ArrayList<>();
    private AudienceListAdapter adapter;
    private int mProgramid;

    public static BaseAwesomeDialog newInstance(int programId) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        LiveHouseAudienceListDialog fragment = new LiveHouseAudienceListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_audience_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mProgramid = getArguments().getInt("programId");
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        adapter = new AudienceListAdapter(getContext(), R.layout.item_audience, mDatas);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                AudienceListBean.DataBean.AudienceInfoBean audienceInfoBean = mDatas.get(position);
                AudienceInfoDialog.newInstance(audienceInfoBean.getUserid(), mProgramid)
                        .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                        .setDimAmount(0)
                        .setShowBottom(true)
                        .show(getFragmentManager());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler.setAdapter(adapter);
        getAudienceList();

    }

    private void getAudienceList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", mProgramid + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_ONLINE, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                AudienceListBean audienceListBean = GsonUtils.GsonToBean(result.toString(), AudienceListBean.class);
                if (audienceListBean.getCode() == 200) {
                    mDatas.clear();
                    mDatas.addAll(audienceListBean.getData().getList());
                    adapter.notifyDataSetChanged();
                    tvTitle.setText(getString(R.string.audience_count));
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

}

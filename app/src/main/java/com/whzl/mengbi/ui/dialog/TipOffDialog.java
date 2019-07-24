package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.ReportBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 举报
 *
 * @author cliang
 * @date 2018.12.6
 */
public class TipOffDialog extends BaseAwesomeDialog {

    @BindView(R.id.rv_report)
    RecyclerView recycler;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private long mUserId;
    private long mVisitorId;
    private int mProgramId;
    private BaseListAdapter adapter;
    private ReportBean.DataBean dataBean;
    private List<String> list = new ArrayList<>();

    public static TipOffDialog newInstance(long userId, long visitorId, int programId) {
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putLong("visitorId", visitorId);
        args.putInt("programId", programId);
        TipOffDialog dialog = new TipOffDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_report;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUserId = getArguments().getLong("userId", 0);
        mVisitorId = getArguments().getLong("visitorId", 0);
        mProgramId = getArguments().getInt("programId", 0);
        getReportWays();
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void initRecycler() {
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setFocusableInTouchMode(false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_report, parent, false);
                return new ReportViewHolder(itemView);
            }
        };
        recycler.setAdapter(adapter);
    }

    class ReportViewHolder extends BaseViewHolder {
        TextView tvReportWay;

        public ReportViewHolder(View itemView) {
            super(itemView);
            tvReportWay = itemView.findViewById(R.id.tv_report_way);
        }

        @Override
        public void onBindViewHolder(int position) {
            tvReportWay.setText(list.get(position));
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            setReport(list.get(position));
            adapter.notifyDataSetChanged();
        }
    }

    private void getReportWays() {
        HashMap paramsMap = new HashMap<>();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.REPORT_REASON, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ReportBean reportBean = GsonUtils.GsonToBean(result.toString(), ReportBean.class);
                if (reportBean.getCode() == 200) {
                    dataBean = reportBean.getData();
                    if (dataBean.getList() != null) {
                        for (int i = 0; i < dataBean.getList().size(); i++) {
                            list.add(dataBean.getList().get(i));
                        }
                    }
                    initRecycler();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setReport(String reason) {
        if (mUserId == 0) {
            Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId", mVisitorId);
        paramsMap.put("targetId", mUserId);
        paramsMap.put("reasonType", reason);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.REPORT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo reportBean = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (reportBean.getCode() == 200) {
                    Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), reportBean.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

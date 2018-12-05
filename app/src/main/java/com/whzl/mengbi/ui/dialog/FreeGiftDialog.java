package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.DailyTaskBean;
import com.whzl.mengbi.ui.activity.BuySuccubusActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class FreeGiftDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_buy_card)
    TextView tvBuyCard;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_get_num)
    TextView tvGetNum;

    private ArrayList<DailyTaskBean.LoginBean> list = new ArrayList();
    private Long userid;
    private BaseListAdapter baseListAdapter;
    private int loginawardId;
    private long loginawardSn;
    private int chatawardId;
    private long chatawardSn;
    private int mProgramId;
    private int mAnchorId;

    public static BaseAwesomeDialog newInstance(int mProgramId, int mAnchorId) {
        FreeGiftDialog freeGiftDialog = new FreeGiftDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("mProgramId", mProgramId);
        bundle.putInt("mAnchorId", mAnchorId);
        freeGiftDialog.setArguments(bundle);
        return freeGiftDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_free_gift;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mProgramId = getArguments().getInt("mProgramId");
        mAnchorId = getArguments().getInt("mAnchorId");
        userid = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        tvBuyCard.getPaint().setAntiAlias(true);
        tvBuyCard.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        tvBuyCard.getPaint().setFakeBoldText(true);
        tvBuyCard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initRecycler();
        initEvent();
        initData();
    }

    private void initEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("领取".equals(tvLogin.getText().toString())) {
                    finishTask(loginawardId, loginawardSn);
                }
            }
        });

        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("领取".equals(tvChat.getText().toString())) {
                    finishTask(chatawardId, chatawardSn);
                }
            }
        });
    }

    private void finishTask(int awardId, long awardSn) {
        HashMap params = new HashMap();
        params.put("userId", userid);
        params.put("awardId", awardId);
        params.put("awardSn", awardSn);

        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .dailyTaskReceive(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        list.clear();
                        initData();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void initData() {
        HashMap params = new HashMap();
        params.put("userId", userid);
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .dailyTask(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<DailyTaskBean>() {

                    @Override
                    public void onSuccess(DailyTaskBean bean) {
                        loginawardId = bean.login.awardId;
                        loginawardSn = bean.login.awardSn;
                        chatawardId = bean.chat.awardId;
                        chatawardSn = bean.chat.awardSn;
                        if ("GRANT".equals(bean.login.status)) {
                            tvLogin.setText("已领取");
                            tvLogin.setBackgroundResource(R.drawable.btn_received_free_gift);
                        } else {
                            tvLogin.setText("领取");
                            tvLogin.setBackgroundResource(R.drawable.btn_receive_free_gift);
                        }

                        if ("GRANT".equals(bean.chat.status)) {
                            tvChat.setText("已领取");
                            tvChat.setBackgroundResource(R.drawable.btn_received_free_gift);
                        } else {
                            tvChat.setText("领取");
                            tvChat.setBackgroundResource(R.drawable.btn_receive_free_gift);
                        }

                        list.addAll(bean.watch);
                        baseListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        baseListAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return list == null ? 0 : 5;
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_free_gift, parent, false);
                return new Holder(itemView);
            }
        };
        recyclerView.setAdapter(baseListAdapter);
    }

    class Holder extends BaseViewHolder {
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_num)
        TextView tvNum;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (list.size() < 1) {
                return;
            }
            DailyTaskBean.LoginBean bean = list.get(position);
            if ("GRANT".equals(bean.status)) {
                tvState.setText("已领取");
                tvState.setTextColor(Color.parseColor("#fefefe"));
                tvState.setBackgroundResource(R.drawable.btn_received_free_gift);
            } else if ("UNGRANT".equals(bean.status)) {
                tvState.setText("领取");
                tvState.setTextColor(Color.parseColor("#fefefe"));
                tvState.setBackgroundResource(R.drawable.btn_receive_free_gift);
            } else if ("UNAWARD".equals(bean.status) && position == 0) {
                tvState.setText("进行中");
                tvState.setTextColor(Color.parseColor("#ff2b3f"));
                tvState.setBackgroundResource(R.drawable.btn_going_free_gift);
            } else if ("UNAWARD".equals(bean.status) && position > 0) {
                if ("GRANT".equals(list.get(position - 1).status)) {
                    tvState.setText("进行中");
                    tvState.setTextColor(Color.parseColor("#ff2b3f"));
                    tvState.setBackgroundResource(R.drawable.btn_going_free_gift);
                    if (position == 1) {
                        tvTime.setText("5分钟");
                        tvGetNum.setText("蓝色妖姬x5");
                    } else if (position == 2) {
                        tvTime.setText("10分钟");
                        tvGetNum.setText("蓝色妖姬x10");
                    } else if (position == 3) {
                        tvTime.setText("15分钟");
                        tvGetNum.setText("蓝色妖姬x15");
                    } else if (position == 4) {
                        tvGetNum.setText("蓝色妖姬x20");
                        tvTime.setText("20分钟");
                    }
                } else {
                    tvState.setText("未完成");
                    tvState.setTextColor(Color.parseColor("#70fefefe"));
                    tvState.setBackgroundResource(R.drawable.btn_unfinished_free_gift);
                }
            } else {
                tvState.setText("未完成");
                tvState.setTextColor(Color.parseColor("#70fefefe"));
                tvState.setBackgroundResource(R.drawable.btn_unfinished_free_gift);
            }

            switch (position) {
                case 0:
                    tvNum.setText("5");
                    break;
                case 1:
                    tvNum.setText("5");
                    break;
                case 2:
                    tvNum.setText("10");
                    break;
                case 3:
                    tvNum.setText("10");
                    break;
                case 4:
                    tvNum.setText("20");
                    break;

            }

            tvState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("领取".equals(tvState.getText().toString())) {
                        finishTask(bean.awardId, bean.awardSn);
                    }
                }
            });
        }
    }

    @OnClick({R.id.tv_buy_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy_card:
                getActivity().startActivity(new Intent(getActivity(), BuySuccubusActivity.class)
                        .putExtra("mProgramId", mProgramId).putExtra("mAnchorId", mAnchorId));
                break;
        }
    }
}

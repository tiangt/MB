package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.BillAwardBean;
import com.whzl.mengbi.model.entity.BillGiftBean;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/19
 */
public class BillAwardFragment extends BasePullListFragment<BillAwardBean.ListBean, BasePresenter> {

    private TextView tvStart;
    private TextView tvEnd;
    private TimePickerView pvTime;

    private long startDate = DateUtils.dateStrToMillis(DateUtils.getStringDateYMD());
    private long endDate = DateUtils.dateStrToMillis(DateUtils.getStringDateYMD());

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldRefresh() {
        return false;
    }

    @Override
    protected boolean setShouldLoadMore() {
        return true;
    }


    @Override
    public void init() {
        super.init();
        hideDividerShawdow(null);
        getPullView().setRefBackgroud(Color.WHITE);
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_bill_pay, getPullView(), false);
        tvStart = view.findViewById(R.id.tv_start_time);
        tvEnd = view.findViewById(R.id.tv_end_time);
        LinearLayout ll = view.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        tvStart.setText(DateUtils.getStringDateYMD());
        tvEnd.setText(DateUtils.getStringDateYMD());
        tvStart.setOnClickListener(v -> showDatePick("start"));
        tvEnd.setOnClickListener(v -> showDatePick("end"));
        Button btn = view.findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(startDate, endDate);
            }
        });
        addHeadTips(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_bill, getPullView(), false);
        setEmptyView(view2);
    }

    private void search(long startDate, long endDate) {
        if (startDate > DateUtils.dateStrToMillis(DateUtils.getStringDateYMD()) || endDate > DateUtils.dateStrToMillis(DateUtils.getStringDateYMD())) {
            ToastUtils.showToast("查询日期不能大于当前日期");
            return;
        }
        if (endDate < startDate) {
            ToastUtils.showToast("开始日期不能大于结束日期");
            return;
        }
        if (endDate - startDate > 30 * 24 * 60 * 60 * 1000L) {
            ToastUtils.showToast("查询间隔不能超过30天");
            return;
        }
        mPage = 1;
        loadData(PullRecycler.ACTION_PULL_TO_REFRESH, mPage);
    }


    public static BillAwardFragment newInstance() {
        Bundle args = new Bundle();
        BillAwardFragment billPayFragment = new BillAwardFragment();
        billPayFragment.setArguments(args);
        return billPayFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("page", mPage);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        paramsMap.put("startDate", DateUtils.getDateToString(startDate, "yyyy-MM-dd"));
        paramsMap.put("endDate", DateUtils.getDateToString(endDate, "yyyy-MM-dd"));
        ApiFactory.getInstance().getApi(Api.class)
                .billAward(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BillAwardBean>(this) {

                    @Override
                    public void onSuccess(BillAwardBean bean) {
                        loadSuccess(bean.list);
                        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                            if (bean.list != null && bean.list.size() > 0) {
                                getPullView().getRecyclerView().smoothScrollToPosition(0);
                            }
                        }
                    }

                    @Override
                    public void onError(int code) {
                        getRefreshLayout().finishLoadMore(false);
                    }
                });
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_bill_pay, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_busname)
        TextView tvBusName;
        @BindView(R.id.tv_goodName)
        TextView tvGoodName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_coinnum)
        TextView tvCoin;
        @BindView(R.id.tv_mengbi)
        TextView tvMengbi;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            BillAwardBean.ListBean bean = mDatas.get(position);
            tvMengbi.setVisibility(View.GONE);
            tvBusName.setText(bean.awardName);
            tvGoodName.setText("奖励");
            tvTime.setText(bean.statusDate);
            tvCoin.setText((bean.contentDetailName));
        }
    }


    private void showDatePick(String state) {
        Calendar calendar = Calendar.getInstance();
        if ("start".equals(state)) {
            calendar.setTimeInMillis(startDate);
        } else {
            calendar.setTimeInMillis(endDate);
        }
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(System.currentTimeMillis());
        start.setTimeInMillis(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L);
        pvTime = new TimePickerBuilder(getMyActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if ("start".equals(state)) {
                    tvStart.setText(DateUtils.getTime(date));
                    startDate = DateUtils.dateStrToMillis(DateUtils.getTime(date));
                } else {
                    tvEnd.setText(DateUtils.getTime(date));
                    endDate = DateUtils.dateStrToMillis(DateUtils.getTime(date));
                }
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(13)
                .setSubmitText("完成")//确认按钮文字
                .setTitleSize(15)//标题文字大小
                .setTitleText("日期选择")//标题文字
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setTitleBgColor(Color.WHITE)
                .setLineSpacingMultiplier(2)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("", "", "", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();

    }
}

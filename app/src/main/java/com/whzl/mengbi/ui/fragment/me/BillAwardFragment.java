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
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author nobody
 * @date 2018/10/19
 */
public class BillAwardFragment extends BasePullListFragment {

    private TextView tvStart;
    private TextView tvEnd;
    private TimePickerView pvTime;

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldRefresh() {
        return false;
    }


    @Override
    public void init() {
        super.init();
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

            }
        });
        addHeadTips(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_bill, getPullView(), false);
        setEmptyView(view2);
    }

    public static BillAwardFragment newInstance() {
        Bundle args = new Bundle();
        BillAwardFragment billPayFragment = new BillAwardFragment();
        billPayFragment.setArguments(args);
        return billPayFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        List list = new ArrayList();
        list.add("s");
        list.add("s");
        list.add("s");
        list.add("s");
        list.add("s");
        list.add("s");
        list.add("s");
        loadSuccess(list);
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_bill_pay, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
        }
    }


    private void showDatePick(String state) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateUtils.dateStrToMillis(DateUtils.getStringDateYMD()));
        pvTime = new TimePickerBuilder(getMyActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if ("start".equals(state)) {
                    tvStart.setText(DateUtils.getTime(date));
                } else {
                    tvEnd.setText(DateUtils.getTime(date));
                }
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(15)
                .setSubmitText("完成")//确认按钮文字
                .setTitleSize(17)//标题文字大小
                .setTitleText("日期选择")//标题文字
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#007aff"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#007aff"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(19)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("", "", "", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();

    }
}

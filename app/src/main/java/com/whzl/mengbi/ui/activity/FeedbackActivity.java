package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_time_feed_back)
    TextView tvTimeFeedBack;

    private TimePickerView pvTime;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_feed_back_layout, "意见反馈", "提交", true);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
        tvTimeFeedBack.setText(DateUtils.getStringDate( ));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        KeyBoardUtil.hideInputMethod(this);
        String content = etFeedback.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请输入反馈内容");
            return;
        }

        showLoading("请稍后...");
//        feedback(content, contact, email);
    }

    private void feedback(String content, String qq, String email) {
        long userId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("content", content);
        paramsMap.put("qq", qq);
        paramsMap.put("contact", email);
        ApiFactory.getInstance().getApi(Api.class)
                .feedback(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        showToast("反馈成功");
                        dismissLoading();
                        finish();
                    }

                    @Override
                    public void onError(int code) {
                        dismissLoading();
                    }
                });
    }


    @OnClick({R.id.rl_time_feed_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_time_feed_back:
                showDatePick();
                break;
        }
    }

    private void showDatePick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeFeedBack.setText(DateUtils.getTime2(date));
            }
        })

                .setDate(calendar)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(13)
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(15)//标题文字大小
                .setTitleText("出生日期")//标题文字
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.parseColor("#70000000"))//标题文字颜色
                .setSubmitColor(Color.parseColor("#ff2b3f"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#70000000"))//取消按钮文字颜色
                .setDividerColor(Color.parseColor("#cdcdcd"))
                .setContentTextSize(15)
                .setTextColorCenter(Color.BLACK)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();

    }
}

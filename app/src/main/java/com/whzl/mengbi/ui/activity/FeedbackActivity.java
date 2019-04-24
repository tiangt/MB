package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

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
    @BindView(R.id.et_email)
    EditText etEmail;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_feed_back_layout, "意见反馈", "提交", true);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.user_settings_feed_back_submit_but)
    public void onClick() {
        KeyBoardUtil.hideInputMethod(this);
        String content = etFeedback.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请输入反馈内容");
            return;
        }

        showLoading("请稍后...");
        feedback(content, contact, email);
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
}

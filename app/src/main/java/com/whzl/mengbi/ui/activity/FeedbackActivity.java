package com.whzl.mengbi.ui.activity;

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
    @BindView(R.id.et_qq)
    EditText etQq;
    @BindView(R.id.et_email)
    EditText etEmail;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_feed_back_layout, R.string.feedback, true);
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.user_settings_feed_back_submit_but)
    public void onClick() {
        KeyBoardUtil.hideInputMethod(this);
        String content = etFeedback.getText().toString().trim();
        String qq = etQq.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请输入反馈内容");
            return;
        }
        if (TextUtils.isEmpty(qq)) {
            showToast("请输入联系qq");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }
        showLoading("请稍后...");
        feedback(content, qq, email);
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

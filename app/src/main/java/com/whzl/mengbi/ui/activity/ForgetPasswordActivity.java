package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 *
 * @author cliang
 * @date 2018.10.19
 */
public class ForgetPasswordActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_phone_msg)
    TextView tvPhoneMsg;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ib_clean_forget_psw)
    ImageButton ibClear;
    private String phone;

    @Override
    protected void initEnv() {
        super.initEnv();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_forget_psw, R.string.forget_psw, true);
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(this);
//        btnNext.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                getVerifyCode(phone);
//            }
//        });
        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone != null) {
                    etPhone.setText("");
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_next:
                if (ClickUtil.isFastClick()) {
                    getVerifyCode(phone);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvPhoneMsg.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        phone = etPhone.getText().toString().trim();
        boolean isPhone = StringUtils.isPhone(phone);
        if (isPhone) {
            btnNext.setEnabled(true);
        } else {
            if (phone.length() == 11) {
                tvPhoneMsg.setVisibility(View.VISIBLE);
                tvPhoneMsg.setText("请输入正确的手机号");
            }
            btnNext.setEnabled(false);
        }

        if (phone.length() > 0) {
            ibClear.setVisibility(View.VISIBLE);
        } else {
            ibClear.setVisibility(View.GONE);
        }
    }

    /**
     * 验证手机号是否存在
     * <p>
     * 验证手机
     */
    private void getVerifyCode(String mobile) {
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("identifyCode", mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.IS_PHONE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if (code.equals("200")) {
                            Intent intent = new Intent(getBaseContext(), ResetPasswordActivity.class);
                            intent.putExtra("mobile", phone);
                            startActivity(intent);
                        } else if (code.equals("-1231")) {
                            tvPhoneMsg.setVisibility(View.VISIBLE);
                            tvPhoneMsg.setText("该手机号不存在");
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityFinishEvent event) {
        finish();
    }
}

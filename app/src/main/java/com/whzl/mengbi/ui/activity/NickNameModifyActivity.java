package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
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
 */
public class NickNameModifyActivity extends BaseActivity {


    @BindView(R.id.et_nick_name)
    EditText etNickName;
    private String nickname;

    @Override
    protected void initEnv() {
        super.initEnv();
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_nick_name_modify, R.string.nick_name, R.string.save, true);
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        String nickname = etNickName.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showToast("昵称不能为空");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("userId", SPUtils.get(this, SpConfig.KEY_USER_ID, 0L));
        hashMap.put("nickname", nickname);
        ApiFactory.getInstance().getApi(Api.class)
                .nickName(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {

                    }

                    @Override
                    public void onError(int code) {

                    }
                });
        Intent intent = new Intent();
        intent.putExtra("nickname", nickname);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void setupView() {
        etNickName.setText(nickname);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.btn_clear)
    public void onClick() {
        etNickName.getText().clear();
    }
}

package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.SystemConfigBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

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
    @BindView(R.id.tv_limit_nick_name_modify)
    TextView tvLimit;
    @BindView(R.id.btn_clear)
    ImageButton btnClear;
    private String nickname;

    @Override
    protected void initEnv() {
        super.initEnv();
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_nick_name_modify, R.string.nick_name, R.string.save, true);
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));

    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        KeyBoardUtil.closeKeybord(etNickName, this);

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
                        Intent intent = new Intent();
                        intent.putExtra("nickname", nickname);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                        if (body.code == -1209) {
                            ToastUtils.snack(etNickName, "昵称已存在");
                        } else if (body.code == -1211) {
                            ToastUtils.snackLong(etNickName, "余额不足", "去充值", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(NickNameModifyActivity.this, WXPayEntryActivity.class));
                                }
                            });
                        } else {
                            ToastUtils.snack(etNickName, "保存失败");
                        }
                    }
                });
    }

    @Override
    protected void setupView() {
        etNickName.setText(nickname);
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    btnClear.setVisibility(View.GONE);
                } else {
                    btnClear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        HashMap map = new HashMap();
        map.put("key", "edit_nickname_money");
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        ApiFactory.getInstance().getApi(Api.class)
                .systemConfig(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SystemConfigBean>() {


                    @Override
                    public void onSuccess(SystemConfigBean jsonElement) {
                        if (jsonElement == null || jsonElement.list == null) {
                            return;
                        }
                        tvLimit.setText(LightSpanString.getLightString(jsonElement.list.get(0).paramValue, Color.parseColor("#70ff2b3f")));
                        tvLimit.append(jsonElement.list.get(0).unitType + " / 次，首次免费。昵称长度限制");
                        tvLimit.append(LightSpanString.getLightString("10", Color.parseColor("#70ff2b3f")));
                        tvLimit.append("个汉字。");
                    }

                    @Override
                    public void onError(ApiResult<SystemConfigBean> body) {

                    }
                });
    }

    @OnClick(R.id.btn_clear)
    public void onClick() {
        etNickName.getText().clear();
    }
}

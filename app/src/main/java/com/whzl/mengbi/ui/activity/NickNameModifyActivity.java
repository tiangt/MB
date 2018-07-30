package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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

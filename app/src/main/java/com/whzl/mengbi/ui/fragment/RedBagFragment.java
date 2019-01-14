package com.whzl.mengbi.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author nobody
 * @date 2019/1/14
 */
public class RedBagFragment extends BaseFragment {
    public static final String NORMAL = "normal";
    public static final String LUCK = "luck";
    public static final String FUND = "fund";
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_red_bag)
    LinearLayout llRedBag;
    Unbinder unbinder;
    private String type;

    public static Fragment newInstance(String type) {
        RedBagFragment redBagFragment = new RedBagFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        redBagFragment.setArguments(bundle);
        return redBagFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_red_bag;
    }

    @Override
    public void init() {
        type = getArguments().getString("type");
        switch (type) {
            case NORMAL:
                tvMoney.setText("单个金额");
                break;
            case LUCK:
                tvMoney.setText("总金额");
                break;
            case FUND:
                llEmpty.setVisibility(View.VISIBLE);
                llRedBag.setVisibility(View.GONE);
                break;
        }
    }


    @OnClick({R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                KeyBoardUtil.closeKeybord(etNumber,getContext());
                if (type.equals(NORMAL)) {
                    if (checkNormal()) {
                        ToastUtils.showToast("send" + Integer.parseInt(etMoney.getText().toString()));
                    }
                } else if (type.equals(LUCK)) {
                    if (checkLuck()) {
                        ToastUtils.showToast("send" + Integer.parseInt(etMoney.getText().toString()));
                    }
                }
                break;
        }
    }

    private boolean checkLuck() {
        if (TextUtils.isEmpty(etMoney.getText()) || Integer.parseInt(etMoney.getText().toString()) == 0
                || Integer.parseInt(etMoney.getText().toString()) % 10000 != 0) {
            ToastUtils.snack(etMoney, "请输入10000的倍数");
            return false;
        }
        if (TextUtils.isEmpty(etNumber.getText()) || Integer.parseInt(etNumber.getText().toString()) < 5) {
            ToastUtils.snack(etMoney, "红包个数不得小于5个");
            return false;
        } else
            return true;
    }

    private boolean checkNormal() {
        if (TextUtils.isEmpty(etMoney.getText()) || Integer.parseInt(etMoney.getText().toString()) == 0
                || Integer.parseInt(etMoney.getText().toString()) % 2000 != 0) {
            ToastUtils.snack(etMoney, "请输入2000的倍数");
            return false;
        }
        if (TextUtils.isEmpty(etNumber.getText()) || Integer.parseInt(etNumber.getText().toString()) < 5) {
            ToastUtils.snack(etMoney, "红包个数不得小于5个");
            return false;
        } else
            return true;
    }
}

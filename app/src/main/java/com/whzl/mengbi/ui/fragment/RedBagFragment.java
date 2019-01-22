package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.ui.activity.RedbagActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                etMoney.setHint("请输入2000的倍数");
                break;
            case LUCK:
                tvMoney.setText("总金额");
                etMoney.setHint("请输入10000的倍数");
                break;
            case FUND:
                llEmpty.setVisibility(View.VISIBLE);
                llRedBag.setVisibility(View.GONE);
                break;
        }
        initMoney();
    }

    private void initMoney() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotalText();
            }
        });
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotalText();
            }
        });

    }

    private void setTotalText() {
        if (TextUtils.isEmpty(etMoney.getText()) || TextUtils.isEmpty(etNumber.getText())) {
            tvTotal.setText("0");
            return;
        }
        long money = Long.parseLong(etMoney.getText().toString());
        long num = Long.parseLong(etNumber.getText().toString());
        if (type.equals(NORMAL)) {
            tvTotal.setText(AmountConversionUitls.amountConversionFormat(money * num));
        } else {
            tvTotal.setText(AmountConversionUitls.amountConversionFormat(money));
        }
    }


    @OnClick({R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                KeyBoardUtil.closeKeybord(etNumber, getContext());
                if (type.equals(NORMAL)) {
                    if (checkNormal()) {
                        sendRedPack("NORMAL");
                    }
                } else if (type.equals(LUCK)) {
                    if (checkLuck()) {
                        sendRedPack("RANDOM");
                    }
                }
                break;
        }
    }

    private void sendRedPack(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("amount", Long.parseLong(tvTotal.getText().toString().replaceAll(",", "")));
        hashMap.put("contentType", "COIN");
        hashMap.put("objectType", "USER");
        hashMap.put("programId", ((RedbagActivity) getActivity()).programId);
        hashMap.put("quantity", Integer.parseInt(etNumber.getText().toString()));
        hashMap.put("redPacketType", s);
        ApiFactory.getInstance().getApi(Api.class)
                .sendRedPacket(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        ToastUtils.showToastUnify(getActivity(), "发送成功");
                        getActivity().finish();
                    }

                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                        switch (body.code) {
                            case -1211:
                                ToastUtils.snackLong(btnSend, "您的萌币余额不足", "充值", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().startActivity(new Intent(getActivity(), WXPayEntryActivity.class));
                                    }
                                });
                                break;
                            case -1265:
                                ToastUtils.showToastUnify(getActivity(), getString(R.string.red_pack_full));
                                break;
                            case -1135:
                                ToastUtils.showToastUnify(getActivity(), getString(R.string.red_pack_unplay));
                                break;
                        }
                    }
                });
    }


    private boolean checkLuck() {
        if (TextUtils.isEmpty(etMoney.getText()) || Long.parseLong(etMoney.getText().toString()) == 0
                || Long.parseLong(etMoney.getText().toString()) % 10000 != 0) {
            ToastUtils.showToastUnify(getActivity(), "请输入10000的倍数");
            return false;
        }
        if (TextUtils.isEmpty(etNumber.getText()) || Long.parseLong(etNumber.getText().toString()) < 5) {
            ToastUtils.showToastUnify(getActivity(), "红包个数不得小于5个");
            return false;
        }
        if (Long.parseLong(etNumber.getText().toString()) > 50) {
            ToastUtils.showToastUnify(getActivity(), "红包个数不得大于50个");
            return false;
        } else
            return true;
    }

    private boolean checkNormal() {
        if (TextUtils.isEmpty(etMoney.getText()) || Long.parseLong(etMoney.getText().toString()) == 0
                || Long.parseLong(etMoney.getText().toString()) % 2000 != 0) {
            ToastUtils.showToastUnify(getActivity(), "请输入2000的倍数");
            return false;
        }
        if (TextUtils.isEmpty(etNumber.getText()) || Long.parseLong(etNumber.getText().toString()) < 5) {
            ToastUtils.showToastUnify(getActivity(), "红包个数不得小于5个");
            return false;
        }
        if (Long.parseLong(etNumber.getText().toString()) > 50) {
            ToastUtils.showToastUnify(getActivity(), "红包个数不得大于50个");
            return false;
        } else
            return true;
    }
}

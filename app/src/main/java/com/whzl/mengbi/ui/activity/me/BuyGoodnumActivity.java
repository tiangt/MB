package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AnchorInfo;
import com.whzl.mengbi.model.entity.GoodNumBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.widget.view.AlignTextView;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/20
 */
public class BuyGoodnumActivity extends BaseActivity {
    @BindView(R.id.atv_nick)
    AlignTextView atvNick;
    @BindView(R.id.atv_time)
    AlignTextView atvTime;
    @BindView(R.id.atv_recommend)
    AlignTextView atvRecommend;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.btn_buy_goodnum)
    Button btnBuyGoodnum;
    @BindView(R.id.tv_name_num)
    TextView tvNameNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.et_send_id)
    EditText etSendId;
    @BindView(R.id.iv_id_success)
    ImageView ivIdSuccess;
    @BindView(R.id.tv_id_fail)
    TextView tvIdFail;
    @BindView(R.id.et_anchor)
    EditText etAnchor;
    @BindView(R.id.iv_success)
    ImageView ivSuccess;
    @BindView(R.id.tv_fail)
    TextView tvFail;
    @BindView(R.id.tv_anchor_nick)
    TextView tvAnchorNick;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private String type;
    private GoodNumBean.DigitsBean bean;

    private String anchorId;
    private String programId;

    @Override
    protected void initEnv() {
        super.initEnv();
        type = getIntent().getStringExtra("type");
        bean = getIntent().getParcelableExtra("bean");
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_buy_goodnum, "send".equals(type) ? "赠送靓号" : "购买靓号", true);
    }

    @Override
    protected void setupView() {
        tvPrice.setText(String.valueOf(bean.rent));
        tvNameNum.setText(bean.goodsName);
        if ("send".equals(type)) {
            llSend.setVisibility(View.VISIBLE);
            btnBuyGoodnum.setText("赠送");
            btnBuyGoodnum.setBackgroundResource(R.drawable.shape_btn_orange);

        } else {
            tvNick.setText(SPUtils.get(this, SpConfig.KEY_USER_NAME, "").toString());
        }

        initEvent();
    }

    private void initEvent() {
        initEditSendID();
        initEditAnchor();
    }

    private void initEditAnchor() {
        etAnchor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(etAnchor.getText().toString())) {
                    return false;
                }

                BusinessUtils.getAnchorInfo(this, etAnchor.getText().toString().trim(), new BusinessUtils.AnchorInfoListener() {
                    @Override
                    public void onSuccess(AnchorInfo bean) {
                        ivSuccess.setVisibility(View.VISIBLE);
                        tvAnchorNick.setText(bean.anchor.name);
                        anchorId = String.valueOf(bean.anchor.id);
                        programId = String.valueOf(bean.programId);
                    }

                    @Override
                    public void onError(int code) {
                        tvFail.setVisibility(View.VISIBLE);
                    }
                });
            }
            return false;
        });

        etAnchor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivSuccess.setVisibility(View.GONE);
                tvFail.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(s.toString())) {
                    tvTips.setVisibility(View.GONE);
                } else {
                    tvTips.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initEditSendID() {
        etSendId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(etSendId.getText().toString())) {
                    return false;
                }
                BusinessUtils.getUserInfo(this, etSendId.getText().toString().trim(), new BusinessUtils.UserInfoListener() {
                    @Override
                    public void onSuccess(UserInfo.DataBean bean) {
                        ivIdSuccess.setVisibility(View.VISIBLE);
                        tvNick.setText(bean.getNickname());
                    }

                    @Override
                    public void onError(int code) {
                        tvIdFail.setVisibility(View.VISIBLE);
                    }
                });
            }
            return false;
        });

        etSendId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivIdSuccess.setVisibility(View.GONE);
                tvIdFail.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_buy_goodnum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_goodnum:
                if ("send".equals(type)) {
                    if (TextUtils.isEmpty(etSendId.getText().toString())) {
                        ToastUtils.showToast("请填写赠送ID");
                        return;
                    } else {
                        buyorsend(anchorId, programId);
                    }
                } else {
                    buyorsend("", "");
                }
                break;
        }
    }

    private void buyorsend(String salerId, String programId) {
        BusinessUtils.mallBuy(this, String.valueOf(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L))
                , bean.goodsId + "", bean.priceId + "", "1", etSendId.getText().toString().trim(), salerId, programId
                , new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToast("send".equals(type) ? "赠送成功" : "购买成功");
                        Intent intent = new Intent();
                        intent.putExtra("state", true);
                        setResult(200, intent);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}

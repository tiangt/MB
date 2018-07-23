package com.whzl.mengbi.wxapi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SDKConfig;
import com.whzl.mengbi.model.entity.RechargeChannelListBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.pay.WXPayOrder;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.impl.RechargePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.view.RechargeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/19
 */
public class WXPayEntryActivity extends BaseActivityNew implements IWXAPIEventHandler, RechargeView {
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_left_amount)
    TextView tvLeftAmount;
    @BindView(R.id.rg_pay_way)
    RadioGroup rgPayWay;
    @BindView(R.id.rb_ali_pay)
    RadioButton rbAliPay;
    @BindView(R.id.rb_we_chat_pay)
    RadioButton rbWeChatPay;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    private RechargePresenter mPresent;
    private IWXAPI wxApi;
    private ArrayList<RechargeRuleListBean> aliRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> weChatRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> currentRuleList = new ArrayList<>();
    private SparseArray<Integer> channelIdMap = new SparseArray<>();
    private BaseListAdapter adapter;
    private int mRuleId = -1;
    private long mUserId;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_wx_pay_entry, R.string.personal, true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresent = new RechargePresenterImpl(this);
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long)0).toString());
        //mUserId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
        wxApi = WXAPIFactory.createWXAPI(this, SDKConfig.KEY_WEIXIN);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void setupView() {
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return currentRuleList == null ? 0 : currentRuleList.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(WXPayEntryActivity.this).inflate(R.layout.item_recharge_rule, null);
                return new RuleViewHolder(item);
            }
        };
        recycler.setAdapter(adapter);
    }

    class RuleViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.rule_container)
        LinearLayout ruleContainer;

        public RuleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            RechargeRuleListBean rechargeRuleListBean = currentRuleList.get(position);
            tvDesc.setText(AmountConversionUitls.coinConversion(rechargeRuleListBean.getChengCount()));
            tvCost.setText(getString(R.string.rmb, rechargeRuleListBean.getRechargeCount() / 100f));
            ruleContainer.setSelected(mRuleId == rechargeRuleListBean.getRuleId());
            tvDesc.setSelected(mRuleId == rechargeRuleListBean.getRuleId());
            tvCost.setSelected(mRuleId == rechargeRuleListBean.getRuleId());
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            RechargeRuleListBean rechargeRuleListBean = currentRuleList.get(position);
            mRuleId = rechargeRuleListBean.getRuleId();
            adapter.notifyDataSetChanged();
            btnRecharge.setEnabled(true);
            btnRecharge.setText(getString(R.string.recharge_confirm, rechargeRuleListBean.getRechargeCount() / 100f));
        }
    }

    @Override
    protected void loadData() {
        mPresent.getUserInfo(mUserId);
        mPresent.getChannelInfo(new HashMap());
    }

    @Override
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        GlideImageLoader.getInstace().displayImage(this, userInfo.getData().getAvatar(), ivAvatar);
        tvAccount.setText(getString(R.string.recharge_account, userInfo.getData().getNickname()));
        tvLeftAmount.setText(R.string.mengbi_left);
        SpannableString spannableString = StringUtils.spannableStringColor(userInfo.getData().getWealth().getCoin() + "", Color.parseColor("#ffa800"));
        tvLeftAmount.append(spannableString);
    }

    @Override
    public void onGetChannelInfoSuccess(RechargeInfo rechargeInfo) {
        if (rechargeInfo != null && rechargeInfo.getData() != null && rechargeInfo.getData().getChannelList() != null) {
            List<RechargeChannelListBean> channelList = rechargeInfo.getData().getChannelList();
            for (int i = 0; i < channelList.size(); i++) {
                RechargeChannelListBean rechargeChannelListBean = channelList.get(i);
                if (!NetConfig.FLAG_ACTIVE.equals(rechargeChannelListBean.getStatus())) {
                    continue;
                }
                if (NetConfig.FLAG_ALI_PAY.equals(rechargeChannelListBean.getChannelFlag())) {
                    rbAliPay.setVisibility(View.VISIBLE);
                    channelIdMap.put(R.id.rb_ali_pay, rechargeChannelListBean.getChannelId());
                    rbAliPay.setChecked(true);
                    aliRechargeRuleList.addAll(rechargeChannelListBean.getList().get(0).getRuleList());
                    currentRuleList.clear();
                    currentRuleList.addAll(aliRechargeRuleList);
                    adapter.notifyDataSetChanged();
                }
                if (NetConfig.FLAG_WECHAT_PAY.equals(rechargeChannelListBean.getChannelFlag())) {
                    rbWeChatPay.setVisibility(View.VISIBLE);
                    channelIdMap.put(R.id.rb_we_chat_pay, rechargeChannelListBean.getChannelId());
                    weChatRechargeRuleList.addAll(rechargeChannelListBean.getList().get(0).getRuleList());
                    if (!rbAliPay.isChecked()) {
                        rbWeChatPay.setChecked(true);
                        currentRuleList.clear();
                        currentRuleList.addAll(aliRechargeRuleList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            btnRecharge.setVisibility(View.VISIBLE);
        }

        rgPayWay.setOnCheckedChangeListener((group, checkedId) -> {
            mRuleId = -1;
            btnRecharge.setEnabled(false);
            btnRecharge.setText(R.string.recharge_tip);
            switch (checkedId) {
                case R.id.rb_ali_pay:
                    currentRuleList.clear();
                    currentRuleList.addAll(aliRechargeRuleList);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.rb_we_chat_pay:
                    currentRuleList.clear();
                    currentRuleList.addAll(weChatRechargeRuleList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onGetPayOrderSuccess(int orderId, String token) {
        int radioId = rgPayWay.getCheckedRadioButtonId();
        if (radioId == R.id.rb_ali_pay) {
            aliPayAuth(token);
        } else {
            WXPayOrder wxPayOrder = GsonUtils.GsonToBean(token, WXPayOrder.class);
            PayReq payReq = new PayReq();
            payReq.appId = wxPayOrder.getAppid();
            payReq.partnerId = wxPayOrder.getPartnerid();
            payReq.prepayId = wxPayOrder.getPrepayid();
            payReq.packageValue = wxPayOrder.getPackageX();
            payReq.nonceStr = wxPayOrder.getNoncestr();
            payReq.timeStamp = wxPayOrder.getTimestamp();
            payReq.sign = wxPayOrder.getSign();
            wxApi.sendReq(payReq);
        }
    }

    @OnClick(R.id.btn_recharge)
    public void onClick() {
        int channelId = channelIdMap.get(rgPayWay.getCheckedRadioButtonId());
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long)0).toString());
        //long mUserId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
        HashMap<String, Long> paramsMap = new HashMap();
        paramsMap.put("channelId", (long)channelId);
        paramsMap.put("ruleId", (long)mRuleId);
        paramsMap.put("userId", mUserId);
        paramsMap.put("toUserId", mUserId);
        paramsMap.put("proxyUserId", (long)0);
        mPresent.getOrderInfo(paramsMap);
    }

    /**
     * aliPay
     */
    private void aliPayAuth(String token) {
        Observable.just(token)
                .map(s -> {
                    PayTask paytask = new PayTask(WXPayEntryActivity.this);
                    return paytask.payV2(s, true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultMap -> {
                    int resultCode = Integer.parseInt(resultMap.get("resultStatus"));
                    switch (resultCode) {
                        case NetConfig.CODE_ALI_PAY_SUCCESS:
                            showToast(R.string.pay_success);
                            mPresent.getUserInfo(mUserId);
                            break;
                        case NetConfig.CODE_ALI_PAY_CANCEL:
                            showToast(R.string.user_cancel);
                            break;
                        default:
                            showToast(R.string.pay_fail);
                            break;
                    }
                });
    }

    //微信支付相关 -- start

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case NetConfig.CODE_WE_CHAT_PAY_SUCCESS:
                    showToast(R.string.pay_success);
                    mPresent.getUserInfo(mUserId);
                    break;
                case NetConfig.CODE_WE_CHAT_PAY_CANCEL:
                    showToast(R.string.user_cancel);
                    break;
                case NetConfig.CODE_WE_CHAT_PAY_FAIL:
                default:
                    showToast(R.string.pay_fail);
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }
    //微信支付相关 -- end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.onDestroy();
        wxApi.unregisterApp();
    }
}

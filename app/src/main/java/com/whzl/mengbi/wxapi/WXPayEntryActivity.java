package com.whzl.mengbi.wxapi;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.model.entity.RechargeChannelListBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.pay.WXPayOrder;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.impl.RechargePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.RechargeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;

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
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, RechargeView {
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
    @BindView(R.id.first_top_up)
    ConstraintLayout firstTopUp;
    @BindView(R.id.tv_rebate_ticket)
    TextView tvRebateTicket;
    @BindView(R.id.tv_rebate_money)
    TextView tvRebateMoney;
    private RechargePresenter mPresent;
    private IWXAPI wxApi;
    private ArrayList<RechargeRuleListBean> aliRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> weChatRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> currentRuleList = new ArrayList<>();
    private SparseIntArray channelIdMap = new SparseIntArray();
    private BaseListAdapter adapter;
    private int mRuleId = -1;
    private long mUserId;
    private List<RebateBean.ListBean> list = new ArrayList<>();
    private String identifyCode = "";
    private int scale = 100;
    private int chengCount;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_wx_pay_entry, R.string.personal, true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresent = new RechargePresenterImpl(this);
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        //mUserId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
        wxApi = WXAPIFactory.createWXAPI(this, SDKConfig.KEY_WEIXIN);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void setupView() {
        boolean hasTopUp = (boolean) SPUtils.get(this, SpConfig.KEY_HAS_RECHARGED, false);
        firstTopUp.setVisibility(hasTopUp ? View.GONE : View.VISIBLE);
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
            tvRebateMoney.setText("额外返利");
            chengCount = currentRuleList.get(position).getChengCount();
            tvRebateMoney.append(StringUtils.spannableStringColor(
                    String.valueOf(chengCount / 100 * scale), Color.parseColor("#ffa800")));
            tvRebateMoney.append("萌币");
        }
    }

    @Override
    protected void loadData() {
        mPresent.getUserInfo(mUserId);
        mPresent.getChannelInfo(new HashMap());
        mPresent.getCoupon(mUserId);
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
    public void onGetCoupon(RebateBean rebateBean) {
        if (rebateBean.list == null || rebateBean.list.size() == 0) {
            tvRebateTicket.setText("暂无返利券");
            tvRebateTicket.setEnabled(false);
            tvRebateMoney.setVisibility(View.GONE);
            return;
        }
        list.clear();
        tvRebateMoney.setVisibility(View.VISIBLE);
        scale = rebateBean.list.get(0).scale;
        identifyCode = rebateBean.list.get(0).identifyCode;
        tvRebateTicket.setText(rebateBean.list.get(0).goodsName + "(" + rebateBean.list.get(0).identifyCode + ")");
        list.addAll(rebateBean.list);
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
            tvRebateMoney.setText("");
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


    @OnClick({R.id.tv_rebate_ticket, R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_rebate_ticket:
                showSelect();
                break;
            case R.id.btn_recharge:
                getOrderInfo(identifyCode);
                break;
        }
    }

    private void getOrderInfo(String identifyCode) {
        int channelId = channelIdMap.get(rgPayWay.getCheckedRadioButtonId());
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        //long mUserId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
        HashMap paramsMap = new HashMap();
        paramsMap.put("channelId", (long) channelId);
        paramsMap.put("ruleId", (long) mRuleId);
        paramsMap.put("userId", mUserId);
        paramsMap.put("toUserId", mUserId);
        paramsMap.put("proxyUserId", (long) 0);
        paramsMap.put("identifyCode", identifyCode);
        mPresent.getOrderInfo(paramsMap);
    }

    private void showSelect() {
        if (list == null || list.size() == 0) {
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvRebateTicket.setText(list.get(options1).goodsName + "(" + list.get(options1).identifyCode + ")");
                identifyCode = list.get(options1).identifyCode;
                scale = list.get(options1).scale;
                tvRebateMoney.setText("额外返利");
                tvRebateMoney.append(StringUtils.spannableStringColor(
                        String.valueOf(chengCount / 100 * scale), Color.parseColor("#ffa800")));
                tvRebateMoney.append("萌币");
            }
        }).setTitleText("选择返利券").setSelectOptions(0).isRestoreItem(false).build();
        pvOptions.setPicker(list);
        pvOptions.show();
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
                            mPresent.getCoupon(mUserId);
                            EventBus.getDefault().post(new UserInfoUpdateEvent());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, true);
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
                    mPresent.getCoupon(mUserId);
                    EventBus.getDefault().post(new UserInfoUpdateEvent());
                    SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, true);
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

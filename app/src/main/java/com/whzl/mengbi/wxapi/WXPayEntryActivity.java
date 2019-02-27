package com.whzl.mengbi.wxapi;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SDKConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.model.entity.RechargeChannelListBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.pay.WXPayOrder;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.impl.RechargePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.view.RechargeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

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
    @BindView(R.id.tv_rebate_name)
    TextView tvRebateName;
    @BindView(R.id.tv_pay_for_other)
    TextView tvPayForOther;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    private RechargePresenter mPresent;
    private IWXAPI wxApi;
    private ArrayList<RechargeRuleListBean> aliRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> weChatRechargeRuleList = new ArrayList<>();
    private ArrayList<RechargeRuleListBean> currentRuleList = new ArrayList<>();
    private SparseIntArray channelIdMap = new SparseIntArray();
    private BaseListAdapter adapter;
    private int mRuleId = -1;
    private long mUserId;
    private long otherUserId;
    private ArrayList<RebateBean.ListBean> list = new ArrayList<>();
    private String identifyCode = "";
    private int scale = 0;
    private int chengCount;
    public static final int USEREBATE = 100;
    private int currentPosition = -1;
    private BaseAwesomeDialog payForDialog;
    private UserInfo.DataBean others;
    private UserInfo.DataBean myself;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_wx_pay_entry, "充值", "联系客服", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresent = new RechargePresenterImpl(this);
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        otherUserId = mUserId;
        wxApi = WXAPIFactory.createWXAPI(this, SDKConfig.KEY_WEIXIN);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(ContextCompat.getColor(this, R.color.textcolor_white_toolbar_right));
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

        rgPayWay.setOnCheckedChangeListener((group, checkedId) -> {
            mRuleId = -1;
            btnRecharge.setEnabled(true);
            btnRecharge.setText(R.string.recharge_tip);
            tvRebateMoney.setText("");
            switch (checkedId) {
                case R.id.rb_ali_pay:
                    scale = 0;
                    currentPosition = -1;
                    tvRebateTicket.setVisibility(View.VISIBLE);
                    tvRebateMoney.setText("");
                    tvRebateName.setText("");
                    currentRuleList.clear();
                    currentRuleList.addAll(aliRechargeRuleList);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.rb_we_chat_pay:
                    scale = 0;
                    currentPosition = -1;
                    tvRebateTicket.setVisibility(View.VISIBLE);
                    tvRebateMoney.setText("");
                    tvRebateName.setText("");
                    currentRuleList.clear();
                    currentRuleList.addAll(weChatRechargeRuleList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            recycler.post(new Runnable() {
                @Override
                public void run() {
                    View childAt = recycler.getChildAt(0);
                    if (childAt != null) {
                        RuleViewHolder childViewHolder = (RuleViewHolder) recycler.getChildViewHolder(childAt);
                        childViewHolder.onItemClick(childViewHolder.itemView, 0);
                    }
                }
            });

        });
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        startQQChat(NetConfig.CUSTOM_OFFICIAL_SERVICE_QQ);
    }

    private void startQQChat(String qq) {
        if (!isQQClientAvailable()) {
            showToast("QQ未安装");
            return;
        }
        final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
    }

    public boolean isQQClientAvailable() {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
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
            currentPosition = position;
            RechargeRuleListBean rechargeRuleListBean = currentRuleList.get(position);
            mRuleId = rechargeRuleListBean.getRuleId();
            adapter.notifyDataSetChanged();
            btnRecharge.setEnabled(true);
            btnRecharge.setText(getString(R.string.recharge_confirm, rechargeRuleListBean.getRechargeCount() / 100f));
            if (scale == 0) {
                tvRebateMoney.setText("");
                tvRebateName.setText("");
                return;
            }
            tvRebateMoney.setText("+");
            chengCount = currentRuleList.get(position).getChengCount();
            tvRebateMoney.append(String.valueOf(chengCount / 100 * scale));
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
        myself = userInfo.getData();
        ivAvatar.setBorderColor(Color.parseColor("#FFFEF9F1"));
        ivAvatar.setBorderWidth(UIUtil.dip2px(this, 2));
        GlideImageLoader.getInstace().displayImage(this, userInfo.getData().getAvatar(), ivAvatar);
        tvAccount.setText(userInfo.getData().getNickname());
        tvLeftAmount.setText(AmountConversionUitls.amountConversionFormat(userInfo.getData().getWealth().getCoin()));
    }

    @Override
    public void onGetCoupon(RebateBean rebateBean) {
        identifyCode = "";
        tvRebateTicket.setVisibility(View.VISIBLE);
        tvRebateName.setText("");
        tvRebateMoney.setText("");
        if (rebateBean.list == null || rebateBean.list.size() == 0) {
            tvRebateTicket.setText("无可用");
            tvRebateTicket.setTextColor(Color.parseColor("#70000000"));
            return;
        }
        list.clear();
//        scale = rebateBean.list.get(0).scale;
//        identifyCode = rebateBean.list.get(0).identifyCode;
        list.addAll(rebateBean.list);
        tvRebateTicket.setText(list.size() + "张可用");
        tvRebateTicket.setTextColor(Color.parseColor("#ff2b3f"));
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
                    aliRechargeRuleList.addAll(rechargeChannelListBean.getList().get(0).getRuleList());
                    currentRuleList.clear();
                    currentRuleList.addAll(aliRechargeRuleList);
                    adapter.notifyDataSetChanged();
//                    rbAliPay.setChecked(true);
                    rbAliPay.performClick();
                }
                if (NetConfig.FLAG_WECHAT_PAY.equals(rechargeChannelListBean.getChannelFlag())) {
                    rbWeChatPay.setVisibility(View.VISIBLE);
                    channelIdMap.put(R.id.rb_we_chat_pay, rechargeChannelListBean.getChannelId());
                    weChatRechargeRuleList.addAll(rechargeChannelListBean.getList().get(0).getRuleList());
                    if (!rbAliPay.isChecked()) {
                        currentRuleList.clear();
                        currentRuleList.addAll(aliRechargeRuleList);
                        adapter.notifyDataSetChanged();
//                        rbWeChatPay.setChecked(true);
                        rbWeChatPay.performClick();
                    }
                }
            }
            btnRecharge.setVisibility(View.VISIBLE);
        }
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


    @OnClick({R.id.ll_use_rebate, R.id.btn_recharge, R.id.tv_pay_for_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_use_rebate:
                showSelect();
                break;
            case R.id.btn_recharge:
                getOrderInfo(identifyCode);
                break;
            case R.id.tv_pay_for_other:
                if (tvPayForOther.getText().toString().equals("取消")) {
                    llMoney.setVisibility(View.VISIBLE);
                    GlideImageLoader.getInstace().displayImage(WXPayEntryActivity.this, myself.getAvatar(), ivAvatar);
                    tvAccount.setText(myself.getNickname());
                    tvPayForOther.setText("为他人充值");
                    otherUserId = mUserId;
                } else {
                    payForOther();
                }
                break;
        }
    }

    private void payForOther() {
        if (payForDialog != null && payForDialog.isAdded()) {
            return;
        }
        payForDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_pay_for_other).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                holder.setOnClickListener(R.id.iv_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                holder.setOnClickListener(R.id.btn_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findOther(holder, dialog);
                    }
                });
            }
        }).show(getSupportFragmentManager());
    }

    private void findOther(ViewHolder holder, BaseAwesomeDialog dialog) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", ((EditText) holder.getView(R.id.et_num)).getText().toString());
        ApiFactory.getInstance().getApi(Api.class)
                .getUserInfo(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<UserInfo.DataBean>() {

                    @Override
                    public void onSuccess(UserInfo.DataBean bean) {
                        dialog.dismiss();
                        holder.getView(R.id.tv_tips).setVisibility(View.INVISIBLE);
                        KeyBoardUtil.closeKeybord((EditText) holder.getView(R.id.et_num), WXPayEntryActivity.this);
                        others = bean;
                        GlideImageLoader.getInstace().displayImage(WXPayEntryActivity.this, others.getAvatar(), ivAvatar);
                        tvAccount.setText(others.getNickname() + " ");
                        for (UserInfo.DataBean.LevelListBean levelListBean : others.getLevelList()) {
                            if (NetConfig.LEVEL_TYPE_USER.equals(levelListBean.getLevelType())) {
                                tvAccount.append(LevelUtil.getImageResourceSpan(getApplicationContext(),
                                        ResourceMap.getResourceMap().getUserLevelIcon(levelListBean.getLevelValue())));
                                break;
                            }
                        }
                        llMoney.setVisibility(View.GONE);
                        tvPayForOther.setText("取消");
                        otherUserId = bean.getUserId();
                    }

                    @Override
                    public void onError(ApiResult<UserInfo.DataBean> body) {
                        if (body.code == -1210) {
                            holder.getView(R.id.tv_tips).setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void getOrderInfo(String identifyCode) {
        int channelId = channelIdMap.get(rgPayWay.getCheckedRadioButtonId());
        mUserId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        //long mUserId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
        HashMap paramsMap = new HashMap();
        paramsMap.put("channelId", (long) channelId);
        paramsMap.put("ruleId", (long) mRuleId);
        paramsMap.put("userId", mUserId);
        paramsMap.put("toUserId", otherUserId);
        paramsMap.put("proxyUserId", (long) 0);
        paramsMap.put("identifyCode", identifyCode);
        mPresent.getOrderInfo(paramsMap);
    }

    private void showSelect() {
        startActivityForResult(new Intent(this, FrgActivity.class)
                .putExtra(FrgActivity.FRAGMENT_CLASS, UseRebateFragment.class)
                .putParcelableArrayListExtra("data", list), USEREBATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == USEREBATE) {
                RebateBean.ListBean rebate = (RebateBean.ListBean) data.getParcelableExtra("rebate");
                if (rebate == null) {
                    tvRebateName.setText("");
                    tvRebateMoney.setText("");
                    scale = 0;
                    tvRebateTicket.setVisibility(View.VISIBLE);
                    identifyCode = "";
                    return;
                }
                identifyCode = rebate.identifyCode;
                tvRebateTicket.setVisibility(View.GONE);
                scale = rebate.scale;
                String[] split = rebate.goodsName.split("%");
                if (split[0] != null) {
                    tvRebateName.setText(split[0] + "%");
                }
                tvRebateMoney.setText("+");
                if (currentPosition == -1) {
                    tvRebateMoney.append("0");
                    tvRebateMoney.append("萌币");
                    return;
                }
                chengCount = currentRuleList.get(currentPosition).getChengCount();
                tvRebateMoney.append(String.valueOf(chengCount / 100 * scale));
                tvRebateMoney.append("萌币");
            }
        }
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
                            mPresent.getCoupon(mUserId);
                            showToast(R.string.user_cancel);
                            break;
                        default:
                            mPresent.getCoupon(mUserId);
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
                    mPresent.getCoupon(mUserId);
                    showToast(R.string.user_cancel);
                    break;
                case NetConfig.CODE_WE_CHAT_PAY_FAIL:
                default:
                    mPresent.getCoupon(mUserId);
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

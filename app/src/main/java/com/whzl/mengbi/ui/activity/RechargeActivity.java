package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.model.entity.RechargeChannelListBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.RechargeListBean;
import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.impl.RechargePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.handler.BaseHandler;
import com.whzl.mengbi.ui.view.RechargeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.alipay.AuthResult;
import com.whzl.mengbi.util.alipay.PayResult;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeActivity extends BaseAtivity implements RechargeView,View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private CircleImageView profileIV;
    private TextView accountNumberTV;
    private TextView coinTV;
    private RadioGroup payModeRG;
    private RecyclerView moneyRV;
    private Button confirmPayBut;

    private RecyclerView.LayoutManager layoutManager;

    private String profile;
    private int userId;
    private int coin;

    private int alipaychannelId;//支付宝充值渠道id
    private int weixinchannelId;//微信充值渠道id
    private int channelId;//充值渠道id
    private int ruleId;//充值规则
    private  static  int PROXY_USERID;//充值代理
    private String mToken;
    private PayHandler payHandler = new PayHandler(this);
    private static final int SDK_PAY_FLAG = 1;//支付宝支付业务
    private static final int SDK_AUTH_FLAG = 2;

    private RechargePresenter rechargePresenter;
    private RechargeInfo rechargeInfo;
    private CommonAdapter moneyAdapter;
    private List<RechargeRuleListBean> alipayRuleListBeans = new ArrayList<>();
    private List<RechargeRuleListBean> weixinRuleListBeans = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_layout);
        initData();
        initView();

    }

   private void initView(){
       profileIV = (CircleImageView) findViewById(R.id.recharge_user_profile);
       accountNumberTV = (TextView) findViewById(R.id.recharge_account_number);
       coinTV = (TextView) findViewById(R.id.recharge_coins_balance);
       payModeRG = (RadioGroup) findViewById(R.id.recharge_mode_RG);
       moneyRV  = (RecyclerView) findViewById(R.id.recharge_money_recyclerview);
       confirmPayBut = (Button) findViewById(R.id.recharge_confirm_pay_but);
       payModeRG.setOnCheckedChangeListener(this);
       confirmPayBut.setOnClickListener(this);
       GlideImageLoader.getInstace().circleCropImage(this,profile,profileIV);
       accountNumberTV.setText(userId+"");
       coinTV.setText(coin+"");

       layoutManager = new GridLayoutManager(this,3);
       moneyRV.setLayoutManager(layoutManager);
       moneyRV.setItemAnimator(new DefaultItemAnimator());

   }

    private void initData(){
        if(getIntent() != null){
            profile = getIntent().getStringExtra("profile");
            userId = getIntent().getIntExtra("userId",0);
            coin = getIntent().getIntExtra("coin",0);
        }

        rechargePresenter = new RechargePresenterImpl(this);
        HashMap hashMap = new HashMap();
        rechargePresenter.getChannelInfo(hashMap);
    }


    @Override
    public void showChannelInfo(RechargeInfo rechargeInfo) {
        this.rechargeInfo = rechargeInfo;
        for(RechargeChannelListBean channelListBean: rechargeInfo.getData().getChannelList()){
            if(channelListBean.getChannelFlag().equals("AlipayMobile")){
                for (RechargeListBean listBean:channelListBean.getList()){
                    alipayRuleListBeans.addAll(listBean.getRuleList());
                }
                alipaychannelId = channelListBean.getChannelId();
                channelId = alipaychannelId;
            }

            if(channelListBean.getChannelFlag().equals("WeixinApp")){
                for (RechargeListBean listBean:channelListBean.getList()){
                    weixinRuleListBeans.addAll(listBean.getRuleList());
                }
                weixinchannelId = channelListBean.getChannelId();
            }
        }

        moneyRV.setAdapter(moneyAdapter = new CommonAdapter<RechargeRuleListBean>(this,R.layout.activity_recharge_rvitem_layout,alipayRuleListBeans) {
            @Override
            protected void convert(ViewHolder holder, RechargeRuleListBean ruleListBean, int position) {
                String chengCount = AmountConversionUitls.coinConversion(ruleListBean.getChengCount());
                String rechargeCount = AmountConversionUitls.amountConversion(ruleListBean.getRechargeCount());
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(chengCount);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa800"));
                stringBuilder.setSpan(colorSpan,0,chengCount.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("\n");
                stringBuilder.append(rechargeCount);
                TextView money = holder.getView(R.id.recharge_rvitem_moeny);
                money.setText(stringBuilder);
            }
        });

        moneyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                RelativeLayout layout = (RelativeLayout)view;
                TextView moneyTextView = (TextView)layout.findViewById(R.id.recharge_rvitem_moeny);
                moneyTextView.setBackgroundColor(Color.parseColor("#ffa800"));
                String moneyContent = moneyTextView.getText().toString();
                moneyTextView.setText(LightSpanString.getLightString(moneyContent, Color.parseColor("#f9f9f9")));
                String rechargeCount = "确认支付￥"+AmountConversionUitls.amountConversion(alipayRuleListBeans.get(position).getRechargeCount());
                confirmPayBut.setText(rechargeCount);
                ruleId = alipayRuleListBeans.get(position).getRuleId();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.recharge_mode_alipay_RB:
                channelId = alipaychannelId;
                moneyRV.setAdapter(moneyAdapter = new CommonAdapter<RechargeRuleListBean>(this,R.layout.activity_recharge_rvitem_layout,alipayRuleListBeans) {
                    @Override
                    protected void convert(ViewHolder holder, RechargeRuleListBean ruleListBean, int position) {
                        String chengCount = AmountConversionUitls.coinConversion(ruleListBean.getChengCount());
                        String rechargeCount = AmountConversionUitls.amountConversion(ruleListBean.getRechargeCount());
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(chengCount);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa800"));
                        stringBuilder.setSpan(colorSpan,0,chengCount.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stringBuilder.append("\n");
                        stringBuilder.append(rechargeCount);
                        TextView moeny = holder.getView(R.id.recharge_rvitem_moeny);
                        moeny.setText(stringBuilder);
                    }
                });
                moneyAdapter.notifyDataSetChanged();
                moneyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        RelativeLayout layout = (RelativeLayout)view;
                        TextView moneyTextView = (TextView)layout.findViewById(R.id.recharge_rvitem_moeny);
                        moneyTextView.setBackgroundColor(Color.parseColor("#ffa800"));
                        String moneyContent = moneyTextView.getText().toString();
                        moneyTextView.setText(LightSpanString.getLightString(moneyContent, Color.parseColor("#f9f9f9")));
                        String rechargeCount = "确认支付￥"+AmountConversionUitls.amountConversion(alipayRuleListBeans.get(position).getRechargeCount());
                        String str = String.valueOf(alipayRuleListBeans.get(position).getRechargeCount());
                        confirmPayBut.setText(rechargeCount);
                        ruleId = alipayRuleListBeans.get(position).getRuleId();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                break;
            case R.id.recharge_mode_weixin_RB:
                channelId = weixinchannelId;
                moneyRV.setAdapter(moneyAdapter = new CommonAdapter<RechargeRuleListBean>(this,R.layout.activity_recharge_rvitem_layout,weixinRuleListBeans) {
                    @Override
                    protected void convert(ViewHolder holder, RechargeRuleListBean ruleListBean, int position) {
                        String chengCount = AmountConversionUitls.coinConversion(ruleListBean.getChengCount());
                        String rechargeCount = AmountConversionUitls.amountConversion(ruleListBean.getRechargeCount());
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(chengCount);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa800"));
                        stringBuilder.setSpan(colorSpan,0,chengCount.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stringBuilder.append("\n");
                        stringBuilder.append(rechargeCount);
                        TextView moeny = holder.getView(R.id.recharge_rvitem_moeny);
                        moeny.setText(stringBuilder);
                    }
                });
                moneyAdapter.notifyDataSetChanged();
                moneyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        String rechargeCount = "确认支付￥"+AmountConversionUitls.amountConversion(weixinRuleListBeans.get(position).getRechargeCount());
                        confirmPayBut.setText(rechargeCount);
                        ruleId = weixinRuleListBeans.get(position).getRuleId();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                break;
                default:
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        HashMap hashMap = new HashMap();
        hashMap.put("channelId",channelId);
        hashMap.put("ruleId",ruleId);
        hashMap.put("userId",userId);
        hashMap.put("toUserId",userId);
        hashMap.put("proxyUserId",PROXY_USERID);
        rechargePresenter.getOrderInfo(hashMap);
    }

    @Override
    public void showOrderInfo(int orderId, String token) {
        mToken = token;
        int radioId= payModeRG.getCheckedRadioButtonId();
        if(radioId == R.id.recharge_mode_alipay_RB){
            aliPayAuth(token);
        }else{
            ToastUtils.showToast("微信支付");
        }
    }

    private class PayHandler extends BaseHandler{

        public PayHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void handleMessage(Message msg, int what) {
            switch (msg.what){
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String,String>)msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if(TextUtils.equals(resultStatus,PayResult.STATUS_CODE)){
                        LogUtils.d("支付成功"+resultInfo);
                    }else{
                        LogUtils.d("支付失败");
                    }
                    break;
                case SDK_AUTH_FLAG:
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj,true);
                    String authResultStatus = authResult.getResultStatus();
                    if(TextUtils.equals(authResultStatus,PayResult.STATUS_CODE)){
                        LogUtils.d("授权成功");
                        aliPay(mToken);
                    }else{
                        LogUtils.d("授权失败");
                    }
                    break;
            }
        }
    }

    /**
     * 支付宝账户授权业务
     */
    private void aliPayAuth(String token){
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                AuthTask authTask = new AuthTask(RechargeActivity.this);
                Map<String,String> result = authTask.authV2(token,true);
                Message msg = Message.obtain();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * 支付宝支付业务
     */
    private void aliPay(String token){
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new  PayTask(RechargeActivity.this);
                Map<String,String> result = alipay.payV2(token,true);
                LogUtils.d("payV2"+result.toString());
                Message msg = Message.obtain();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rechargePresenter.onDestroy();
    }
}

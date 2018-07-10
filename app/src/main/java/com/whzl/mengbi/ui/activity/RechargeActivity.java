package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RechargeChannelListBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.RechargeListBean;
import com.whzl.mengbi.model.entity.RechargeRuleListBean;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.impl.RechargePresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.view.RechargeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RechargeActivity extends BaseAtivity implements RechargeView,View.OnClickListener{

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

    private int channelId;//充值渠道id
    private int ruleId;//充值规则
    public  static  int PROXY_USERID;//充值代理

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
       confirmPayBut.setOnClickListener(this);
       GlideImageLoader.getInstace().circleCropImage(this,profile,profileIV);
       accountNumberTV.setText(userId+"");
       coinTV.setText(coin+"");

       layoutManager = new GridLayoutManager(this,3);
       moneyRV.setLayoutManager(layoutManager);


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
                channelId = channelListBean.getChannelId();
            }
            if(channelListBean.getChannelFlag().equals("WeixinApp")){
                for (RechargeListBean listBean:channelListBean.getList()){
                    weixinRuleListBeans.addAll(listBean.getRuleList());
                }
            }
        }

        int payMode = payModeRG.getCheckedRadioButtonId();
        if(payMode == R.id.recharge_mode_alipay_RB){
            moneyRV.setAdapter(moneyAdapter = new CommonAdapter<RechargeRuleListBean>(this,R.layout.activity_recharge_rvitem_layout,alipayRuleListBeans) {
                @Override
                protected void convert(ViewHolder holder, RechargeRuleListBean ruleListBean, int position) {
                    String chengCount = String.valueOf(ruleListBean.getChengCount())+"萌币";
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder(chengCount);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa800"));
                    stringBuilder.setSpan(colorSpan,0,chengCount.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBuilder.append("\n");
                    stringBuilder.append("￥"+ruleListBean.getRechargeCount());
                    TextView moeny = holder.getView(R.id.recharge_rvitem_moeny);
                    moeny.setText(stringBuilder);
                }
            });
            moneyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    String str = "确认支付￥"+String.valueOf(alipayRuleListBeans.get(position).getRechargeCount());
                    confirmPayBut.setText(str);
                    ruleId = alipayRuleListBeans.get(position).getRuleId();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

        }

        if(payMode == R.id.recharge_mode_weixin_RB){
            moneyRV.setAdapter(moneyAdapter = new CommonAdapter<RechargeRuleListBean>(this,R.layout.activity_recharge_rvitem_layout,weixinRuleListBeans) {
                @Override
                protected void convert(ViewHolder holder, RechargeRuleListBean ruleListBean, int position) {
                    String chengCount = String.valueOf(ruleListBean.getChengCount())+"萌币";
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder(chengCount);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa800"));
                    stringBuilder.setSpan(colorSpan,0,chengCount.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBuilder.append("\n");
                    stringBuilder.append("￥"+ruleListBean.getRechargeCount());
                    TextView moeny = holder.getView(R.id.recharge_rvitem_moeny);
                    moeny.setText(stringBuilder);
                }
            });

            moneyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    String str = "确认支付￥"+String.valueOf(weixinRuleListBeans.get(position).getRechargeCount());
                    confirmPayBut.setText(str);
                    ruleId = weixinRuleListBeans.get(position).getRuleId();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
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
        LogUtils.d("showOrderInfo>>>>>>"+orderId+">>>"+token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rechargePresenter.onDestroy();
    }
}

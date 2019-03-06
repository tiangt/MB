package com.whzl.mengbi.ui.dialog.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.MengdouChangeEvent;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.ClickUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2019/3/6
 */
public class SnatchDialog extends BaseAwesomeDialog {

    private TextView tvHisPrize;
    private ImageButton ibReduce;
    private ImageButton ibAdd;
    private TextView tvWant;
    private List list = new ArrayList();
    private long mUserId;
    private TextView tvMengdou;

    public static SnatchDialog newInstance(long mUserId) {
        SnatchDialog dialog = new SnatchDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("mUserId", mUserId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_snatch;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUserId = getArguments().getLong("mUserId");

        tvHisPrize = holder.getView(R.id.tv_his_prize);
        tvHisPrize.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvWant = holder.getView(R.id.tv_want_join);
        tvMengdou = holder.getView(R.id.tv_mengdou);
        ibReduce = holder.getView(R.id.ib_reduce_want);
        ibAdd = holder.getView(R.id.ib_add_want);
        ibReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tvWant.getText().toString()) <= 1) {
                    return;
                }
                tvWant.setText(String.valueOf(Integer.parseInt(tvWant.getText().toString()) - 1));
            }
        });
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tvWant.getText().toString()) >= 10) {
                    return;
                }
                tvWant.setText(String.valueOf(Integer.parseInt(tvWant.getText().toString()) + 1));
            }
        });
        holder.setOnClickListener(R.id.tv_five, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWant.setText("5");
            }
        });
        holder.setOnClickListener(R.id.tv_ten, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWant.setText("10");
            }
        });
        holder.setOnClickListener(R.id.iv_mengdou_snatch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastClick()) {
                    startActivity(new Intent(getActivity(), ShopActivity.class));
                }
            }
        });

        loadData();
    }

    private void loadData() {
        getMengdou();
    }

    private void getMengdou() {
        BusinessUtils.getUserInfo(getActivity(), mUserId + "", new BusinessUtils.UserInfoListener() {
            @Override
            public void onSuccess(UserInfo.DataBean bean) {
                if (bean != null && bean.getWealth() != null) {
                    long mengDou = bean.getWealth().getMengDou();
                    tvMengdou.setText(AmountConversionUitls.amountConversionFormat(mengDou));
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MengdouChangeEvent event) {
        getMengdou();
    }
}

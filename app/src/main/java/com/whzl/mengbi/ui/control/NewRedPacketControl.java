package com.whzl.mengbi.ui.control;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.OpenRedBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author nobody
 * @date 2019/1/17
 */
public class NewRedPacketControl {
    private final RelativeLayout redContain;
    private final RelativeLayout productView;
    private Context context;
    public List<RoomRedpackList.ListBean> redPackList = new ArrayList();
    private BaseAwesomeDialog offDialog;
    private BaseAwesomeDialog okDialog;
    private TextView tvSecond;
    private TextView tvSize;
    private Disposable disposable;

    private boolean isPlay = false;
    private final CompositeDisposable compositeDisposable;

    public NewRedPacketControl(Context context, RelativeLayout rlRedbagLive, RelativeLayout unclickLinearLayout) {
        this.context = context;
        this.redContain = rlRedbagLive;
        this.productView = unclickLinearLayout;
        compositeDisposable = new CompositeDisposable();
    }

    public void init() {
        tvSecond = redContain.findViewById(R.id.tv_second_redbag_live);
        tvSize = redContain.findViewById(R.id.tv_size_redbag_live);
        tvSize.setText(String.valueOf(redPackList.size()));
        if (redPackList.isEmpty() || isPlay) {
            return;
        }
        RoomRedpackList.ListBean listBean = redPackList.get(0);
        cutDown(listBean);
    }

    private void cutDown(RoomRedpackList.ListBean listBean) {
        tvSize.setText(String.valueOf(redPackList.size()));
        isPlay = true;
        long time = (System.currentTimeMillis() - listBean.currentTime) / 1000;
        redContain.setVisibility(View.VISIBLE);
        LogUtils.e("ssssssssssss   " + listBean.leftSeconds);
        LogUtils.e("ssssssssssss   " + time);

        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Long aLong) -> {
                    LogUtils.e("ssssssssssss   " + aLong);
                    if (aLong > listBean.leftSeconds - time) {
                        isPlay = false;
                        disposable.dispose();
                        redPackList.remove(listBean);
                        productRedbag(listBean);
                        if (redPackList.isEmpty()) {
                            redContain.setVisibility(View.GONE);
                        } else {
                            cutDown(redPackList.get(0));
                        }
                        return;
                    }
                    tvSecond.setText(String.valueOf(listBean.leftSeconds - time - aLong));
                });
        compositeDisposable.add(disposable);
    }

    private void productRedbag(RoomRedpackList.ListBean listBean) {
        Random random = new Random();
        int bottom = random.nextInt(UIUtil.getScreenHeightPixels(context) - UIUtil.dip2px(context, 75));
        int left = random.nextInt(UIUtil.getScreenWidthPixels(context) - UIUtil.dip2px(context, 59));
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtil.dip2px(context, 59), UIUtil.dip2px(context, 75));
        params.topMargin = bottom;
        params.leftMargin = left;
        imageView.setLayoutParams(params);
        GlideImageLoader.getInstace().displayImage(context, R.drawable.ic_red_pack_on_live, imageView);
        productView.addView(imageView);
        imageView.setOnClickListener(v -> {
            openRed(listBean);
            productView.removeView(imageView);
        });
        Disposable disposable = Observable.timer(listBean.expDate - listBean.effDate, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    LogUtils.e("ssssssssssss   effDate  " + aLong);
                    productView.removeView(imageView);
                });
        compositeDisposable.add(disposable);
    }


    public void openRed(RoomRedpackList.ListBean listBean) {
        if ((Long) SPUtils.get(context, SpConfig.KEY_USER_ID, 0L) == 0) {
            ((LiveDisplayActivity) context).login();
            return;
        }
        HashMap params = new HashMap();
        params.put("packed", listBean.redPacketId);
        params.put("userId", SPUtils.get(context, SpConfig.KEY_USER_ID, 0L).toString());
        params.put("token", SPUtils.get(context, SpConfig.KEY_SESSION_ID, "").toString());
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(SPUtils.get(context, SpConfig.REDPACKETURL, "").toString(), RequestManager.TYPE_POST_URL, params,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        OpenRedBean openRedBean = GsonUtils.GsonToBean(result.toString(), OpenRedBean.class);
                        if (openRedBean.code == 200) {
                            if (okDialog != null && okDialog.isAdded()) {
                                return;
                            }
                            okDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_ok)
                                    .setConvertListener(new ViewConvertListener() {
                                        @Override
                                        protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                                            holder.setText(R.id.tv_amount, AmountConversionUitls.amountConversionFormat(openRedBean.data.amount));
                                        }
                                    })
                                    .setOutCancel(true)
                                    .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                            EventBus.getDefault().post(new UserInfoUpdateEvent());
                        } else if (openRedBean.code == 1008) {
                            if (offDialog != null && offDialog.isAdded()) {
                                return;
                            }
                            offDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_off)
                                    .setConvertListener(new ViewConvertListener() {
                                        @Override
                                        protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

                                        }
                                    })
                                    .setOutCancel(true)
                                    .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        AwesomeDialog.init().setLayoutId(R.layout.dialog_redpack_off)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

                                    }
                                })
                                .setOutCancel(true)
                                .show(((LiveDisplayActivity) context).getSupportFragmentManager());
                    }
                });
    }

    public void destroy() {
        compositeDisposable.dispose();
        redContain.setVisibility(View.GONE);
        productView.removeAllViews();
    }

    public void notifyDataSetChanged() {
        tvSize.setText(String.valueOf(redPackList.size()));
    }
}

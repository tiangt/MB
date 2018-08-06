package com.whzl.mengbi.ui.dialog.base;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GuardPriceBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/6
 */
public class GuardDetailDialog extends BaseAwesomeDialog {
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private long mProgramId;
    private GuardPriceBean mGuardPriceBean;

    public static GuardDetailDialog newInstance(int programId, RoomInfoBean.DataBean.AnchorBean anchorBean) {
        GuardDetailDialog detailDialog = new GuardDetailDialog();
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putParcelable("anchor", anchorBean);
        detailDialog.setArguments(args);
        return detailDialog;
    }


    @Override
    public int intLayoutId() {
        return R.layout.dialog_guard_detaili;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mAnchorBean = getArguments().getParcelable("anchor");
        mProgramId = getArguments().getInt("programId");
        ApiFactory.getInstance().getApi(Api.class)
                .getGuardPrice(ParamsUtils.getSignPramsMap(new HashMap<>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GuardPriceBean>(this) {
                    @Override
                    public void onSuccess(GuardPriceBean guardPriceBean) {
                        if (guardPriceBean != null) {
                            mGuardPriceBean = guardPriceBean;
                            if (guardPriceBean.prices != null && guardPriceBean.prices.month != null) {
                                tvCost.setText(guardPriceBean.prices.month.rent + "");
                            }
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
        tvNickName.setText(mAnchorBean.getName());
        GlideImageLoader.getInstace().displayImage(getContext(), mAnchorBean.getAvatar(), ivAvatar);
    }


    @OnClick(R.id.btn_guard)
    public void onClick() {
        if (mGuardPriceBean == null) {
            ToastUtils.showToast("数据有误");
            return;
        }
        long userId = (long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        HashMap paramsMap = new HashMap();
        paramsMap.put("roomId", mProgramId);
        paramsMap.put("programId", mProgramId);
        paramsMap.put("targetId", mAnchorBean.getId());
        paramsMap.put("goodsId", mGuardPriceBean.goodsId);
        paramsMap.put("count", 1);
        paramsMap.put("userId", userId);
        paramsMap.put("useBag", false);
        ApiFactory.getInstance().getApi(Api.class)
                .sendGift(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {
                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}

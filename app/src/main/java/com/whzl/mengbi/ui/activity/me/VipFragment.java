package com.whzl.mengbi.ui.activity.me;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GetVipPriceBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.BusinessUtils;
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
 * @author niko
 * @date 2018/9/18
 */
public class VipFragment extends BaseFragment {
    private static final int BUY = 1;
    private static final int SEND = 2;
    @BindView(R.id.btn_buy_shop)
    Button btnBuyShop;
    @BindView(R.id.btn_send_shop)
    Button btnSendShop;
    private BaseAwesomeDialog buyDialog;
    private GetVipPriceBean priceBean;
    private String toUserId;
    private boolean idIsOK;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_shop;
    }

    @Override
    public void init() {
        HashMap paramsMap = new HashMap();
        ApiFactory.getInstance().getApi(Api.class)
                .getVipprices(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetVipPriceBean>(this) {

                    @Override
                    public void onSuccess(GetVipPriceBean bean) {
                        priceBean = bean;
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }


    @OnClick({R.id.btn_buy_shop, R.id.btn_send_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_shop:
//                startActivity(new Intent(getMyActivity(), BuyVipActivity.class));
                if (buyDialog != null && buyDialog.isAdded()) {
                    return;
                }
                showDialog(BUY);
                break;
            case R.id.btn_send_shop:
//                startActivity(new Intent(getMyActivity(), SendVipActivity.class));
                if (buyDialog != null && buyDialog.isAdded()) {
                    return;
                }
                showDialog(SEND);
                break;
        }
    }

    private void showDialog(int type) {
        idIsOK = false;
        toUserId = "";
        UserInfo.DataBean currentUser = ((ShopActivity) getMyActivity()).currentUser;
        buyDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_vip_shop).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                holder.setVisible(R.id.ll_buy, type == BUY);
                holder.setVisible(R.id.ll_send, type == SEND);
                TextView tvSend = holder.getView(R.id.tv_type);
                tvSend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                holder.setText(R.id.tv_type, type == BUY ? "赠送他人" : "自己购买");
                holder.setOnClickListener(R.id.tv_type, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showDialog(type == BUY ? SEND : BUY);
                    }
                });
                holder.setOnClickListener(R.id.ib_clear, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setText(R.id.et_send, "");
                    }
                });
                EditText editText = (EditText) holder.getView(R.id.et_send);
                holder.setOnClickListener(R.id.btn_send, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            return;
                        }
                        BusinessUtils.getUserInfo(getMyActivity(), editText.getText().toString().trim(),
                                new BusinessUtils.UserInfoListener() {
                                    @Override
                                    public void onSuccess(UserInfo.DataBean bean) {
                                        holder.setVisible(R.id.ll_buy, true);
                                        holder.setVisible(R.id.ll_send, false);
                                        GlideImageLoader.getInstace().displayImage(getMyActivity(), bean.getAvatar()
                                                , holder.getView(R.id.iv_avatar));
                                        holder.setText(R.id.tv_nick, bean.getNickname());
                                        toUserId = String.valueOf(bean.getUserId());
                                        idIsOK = true;
                                    }

                                    @Override
                                    public void onError(int code) {
                                        ToastUtils.showToastUnify(getMyActivity(), "该萌号用户不存在");
                                        idIsOK = false;
                                    }
                                });
                    }
                });
                GlideImageLoader.getInstace().displayImage(getMyActivity(), currentUser.getAvatar()
                        , holder.getView(R.id.iv_avatar));
                holder.setText(R.id.tv_nick, currentUser.getNickname());
                holder.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == BUY) {
                            buy();
                        } else {
                            if (idIsOK) {
                                buy();
                            }
                        }
                    }
                });
            }
        }).setDimAmount(0).setShowBottom(true).show(getFragmentManager());
    }

    private void buy() {
        BusinessUtils.mallBuy(getMyActivity(), String.valueOf(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L)), String.valueOf(priceBean.goodsId)
                , String.valueOf(priceBean.prices.month.priceId), "1", toUserId, "", new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToastUnify(getMyActivity(), "购买成功");
                        buyDialog.dismiss();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}

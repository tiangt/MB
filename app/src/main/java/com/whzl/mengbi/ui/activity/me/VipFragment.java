package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author niko
 * @date 2018/9/18
 */
public class VipFragment extends BaseFragment {
    @BindView(R.id.btn_buy_shop)
    Button btnBuyShop;
    @BindView(R.id.btn_send_shop)
    Button btnSendShop;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_shop;
    }

    @Override
    public void init() {

    }


    @OnClick({R.id.btn_buy_shop, R.id.btn_send_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_shop:
                startActivity(new Intent(getMyActivity(), BuyVipActivity.class));
                showDialog();
                break;
            case R.id.btn_send_shop:
                startActivity(new Intent(getMyActivity(), SendVipActivity.class));
                break;
        }
    }

    private void showDialog() {
        UserInfo.DataBean currentUser = ((ShopActivity) getMyActivity()).currentUser;
        BaseAwesomeDialog dialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_vip_shop).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                GlideImageLoader.getInstace().displayImage(getMyActivity(), currentUser.getAvatar()
                        , holder.getView(R.id.iv_avatar));
                holder.setText(R.id.tv_nick, currentUser.getNickname());
                TextView tvSend = holder.getView(R.id.tv_send);
                tvSend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                tvSend.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            }
        }).setDimAmount(0).setShowBottom(true).show(getFragmentManager());
    }
}

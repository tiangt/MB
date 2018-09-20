package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.view.AlignTextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author niko
 * @date 2018/9/18
 */
public class VipFragment extends BaseFragment {
    @BindView(R.id.atv1)
    AlignTextView atv1;
    @BindView(R.id.atv2)
    AlignTextView atv2;
    Unbinder unbinder;
    @BindView(R.id.atv3)
    AlignTextView atv3;
    @BindView(R.id.atv4)
    AlignTextView atv4;
    @BindView(R.id.atv5)
    AlignTextView atv5;
    @BindView(R.id.atv6)
    AlignTextView atv6;
    @BindView(R.id.atv7)
    AlignTextView atv7;
    @BindView(R.id.atv8)
    AlignTextView atv8;
    @BindView(R.id.atv9)
    AlignTextView atv9;
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
        initNote();

    }

    private void initNote() {
        atv1.setText("尊贵标识");
        atv2.setText("排位靠前");
        atv3.setText("私聊");
        atv4.setText("防禁踢");
        atv5.setText("聊天字数");
        atv6.setText("专属表情");
        atv7.setText("免费礼物");
        atv8.setText("广播卡");
        atv9.setText("炫彩昵称");
    }


    @OnClick({R.id.btn_buy_shop, R.id.btn_send_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_shop:
                startActivity(new Intent(getMyActivity(),BuyVipActivity.class));
                break;
            case R.id.btn_send_shop:
                startActivity(new Intent(getMyActivity(),SendVipActivity.class));
                break;
        }
    }
}

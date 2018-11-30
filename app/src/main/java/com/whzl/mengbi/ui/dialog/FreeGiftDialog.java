package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.BuySuccubusActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class FreeGiftDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_buy_card)
    TextView tvBuyCard;

    public static BaseAwesomeDialog newInstance() {
        FreeGiftDialog freeGiftDialog = new FreeGiftDialog();
        return freeGiftDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_free_gift;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        tvBuyCard.getPaint().setAntiAlias(true);
        tvBuyCard.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        tvBuyCard.getPaint().setFakeBoldText(true);
        tvBuyCard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    @OnClick({R.id.tv_buy_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy_card:
                getActivity().startActivity(new Intent(getActivity(), BuySuccubusActivity.class));
                break;
        }
    }
}

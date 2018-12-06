package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.12.6
 */
public class TipOffDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_tip_1)
    TextView tvTip1;
    @BindView(R.id.tv_tip_2)
    TextView tvTip2;
    @BindView(R.id.tv_tip_3)
    TextView tvTip3;
    @BindView(R.id.tv_tip_4)
    TextView tvTip4;
    @BindView(R.id.tv_tip_5)
    TextView tvTip5;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private long mUserId;
    private long mVisitorId;
    private int mProgramId;

    public static TipOffDialog newInstance(long userId, long visitorId, int programId) {
        Bundle args = new Bundle();
        args.putLong("userId",userId);
        args.putLong("visitorId",visitorId);
        args.putInt("programId",programId);
        TipOffDialog dialog = new TipOffDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_tip_off;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUserId = getArguments().getLong("userId",0);
        mVisitorId = getArguments().getLong("visitorId",0);
        mProgramId = getArguments().getInt("programId",0);
    }

    @OnClick({R.id.tv_tip_1, R.id.tv_tip_2, R.id.tv_tip_3, R.id.tv_tip_4,
            R.id.tv_tip_5, R.id.tv_other, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_1:
                break;
            case R.id.tv_tip_2:
                break;
            case R.id.tv_tip_3:
                break;
            case R.id.tv_tip_4:
                break;
            case R.id.tv_tip_5:
                break;
            case R.id.tv_other:
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void tipOff(){

    }
}

package com.whzl.mengbi.ui.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.12.25
 */
public class OneClickDialog extends BaseAwesomeDialog {

    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    public static BaseAwesomeDialog newInstance() {
        OneClickDialog dialog = new OneClickDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_one_click;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

    }

    @OnClick({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                break;

            case R.id.btn_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }
}

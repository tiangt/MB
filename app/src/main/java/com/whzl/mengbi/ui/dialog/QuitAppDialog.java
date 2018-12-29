package com.whzl.mengbi.ui.dialog;

import android.view.View;
import android.widget.Button;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.12.29
 */
public class QuitAppDialog extends BaseAwesomeDialog {

    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    public static QuitAppDialog newInstance() {
        QuitAppDialog dialog = new QuitAppDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_qiut_app;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

    }

    @OnClick({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                dismiss();
                ((MainActivity)getActivity()).isExit = true;
                break;
            case R.id.btn_cancel:
                ((MainActivity)getActivity()).isExit = false;
                dismiss();
                break;
            default:
                break;
        }
    }
}

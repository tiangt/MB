package com.whzl.mengbi.ui.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2019.1.9
 */
public class BindingPhoneDialog extends BaseFullScreenDialog {

    @BindView(R.id.tv_jump)
    TextView tvJump;
    @BindView(R.id.btn_binding)
    Button btnBinding;

    public static BindingPhoneDialog newInstance() {
        BindingPhoneDialog dialog = new BindingPhoneDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_binding_phone;
    }

    @Override
    public void convertView(ViewHolder holder, BaseFullScreenDialog dialog) {

    }

    @OnClick({R.id.tv_jump, R.id.btn_binding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                dismiss();
                break;

            case R.id.btn_binding:
                dismiss();
                break;

            default:
                break;
        }
    }
}

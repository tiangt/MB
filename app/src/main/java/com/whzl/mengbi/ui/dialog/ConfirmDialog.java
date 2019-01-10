package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 确认，取消
 *
 * @author cliang
 * @date 2019.1.10
 */
public class ConfirmDialog extends BaseAwesomeDialog {

    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private OnClickListener listener;

    public static ConfirmDialog newInstance(String prompt) {
        Bundle args = new Bundle();
        args.putString("prompt", prompt);
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public interface OnClickListener {
        void onClickSuccess();
    }

    public BaseAwesomeDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        String prompt = getArguments().getString("prompt");
        tvPrompt.setText(prompt);
    }

    @OnClick({R.id.btn_confirm, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (listener != null) {
                    listener.onClickSuccess();
                }
                dismiss();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }
}

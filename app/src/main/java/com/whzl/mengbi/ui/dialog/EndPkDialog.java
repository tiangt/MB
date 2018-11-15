package com.whzl.mengbi.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.whzl.mengbi.R;

/**
 * @author cliang
 * @date 2018.11.15
 */
public class EndPkDialog extends AlertDialog implements View.OnClickListener {

    private Context context;
    private TextView tvCountdown;
    private TextView tvJump;

    public EndPkDialog(Context context) {
        super(context);
        this.context = context;
    }

    public EndPkDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EndPkDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_end_pk, null);
        setContentView(view);
        tvCountdown = view.findViewById(R.id.tv_countdown);
        tvJump = view.findViewById(R.id.tv_jump);
        tvJump.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.9);
        dialogWindow.setAttributes(lp);
        new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String value = String.valueOf((int) (millisUntilFinished / 1000L));
                tvCountdown.setText(value + " s");
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                dismiss();
                break;
            default:
                break;
        }
    }
}

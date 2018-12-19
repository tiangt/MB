package com.whzl.mengbi.ui.dialog;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.SendSuperWordEvent;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/12/19
 */
public class AddSendWordDialog extends AwesomeDialog {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_num)
    TextView tvNum;

    public static AddSendWordDialog newInstance() {
        AddSendWordDialog addSendWordDialog = new AddSendWordDialog();
        return addSendWordDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_add_send_word;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        super.convertView(holder, dialog);
        String string = SPUtils.get(getActivity(), SpConfig.DEFAULT_SENT_WORD, "").toString();
        etContent.setText(TextUtils.isEmpty(string) ? "" : string);
        tvNum.setText((20 - etContent.getText().toString().length()) + "");
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNum.setText((20 - s.toString().length()) + "");
            }
        });
    }


    @OnClick({R.id.tv_cancel, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                KeyBoardUtil.closeKeybord(etContent, getActivity());
                dismiss();
                break;
            case R.id.tv_save:
                SPUtils.put(getActivity(), SpConfig.DEFAULT_SENT_WORD, etContent.getText().toString().trim());
                KeyBoardUtil.closeKeybord(etContent, getActivity());
                EventBus.getDefault().post(new SendSuperWordEvent());
                dismiss();
                break;
        }
    }


}

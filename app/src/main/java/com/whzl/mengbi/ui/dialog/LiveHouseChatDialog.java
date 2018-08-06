package com.whzl.mengbi.ui.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.CommonEmojiMotherFragment;
import com.whzl.mengbi.ui.dialog.fragment.EmojiFragment;
import com.whzl.mengbi.ui.dialog.fragment.GuardEmojiFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragmentNew;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class LiveHouseChatDialog extends BaseAwesomeDialog implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.ll_emoji_container)
    LinearLayout llEmojiContiner;
    @BindView(R.id.btn_input_change)
    Button btnInputChange;
    @BindView(R.id.dialog_out)
    View dialogOut;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rg_emoji_switch)
    RadioGroup rgEmojiSwitch;
    private boolean isShowSoftInput;
    private int height;
    private int currentSelectedIndex;
    private Fragment[] fragments;

    public static BaseAwesomeDialog newInstance() {
        return new LiveHouseChatDialog();
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_chat;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        etContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == 1) {
                    KeyBoardUtil.closeKeybord(etContent, getContext());
                    dismiss();
                }
                return false;
            }
        });
        initFragments();
        rgEmojiSwitch.check(R.id.rb_comm);
        rgEmojiSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_comm:
                        setTabChange(0);
                        break;
                    case R.id.rb_guard:
                        setTabChange(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initFragments() {
        CommonEmojiMotherFragment commonEmojiMotherFragment = CommonEmojiMotherFragment.newInstance();
        commonEmojiMotherFragment.setEtContent(etContent);
        fragments = new Fragment[]{commonEmojiMotherFragment, GuardEmojiFragment.newInstance()};
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.commit();
    }

    private void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[currentSelectedIndex]);
        if (fragments[index].isAdded()) {
            fragmentTransaction.show(fragments[index]);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragments[index]);
        }
        fragmentTransaction.commit();
        currentSelectedIndex = index;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (height == 0) {
            height = dialogOut.getHeight();
            return;
        }
        if (dialogOut.getHeight() - height > 100 && !btnInputChange.isSelected() && isShowSoftInput) {
            dismiss();
        }
        if (dialogOut.getHeight() - height < -100 && !btnInputChange.isSelected()) {
            isShowSoftInput = true;
        }
        height = dialogOut.getHeight();
    }

    @Override
    public void onResume() {
        super.onResume();
        etContent.performClick();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @OnClick({R.id.btn_send, R.id.btn_input_change, R.id.dialog_out, R.id.et_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String message = etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    ((LiveDisplayActivity) getActivity()).sendMeeage(message);
                    etContent.getText().clear();
                    KeyBoardUtil.closeKeybord(etContent, getContext());
                    dismiss();
                }
                break;
            case R.id.btn_input_change:
                btnInputChange.setSelected(!btnInputChange.isSelected());
                if (btnInputChange.isSelected()) {
                    KeyBoardUtil.closeKeybord(etContent, getContext());
                    llEmojiContiner
                            .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llEmojiContiner.setVisibility(View.VISIBLE);
                            isShowSoftInput = false;
                        }
                    }, 200);
                } else {
                    llEmojiContiner.setVisibility(View.GONE);
                    KeyBoardUtil.openKeybord(etContent, getContext());
                }
                break;
            case R.id.et_content:
                btnInputChange.setSelected(false);
                llEmojiContiner.setVisibility(View.GONE);
                KeyBoardUtil.openKeybord(etContent, getContext());
                break;
            case R.id.dialog_out:
                KeyBoardUtil.closeKeybord(etContent, getContext());
                dismiss();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDestroyView() {
        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        super.onDestroyView();
    }
}

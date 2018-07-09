package com.whzl.mengbi.ui.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.EmojiFragment;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/6
 */
public class LiveHouseChatDialog extends BaseAwesomeDialog implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_index_container)
    LinearLayout pagerIndexContainer;
    @BindView(R.id.ll_emoji_container)
    LinearLayout llEmojiContiner;
    @BindView(R.id.btn_input_change)
    Button btnInputChange;
    @BindView(R.id.dialog_out)
    View dialogOut;
    private boolean isShowSoftInput;
    private int height;

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
        ArrayList<Fragment> pagers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EmojiFragment emojiFragment = EmojiFragment.newInstance(i);
            emojiFragment.setMessageEditText(etContent);
            pagers.add(emojiFragment);
        }
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), pagers));
        setUpPagerIndex();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if(height == 0){
            height = dialogOut.getHeight();
            return;
        }
        if (dialogOut.getHeight() - height > 100 && !btnInputChange.isSelected() && isShowSoftInput) {
            dismiss();
        }
        if(dialogOut.getHeight() - height < -100 && !btnInputChange.isSelected()){
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

    private void setUpPagerIndex() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 8), UIUtil.dip2px(getContext(), 8));
        for (int i = 0; i < 3; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.selector_common_pager_index);
            if (i == 0) {
                view.setSelected(true);
            } else {
                params.leftMargin = UIUtil.dip2px(getContext(), 7.5f);
            }
            pagerIndexContainer.addView(view, params);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        pagerIndexContainer.getChildAt(i).setSelected(true);
                    } else {
                        pagerIndexContainer.getChildAt(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.btn_send, R.id.btn_input_change, R.id.dialog_out, R.id.et_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                break;
            case R.id.btn_input_change:
                btnInputChange.setSelected(!btnInputChange.isSelected());
                if (btnInputChange.isSelected()) {
                    KeyBoardUtil.closeKeybord(etContent, getContext());
                    viewPager.postDelayed(new Runnable() {
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

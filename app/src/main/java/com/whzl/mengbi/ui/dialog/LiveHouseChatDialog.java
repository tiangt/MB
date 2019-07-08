package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.message.events.ChatInputEvent;
import com.whzl.mengbi.chat.room.message.events.SendBroadEvent;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.CLickGuardOrVipEvent;
import com.whzl.mengbi.greendao.PrivateChatUser;
import com.whzl.mengbi.model.entity.BroadCastNumBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.GuardDetailDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.CommonEmojiMotherFragment;
import com.whzl.mengbi.ui.dialog.fragment.GuardEmojiFragment;
import com.whzl.mengbi.ui.dialog.fragment.VipEmojiFragment;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.SoftKeyboardStateHelper;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.btn_input_broad)
    Button btnInputBroad;
    @BindView(R.id.container_hot_word)
    LinearLayout containerHot;
    private boolean isShowSoftInput = true;
    private int height;
    private int currentSelectedIndex;
    private Fragment[] fragments;
    private boolean isGuard;
    private boolean isVip;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private int mProgramId;
    private PrivateChatUser mChatToUser;
    private int total;
    private Long userId;
    private String mAt;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;
    private List<String> hotList;

    public static BaseAwesomeDialog newInstance(boolean isGuard, boolean isVip, int programId, RoomInfoBean.DataBean.AnchorBean anchorBean) {
        LiveHouseChatDialog liveHouseChatDialog = new LiveHouseChatDialog();
        Bundle args = new Bundle();
        args.putBoolean("isGuard", isGuard);
        args.putBoolean("isVip", isVip);
        args.putInt("programId", programId);
        args.putParcelable("anchor", anchorBean);


        liveHouseChatDialog.setArguments(args);
        return liveHouseChatDialog;
    }

    public static BaseAwesomeDialog newInstance(boolean isGuard, boolean isVip, int programId, RoomInfoBean.DataBean.AnchorBean anchorBean, PrivateChatUser dateBean) {
        LiveHouseChatDialog liveHouseChatDialog = new LiveHouseChatDialog();
        Bundle args = new Bundle();
        args.putBoolean("isGuard", isGuard);
        args.putBoolean("isVip", isVip);
        args.putInt("programId", programId);
        args.putParcelable("anchor", anchorBean);
        args.putParcelable("chatToUser", dateBean);
        liveHouseChatDialog.setArguments(args);
        return liveHouseChatDialog;
    }

    public static BaseAwesomeDialog newInstance(boolean isGuard, boolean isVip, int programId, String at, RoomInfoBean.DataBean.AnchorBean anchorBean) {
        LiveHouseChatDialog liveHouseChatDialog = new LiveHouseChatDialog();
        Bundle args = new Bundle();
        args.putBoolean("isGuard", isGuard);
        args.putBoolean("isVip", isVip);
        args.putInt("programId", programId);
        args.putString("at", at);
        args.putParcelable("anchor", anchorBean);
        liveHouseChatDialog.setArguments(args);
        return liveHouseChatDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mChatToUser == null) {
            dialogOut.setVisibility(View.GONE);
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } else {
            dialogOut.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_house_chat;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

        softKeyboardStateHelper = new SoftKeyboardStateHelper(getActivity().findViewById(R.id.rootView));
        softKeyboardStateListener = new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {

            }

            @Override
            public void onSoftKeyboardClosed() {
                if (!btnInputChange.isSelected()) {
                    hide();
                }
            }
        };
        softKeyboardStateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);

        isGuard = getArguments().getBoolean("isGuard");
        isVip = getArguments().getBoolean("isVip");

        userId = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        mProgramId = getArguments().getInt("programId");
        mAnchorBean = getArguments().getParcelable("anchor");
        mChatToUser = getArguments().getParcelable("chatToUser");
        mAt = getArguments().getString("at");
        if (!TextUtils.isEmpty(mAt)) {
            etContent.setText(mAt);
            etContent.setSelection(mAt.length());
        }
        if (mChatToUser != null) {
            etContent.setHint("对" + mChatToUser.getName() + "私聊");
            btnInputBroad.setVisibility(View.GONE);
        }
        etContent.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == 1) {
                hide();
                isShowSoftInput = false;
                if (mChatToUser == null) {
                    EventBus.getDefault().post(new ChatInputEvent(-1));
                }

            }
            return false;
        });
        etContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String message = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    return true;
                }
                if (btnInputBroad.isSelected()) {
                    sendBroadCast(message);
                    etContent.getText().clear();
                } else {
                    ((LiveDisplayActivity) getActivity()).sendMessage(message, mChatToUser);
                    etContent.getText().clear();
                }
                return true;
            }
            return false;
        });
        initFragments();
        rgEmojiSwitch.check(R.id.rb_comm);
        rgEmojiSwitch.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_comm:
                    setTabChange(0);
                    break;
                case R.id.rb_guard:
                    setTabChange(1);
                    break;
                case R.id.rb_vip:
                    setTabChange(2);
                    break;
                default:
                    break;
            }
        });
        getBroadNum();

        if (mChatToUser == null) {
            initHotRv();
        }
    }

    private void initHotRv() {
        //666      1     233      为主播打call      主播你飘了     求联系方式      主播哪里人
        hotList = new ArrayList<>();
        hotList.add("666");
        hotList.add("1");
        hotList.add("233");
        hotList.add("为主播打call");
        hotList.add("主播你飘了");
        hotList.add("求联系方式");
        hotList.add("主播哪里人");

        for (int i = 0; i < hotList.size(); i++) {
            String s = hotList.get(i);
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_word, null);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = UIUtil.dip2px(getActivity(), 3);
            layoutParams.rightMargin = UIUtil.dip2px(getActivity(), 3);
            ((TextView) inflate.findViewById(R.id.tv_hot_word)).setText(s);
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LiveDisplayActivity) getActivity()).sendMessage(s, null);
                }
            });
            containerHot.addView(inflate, layoutParams);
        }
    }


    private void getBroadNum() {
        HashMap params = new HashMap();
        params.put("userId", userId);
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .getBroadNum(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BroadCastNumBean>() {

                    @Override
                    public void onSuccess(BroadCastNumBean bean) {
                        total = bean.total;
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void initFragments() {
        CommonEmojiMotherFragment commonEmojiMotherFragment = CommonEmojiMotherFragment.newInstance();
        commonEmojiMotherFragment.setEtContent(etContent);
        GuardEmojiFragment guardEmojiFragment = GuardEmojiFragment.newInstance(isGuard);
        guardEmojiFragment.setMessageEditText(etContent);
        //VIP表情
        VipEmojiFragment vipEmojiFragment = VipEmojiFragment.newInstance(isVip);
        vipEmojiFragment.setMessageEditText(etContent);

        guardEmojiFragment.setListener(() -> {
            GuardDetailDialog.newInstance(mProgramId, mAnchorBean).setShowBottom(true).setDimAmount(0).show(getFragmentManager());
            dismiss();
            EventBus.getDefault().post(new CLickGuardOrVipEvent());
        });

        vipEmojiFragment.setVipListener(() -> {
            Intent intent = new Intent(getContext(), ShopActivity.class);
            intent.putExtra(ShopActivity.SELECT, 1);
            ((LiveDisplayActivity) getActivity()).startActivityForResult(intent, AppUtils.OPEN_VIP);
            dismiss();
            EventBus.getDefault().post(new CLickGuardOrVipEvent());
        });

        fragments = new Fragment[]{commonEmojiMotherFragment, guardEmojiFragment, vipEmojiFragment};
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.commit();
    }


    private void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
        int[] location = new int[2];
        etContent.getLocationOnScreen(location);
        if (height == 0) {
            height = location[1];
            return;
        }
        if (location[1] - height > 100 && !btnInputChange.isSelected() && isShowSoftInput) {
            hide();
        }
//        if (location[1] - height < -100 && !btnInputChange.isSelected()) {
//            isShowSoftInput = true;
//            if (mChatToUser == null) {
//                EventBus.getDefault().post(new ChatInputEvent(getResources().getDisplayMetrics().heightPixels - location[1]));
//            }
//        }
        height = location[1];
    }


    @Override
    public void onResume() {
        super.onResume();
        if (etContent != null) {
            etContent.performClick();
            if (mChatToUser == null) {
                EventBus.getDefault().post(new ChatInputEvent(1));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @OnClick({R.id.btn_send, R.id.btn_input_change, R.id.dialog_out, R.id.et_content, R.id.btn_input_broad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String message = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                if (btnInputBroad.isSelected()) {
                    sendBroadCast(message);
                    etContent.getText().clear();
                } else {
                    ((LiveDisplayActivity) getActivity()).sendMessage(message, mChatToUser);
                    etContent.getText().clear();
                }
                break;
            case R.id.btn_input_change:
                btnInputChange.setSelected(!btnInputChange.isSelected());
                if (btnInputChange.isSelected()) {
                    KeyBoardUtil.closeKeybord(etContent, getContext());
                    llEmojiContiner
                            .postDelayed(() -> {
                                LogUtils.e("Thread.currentThread().getId() " + Thread.currentThread().getId());
                                llEmojiContiner.setVisibility(View.VISIBLE);
                                isShowSoftInput = false;
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
                hide();
                break;
            case R.id.btn_input_broad:
                long userId = (long) SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0);
                if (userId == 0) {
                    ((LiveDisplayActivity) getActivity()).login();
                    hide();
                    return;
                }
                btnInputBroad.setSelected(!btnInputBroad.isSelected());
                if (btnInputBroad.isSelected()) {
                    etContent.setHint("广播每条10000萌币 (广播卡:" + total + ")");
                } else {
                    etContent.setHint(getString(R.string.live_house_chat_edit_text_hint));
                }
                break;
            default:
                break;
        }
    }

    public void hide() {
        if (etContent == null) {
            return;
        }
        isShowSoftInput = false;
        KeyBoardUtil.closeKeybord(etContent, getContext());
        dismiss();
        if (mChatToUser == null) {
            EventBus.getDefault().post(new ChatInputEvent(-1));
        }
    }

    private void sendBroadCast(String message) {
        HashMap params = new HashMap();
        params.put("userId", userId);
        params.put("programId", mProgramId);
        params.put("message", message);
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .sendBroadcast(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        ToastUtils.showToast("发送成功");
                        EventBus.getDefault().post(new SendBroadEvent());
                        hide();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDestroyView() {
        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (softKeyboardStateHelper != null && softKeyboardStateListener != null) {
            softKeyboardStateHelper.removeSoftKeyboardStateListener(softKeyboardStateListener);
        }
        super.onDestroyView();
    }
}

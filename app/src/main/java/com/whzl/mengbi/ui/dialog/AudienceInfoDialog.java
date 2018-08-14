package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.whzl.mengbi.util.UserIdentity.ROOM_MANAGER;
import static com.whzl.mengbi.util.UserIdentity.getCanChatPaivate;

/**
 * @author shaw
 * @date 2018/7/11
 */
public class AudienceInfoDialog extends BaseAwesomeDialog {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_level_icon)
    ImageView ivLevelIcon;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_ban)
    TextView tvBan;
    @BindView(R.id.tv_kick_out)
    TextView tvKickOut;
    @BindView(R.id.tv_upgrade)
    TextView tvUpgrade;
    @BindView(R.id.tv_private_chat)
    TextView tvPrivateChat;
    @BindView(R.id.ll_option_container)
    LinearLayout llOptionContainer;
    private RoomUserInfo.DataBean mViewedUser;
    private RoomUserInfo.DataBean mUser;
    private long mViewUserId;
    private int mProgramId;

    public static AudienceInfoDialog newInstance(long userId, int programId) {
        AudienceInfoDialog dialog = new AudienceInfoDialog();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        dialog.setArguments(args);
        return dialog;
    }

    public static AudienceInfoDialog newInstance(long userId, int programId, RoomUserInfo.DataBean user) {
        AudienceInfoDialog dialog = new AudienceInfoDialog();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        args.putParcelable("user", user);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_audience_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mViewUserId = getArguments().getLong("userId");
        mProgramId = getArguments().getInt("programId");
        mUser = getArguments().getParcelable("user");
        if (mUser == null || mUser.getUserId() == 0 || mUser.getUserId() == mViewUserId) {
            llOptionContainer.setVisibility(View.GONE);
        }
        getUserInfo(mViewUserId, mProgramId);
    }

    private void getUserInfo(long userId, int programId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                if (isDetached() || getContext() == null) {
                    return;
                }
                RoomUserInfo roomUserInfoData = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfoData.getCode() == 200) {
                    if (roomUserInfoData.getData() != null) {
                        mViewedUser = roomUserInfoData.getData();
                        setupView(mViewedUser);
                        if (mUser == null || mUser.getUserId() == mViewUserId) {
                            return;
                        }
                        setupOperations();
                    }
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setupOperations() {
        if (mUser.getIdentityId() == UserIdentity.OPTR_MANAGER) {
            if (mViewedUser.getIdentityId() == UserIdentity.ANCHOR) {
                tvPrivateChat.setVisibility(View.VISIBLE);
            } else {
                tvBan.setVisibility(View.VISIBLE);
                tvKickOut.setVisibility(View.VISIBLE);
                tvPrivateChat.setVisibility(getCanChatPaivate(mViewedUser) ? View.VISIBLE : View.GONE);
            }
            return;
        }

        if (mUser.getIdentityId() == UserIdentity.ANCHOR) {
            if (mViewedUser.getIdentityId() == UserIdentity.OPTR_MANAGER) {
                tvPrivateChat.setVisibility(View.VISIBLE);
            } else {
                tvBan.setVisibility(View.VISIBLE);
                tvKickOut.setVisibility(View.VISIBLE);
                tvPrivateChat.setVisibility(getCanChatPaivate(mViewedUser) ? View.VISIBLE : View.GONE);
                tvUpgrade.setVisibility(mViewUserId == 0 ? View.GONE : View.VISIBLE);
            }
            return;
        }

        if (mUser.getIdentityId() == UserIdentity.ROOM_MANAGER) {
            if (mViewedUser.getIdentityId() == UserIdentity.OPTR_MANAGER || mViewedUser.getIdentityId() == UserIdentity.ANCHOR) {
                tvPrivateChat.setVisibility(View.VISIBLE);
            } else {
                tvBan.setVisibility(View.VISIBLE);
                tvKickOut.setVisibility(View.VISIBLE);
                tvPrivateChat.setVisibility(getCanChatPaivate(mViewedUser) ? View.VISIBLE : View.GONE);
            }
            return;
        }
        llOptionContainer.setVisibility(getCanChatPaivate(mUser) && getCanChatPaivate(mViewedUser) ? View.VISIBLE : View.GONE);
        tvPrivateChat.setVisibility(getCanChatPaivate(mUser) && getCanChatPaivate(mViewedUser) ? View.VISIBLE : View.GONE);
    }

    private void setupView(RoomUserInfo.DataBean user) {
        GlideImageLoader.getInstace().displayImage(getContext(), user.getAvatar(), ivAvatar);
        tvName.setText(user.getNickname());
        tvUserId.setText("ID " + user.getUserId());
        tvLocation.setText(user.getCity());
        String introduce = user.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            tvIntroduce.setText(introduce);
        }
        tvAge.setText(DateUtils.getAge(user.getBirthday()) + "岁");
        int identityId = user.getIdentityId();
        if (user.getIdentityId() == ROOM_MANAGER) {
            tvUpgrade.setText("降房管");
        }
        if (user.getDisabledService() != null) {
            for (int i = 0; i < user.getDisabledService().size(); i++) {
                if ("MUTE".equals(user.getDisabledService().get(i))) {
                    tvBan.setText("取消禁言");
                }
            }
        }
        List<RoomUserInfo.LevelMapBean> levelMap = user.getLevelList();
        for (int i = 0; i < levelMap.size(); i++) {
            if (identityId == 10 && "ANCHOR_LEVEL".equals(levelMap.get(i).getLevelType())) {
                int levelValue = levelMap.get(i).getLevelValue();
                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
            } else if ("USER_LEVEL".equals(levelMap.get(i).getLevelType())) {
                int levelValue = levelMap.get(i).getLevelValue();
                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));

            }
        }
    }

    @OnClick({R.id.btn_dismiss, R.id.tv_private_chat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dismiss:
                dismiss();
                break;
            case R.id.tv_private_chat:
                EventBus.getDefault().post(new PrivateChatSelectedEvent(mViewedUser));
                dismiss();
                if (listener != null) {
                    listener.onPrivateChatClick();
                }
                break;
            case R.id.tv_ban:
                if (mViewedUser.getDisabledService() != null) {
                    for (int i = 0; i < mViewedUser.getDisabledService().size(); i++) {
                        if ("MUTE".equals(mViewedUser.getDisabledService().get(i))) {
                            severOperate("CANCEL_MUTE");
                            return;
                        }
                    }
                }
                severOperate("MUTE");
                break;
            case R.id.tv_upgrade:
                if (mViewedUser.getIdentityId() == UserIdentity.ROOM_MANAGER) {
                    cancelManager();
                } else {
                    setManager();
                }
                break;
            case R.id.tv_kick_out:
                severOperate("KICK");
                break;
        }
    }

    public BaseAwesomeDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnClickListener listener;

    public interface OnClickListener {
        void onPrivateChatClick();
    }

    private void severOperate(String operate) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("serviceCode", operate);
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mViewedUser.getUserId());
        ApiFactory.getInstance().getApi(Api.class)
                .serverOprate(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(AudienceInfoDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void setManager() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mViewedUser.getUserId());
        ApiFactory.getInstance().getApi(Api.class)
                .setManager(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(AudienceInfoDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void cancelManager() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("programId", mProgramId);
        paramsMap.put("userId", mViewedUser.getUserId());
        ApiFactory.getInstance().getApi(Api.class)
                .cancleManager(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(AudienceInfoDialog.this) {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        ToastUtils.showToast("操作成功");
                        dismiss();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}

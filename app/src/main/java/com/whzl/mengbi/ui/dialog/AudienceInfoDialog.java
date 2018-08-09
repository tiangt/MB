package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.PrivateChatSelectedEvent;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtil;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    //    @BindView(R.id.tv_ban)
//    TextView tvBan;
//    @BindView(R.id.tv_kick_out)
//    TextView tvKickOut;
//    @BindView(R.id.tv_upgrade)
//    TextView tvUpgrade;
    @BindView(R.id.tv_private_chat)
    TextView tvPrivateChat;
    @BindView(R.id.ll_option_container)
    LinearLayout llOptionContainer;
    private RoomUserInfo.DataBean roomUserInfo;

    public static AudienceInfoDialog newInstance(long userId, int programId) {
        AudienceInfoDialog dialog = new AudienceInfoDialog();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_audience_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        long audienceId = getArguments().getLong("userId");
        int programId = getArguments().getInt("programId");
        getUserInfo(audienceId, programId);
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
                boolean canPrivateChat = (boolean) SPUtils.get(getContext(), SpConfig.KEY_PRIVATE_CHAT, false);
                RoomUserInfo roomUserInfoData = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfoData.getCode() == 200) {
                    if (roomUserInfoData.getData() != null) {
                        roomUserInfo = roomUserInfoData.getData();
                        GlideImageLoader.getInstace().displayImage(getContext(), AudienceInfoDialog.this.roomUserInfo.getAvatar(), ivAvatar);
                        tvName.setText(AudienceInfoDialog.this.roomUserInfo.getNickname());
                        tvUserId.setText("ID " + roomUserInfo.getUserId());
                        tvLocation.setText(roomUserInfo.getCity());
                        String introduce = roomUserInfo.getIntroduce();
                        if (!TextUtils.isEmpty(introduce)) {
                            tvIntroduce.setText(introduce);
                        }
                        tvAge.setText(DateUtils.getAge(AudienceInfoDialog.this.roomUserInfo.getBirthday()) + "岁");
                        int identityId = AudienceInfoDialog.this.roomUserInfo.getIdentityId();
                        if (identityId == UserIdentity.ANCHOR
                                || identityId == UserIdentity.OPTR_MANAGER
                                || identityId == UserIdentity.ROOM_MANAGER) {
                            if (canPrivateChat) {
                                llOptionContainer.setVisibility(View.VISIBLE);
                            }
                        }
                        List<RoomUserInfo.LevelMapBean> levelMap = AudienceInfoDialog.this.roomUserInfo.getLevelList();
                        for (int i = 0; i < levelMap.size(); i++) {
                            if (identityId == 10 && "ANCHOR_LEVEL".equals(levelMap.get(i).getLevelType())) {
                                int levelValue = levelMap.get(i).getLevelValue();
                                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                            } else if ("USER_LEVEL".equals(levelMap.get(i).getLevelType())) {
                                int levelValue = levelMap.get(i).getLevelValue();
                                ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                                if (levelValue > 5 && canPrivateChat) {
                                    llOptionContainer.setVisibility(View.VISIBLE);
                                }
                            } else if ("ROYAL".equals(levelMap.get(i).getLevelType())) {
                                int levelValue = levelMap.get(i).getLevelValue();
                                if (levelValue > 0 && canPrivateChat) {
                                    llOptionContainer.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        if (AudienceInfoDialog.this.roomUserInfo.getGoodsList() != null) {
                            for (int i = 0; i < AudienceInfoDialog.this.roomUserInfo.getGoodsList().size(); i++) {
                                if ("GUARD".equals(AudienceInfoDialog.this.roomUserInfo.getGoodsList().get(i).getGoodsType()) && canPrivateChat) {
                                    llOptionContainer.setVisibility(View.VISIBLE);
                                }
                                if ("VIP".equals(AudienceInfoDialog.this.roomUserInfo.getGoodsList().get(i).getGoodsType()) && canPrivateChat) {
                                    llOptionContainer.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });


    }

    @OnClick({R.id.btn_dismiss, R.id.tv_private_chat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dismiss:
                dismiss();
                break;
            case R.id.tv_private_chat:
                EventBus.getDefault().post(new PrivateChatSelectedEvent(roomUserInfo));
                dismiss();
                if (listener != null) {
                    listener.onClick();
                }
                break;
        }
    }

    public BaseAwesomeDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick();
    }
}

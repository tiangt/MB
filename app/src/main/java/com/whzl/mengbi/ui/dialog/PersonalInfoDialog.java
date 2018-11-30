package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.PersonalInfoActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.11.29
 */
public class PersonalInfoDialog extends BaseAwesomeDialog {

    @BindView(R.id.btn_personal)
    ImageButton mPersonalPage;
    @BindView(R.id.btn_close)
    ImageButton mClose;
    @BindView(R.id.iv_user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.tv_nick_name)
    TextView mTvNickName;
    @BindView(R.id.tv_anchor_id)
    TextView mTvAnchorId;
    @BindView(R.id.tv_introduce)
    TextView mTvIntroduce;
    @BindView(R.id.ll_level_container)
    LinearLayout linearLayout;

    private float dimAmount = 0.5f;//灰度深浅
    private long mUserId;
    private int mProgramId;
    private RoomUserInfo.DataBean mViewedUser;
    private List<RoomUserInfo.LevelMapBean> levelMapBeans;
    private int levelValue;
    private String levelType;

    public static PersonalInfoDialog newInstance(long userId, int programId) {
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        PersonalInfoDialog dialog = new PersonalInfoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_personal_info;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        mUserId = getArguments().getLong("userId");
        mProgramId = getArguments().getInt("programId");
        setAnimation();
        getUserInfo(mUserId, mProgramId);
    }

    private void setAnimation() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
        setCancelable(true);
    }

    @OnClick({R.id.btn_personal, R.id.btn_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal:
                Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
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
                    }
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    private void setupView(RoomUserInfo.DataBean user) {
        GlideImageLoader.getInstace().displayImage(getContext(), user.getAvatar(), mUserAvatar);
        mTvNickName.setText(user.getNickname());
        mTvAnchorId.setText("ID " + user.getUserId());
        String introduce = user.getIntroduce();
        if (!TextUtils.isEmpty(introduce)) {
            mTvIntroduce.setText(introduce);
        }
        int identityId = user.getIdentityId();
        linearLayout.removeAllViews();
        ImageView imageView = new ImageView(getContext());
        levelMapBeans = new ArrayList<>();
        if (user.getLevelList() != null) {
            for (int i = 0; i < user.getLevelList().size(); i++) {
                levelType = user.getLevelList().get(i).getLevelType();
                levelValue = user.getLevelList().get(i).getLevelValue();
                if ("USER_LEVEL".equals(levelType)) {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                } else if ("ROYAL_LEVEL".equals(levelType)) {
                    imageView.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 20), UIUtil.dip2px(getContext(), 20));
            linearLayout.addView(imageView, params);
        }

    }
}

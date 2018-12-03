package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.PersonalInfoActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
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

    @BindView(R.id.iv_bg_personal)
    ImageView ivBgPersonal;
    @BindView(R.id.btn_buy_royal)
    ImageButton btnBuyRoyal;
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
    @BindView(R.id.tv_follow)
    TextView tvFollow;

    private float dimAmount = 0.5f;//灰度深浅
    private long mUserId;
    private int mProgramId;
    private RoomUserInfo.DataBean mViewedUser;
    private List<RoomUserInfo.LevelMapBean> levelMapBeans;
    private int levelValue;
    private String levelType;
    private boolean mIsSubs = false;
    private long mCurrentId;

    public static PersonalInfoDialog newInstance(long userId, int programId) {
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        args.putInt("programId", programId);
        PersonalInfoDialog dialog = new PersonalInfoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public static PersonalInfoDialog newInstance(long anchorId, int programId, long userId, boolean isSubs) {
        Bundle args = new Bundle();
        args.putLong("userId", anchorId); //主播ID
        args.putInt("programId", programId);
        args.putLong("currentUserId", userId); //当前用户ID
        args.putBoolean("isSubs", isSubs);
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
        mIsSubs = getArguments().getBoolean("isSubs");
        mCurrentId = getArguments().getLong("currentUserId");
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

    @OnClick({R.id.btn_personal, R.id.btn_buy_royal, R.id.tv_follow, R.id.btn_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal:
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_buy_royal:
                Intent intentShop = new Intent(getActivity(), ShopActivity.class);
                startActivity(intentShop);
                break;
            case R.id.tv_follow:
                if (mIsSubs) {
                    //取消关注
                    ((LiveDisplayActivity) getContext()).setCancelFollow(mCurrentId, mProgramId);
                    mIsSubs = false;
                    dismiss();
                } else {
                    //关注
                    ((LiveDisplayActivity) getContext()).setFollow();
                    tvFollow.setText(R.string.followed);
                    tvFollow.setTextColor(Color.GRAY);
                    mIsSubs = true;
                }
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

        //贵族等级
        if (user.getLevelList() != null) {
            for (int i = 0; i < user.getLevelList().size(); i++) {
                levelType = user.getLevelList().get(i).getLevelType();
                levelValue = user.getLevelList().get(i).getLevelValue();
                if ("ROYAL_LEVEL".equals(levelType)) {
                    setRoyalBackground(levelValue);
                    if (levelValue == 0) {
                        break;
                    } else {
                        if (levelValue == 0) {
                            btnBuyRoyal.setVisibility(View.VISIBLE);
                        }
                        ImageView royalImage = new ImageView(getContext());
                        royalImage.setImageResource(ResourceMap.getResourceMap().getRoyalLevelIcon(levelValue));
                        LinearLayout.LayoutParams royalParams = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 20), UIUtil.dip2px(getContext(), 20));
                        royalParams.leftMargin = UIUtil.dip2px(getContext(), 6);
                        royalParams.gravity = UIUtil.dip2px(getContext(), 6);
                        linearLayout.addView(royalImage, royalParams);
                    }
                }
            }
        }

        //主播用户等级
        if (user.getLevelList() != null) {
            for (int i = 0; i < user.getLevelList().size(); i++) {
                levelType = user.getLevelList().get(i).getLevelType();
                levelValue = user.getLevelList().get(i).getLevelValue();
                if (identityId == 10) {
                    tvFollow.setVisibility(View.VISIBLE);
                    if (mIsSubs) {
                        tvFollow.setText(R.string.followed);
                        tvFollow.setTextColor(Color.GRAY);
                    } else {
                        tvFollow.setText(R.string.not_followed);
                        tvFollow.setTextColor(Color.RED);
                    }
                    imageView.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelValue));
                } else {
                    imageView.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelValue));
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 20), UIUtil.dip2px(getContext(), 20));
            params.leftMargin = UIUtil.dip2px(getContext(), 6);
            params.gravity = UIUtil.dip2px(getContext(), 6);
            linearLayout.addView(imageView, params);
        }

        //守护，VIP
        if (user.getGoodsList() != null) {
            for (int i = 0; i < user.getGoodsList().size(); i++) {
                RoomUserInfo.GoodsListBean goodsListBean = user.getGoodsList().get(i);
                if ("BADGE".equals(goodsListBean.getGoodsType()) || "GUARD".equals(goodsListBean.getGoodsType()) || "VIP".equals(goodsListBean.getGoodsType())) {
                    Glide.with(getContext())
                            .load(goodsListBean.getGoodsIcon())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    if (getContext() == null) {
                                        return;
                                    }
                                    int intrinsicHeight = resource.getIntrinsicHeight();
                                    int intrinsicWidth = resource.getIntrinsicWidth();
                                    ImageView goodImage = new ImageView(getContext());
                                    goodImage.setImageDrawable(resource);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext()
                                            , intrinsicWidth / 4f * 3)
                                            , UIUtil.dip2px(getContext(), intrinsicHeight / 4f * 3));
                                    params.leftMargin = UIUtil.dip2px(getContext(), 6);
                                    params.gravity = UIUtil.dip2px(getContext(), 6);
                                    linearLayout.addView(goodImage, params);
                                }
                            });
                }
            }
        }

        //房管
        if (identityId == UserIdentity.ROOM_MANAGER) {
            ImageView mgrView = new ImageView(getContext());
            mgrView.setImageResource(R.drawable.room_manager);
            LinearLayout.LayoutParams mgrViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mgrViewParams.leftMargin = UIUtil.dip2px(getContext(), 6);
            mgrViewParams.gravity = UIUtil.dip2px(getContext(), 6);
            linearLayout.addView(mgrView, mgrViewParams);
        }
    }

    private void setRoyalBackground(int royalLevel) {
        switch (royalLevel) {
            case 0:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_civilian, ivBgPersonal);
                break;
            case 1:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_bronze, ivBgPersonal);
                break;
            case 2:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_silver, ivBgPersonal);
                break;
            case 3:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_gold, ivBgPersonal);
                break;
            case 4:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_platinum, ivBgPersonal);
                break;
            case 5:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_diamond, ivBgPersonal);
                break;
            case 6:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_star, ivBgPersonal);
                break;
            case 7:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_king, ivBgPersonal);
                break;
            default:
                GlideImageLoader.getInstace().displayImage(getActivity(), R.drawable.bg_civilian, ivBgPersonal);
                break;
        }
    }
}
